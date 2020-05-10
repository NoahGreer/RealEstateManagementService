import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMaintenance, Maintenance } from 'app/shared/model/maintenance.model';
import { MaintenanceService } from './maintenance.service';
import { MaintenanceComponent } from './maintenance.component';
import { MaintenanceDetailComponent } from './maintenance-detail.component';
import { MaintenanceUpdateComponent } from './maintenance-update.component';

@Injectable({ providedIn: 'root' })
export class MaintenanceResolve implements Resolve<IMaintenance> {
  constructor(private service: MaintenanceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMaintenance> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((maintenance: HttpResponse<Maintenance>) => {
          if (maintenance.body) {
            return of(maintenance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Maintenance());
  }
}

export const maintenanceRoute: Routes = [
  {
    path: '',
    component: MaintenanceComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'realEstateManagementServiceApp.maintenance.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MaintenanceDetailComponent,
    resolve: {
      maintenance: MaintenanceResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.maintenance.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MaintenanceUpdateComponent,
    resolve: {
      maintenance: MaintenanceResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.maintenance.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MaintenanceUpdateComponent,
    resolve: {
      maintenance: MaintenanceResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.maintenance.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
