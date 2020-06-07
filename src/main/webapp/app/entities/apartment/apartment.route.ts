import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IApartment, Apartment } from 'app/shared/model/apartment.model';
import { ApartmentService } from './apartment.service';
import { ApartmentComponent } from './apartment.component';
import { ApartmentDetailComponent } from './apartment-detail.component';
import { ApartmentUpdateComponent } from './apartment-update.component';

@Injectable({ providedIn: 'root' })
export class ApartmentResolve implements Resolve<IApartment> {
  constructor(private service: ApartmentService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IApartment> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((apartment: HttpResponse<Apartment>) => {
          if (apartment.body) {
            return of(apartment.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Apartment());
  }
}

export const apartmentRoute: Routes = [
  {
    path: '',
    component: ApartmentComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'realEstateManagementServiceApp.apartment.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ApartmentDetailComponent,
    resolve: {
      apartment: ApartmentResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.apartment.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ApartmentUpdateComponent,
    resolve: {
      apartment: ApartmentResolve
    },
    data: {
      authorities: [Authority.MANAGER],
      pageTitle: 'realEstateManagementServiceApp.apartment.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ApartmentUpdateComponent,
    resolve: {
      apartment: ApartmentResolve
    },
    data: {
      authorities: [Authority.MANAGER],
      pageTitle: 'realEstateManagementServiceApp.apartment.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
