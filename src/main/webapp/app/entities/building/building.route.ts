import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IBuilding, Building } from 'app/shared/model/building.model';
import { BuildingService } from './building.service';
import { BuildingComponent } from './building.component';
import { BuildingDetailComponent } from './building-detail.component';
import { BuildingUpdateComponent } from './building-update.component';

@Injectable({ providedIn: 'root' })
export class BuildingResolve implements Resolve<IBuilding> {
  constructor(private service: BuildingService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBuilding> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((building: HttpResponse<Building>) => {
          if (building.body) {
            return of(building.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Building());
  }
}

export const buildingRoute: Routes = [
  {
    path: '',
    component: BuildingComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'realEstateManagementServiceApp.building.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: BuildingDetailComponent,
    resolve: {
      building: BuildingResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.building.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: BuildingUpdateComponent,
    resolve: {
      building: BuildingResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.building.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: BuildingUpdateComponent,
    resolve: {
      building: BuildingResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.building.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
