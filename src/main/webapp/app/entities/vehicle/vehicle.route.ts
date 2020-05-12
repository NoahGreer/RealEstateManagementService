import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IVehicle, Vehicle } from 'app/shared/model/vehicle.model';
import { VehicleService } from './vehicle.service';
import { VehicleComponent } from './vehicle.component';
import { VehicleDetailComponent } from './vehicle-detail.component';
import { VehicleUpdateComponent } from './vehicle-update.component';

@Injectable({ providedIn: 'root' })
export class VehicleResolve implements Resolve<IVehicle> {
  constructor(private service: VehicleService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVehicle> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((vehicle: HttpResponse<Vehicle>) => {
          if (vehicle.body) {
            return of(vehicle.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Vehicle());
  }
}

export const vehicleRoute: Routes = [
  {
    path: '',
    component: VehicleComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,desc',
      pageTitle: 'realEstateManagementServiceApp.vehicle.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'report',
    component: VehicleComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'realEstateManagementServiceApp.vehicle.home.report'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: VehicleDetailComponent,
    resolve: {
      vehicle: VehicleResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.vehicle.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: VehicleUpdateComponent,
    resolve: {
      vehicle: VehicleResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.vehicle.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: VehicleUpdateComponent,
    resolve: {
      vehicle: VehicleResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.vehicle.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
