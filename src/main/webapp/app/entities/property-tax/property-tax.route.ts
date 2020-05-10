import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPropertyTax, PropertyTax } from 'app/shared/model/property-tax.model';
import { PropertyTaxService } from './property-tax.service';
import { PropertyTaxComponent } from './property-tax.component';
import { PropertyTaxDetailComponent } from './property-tax-detail.component';
import { PropertyTaxUpdateComponent } from './property-tax-update.component';

@Injectable({ providedIn: 'root' })
export class PropertyTaxResolve implements Resolve<IPropertyTax> {
  constructor(private service: PropertyTaxService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPropertyTax> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((propertyTax: HttpResponse<PropertyTax>) => {
          if (propertyTax.body) {
            return of(propertyTax.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PropertyTax());
  }
}

export const propertyTaxRoute: Routes = [
  {
    path: '',
    component: PropertyTaxComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'realEstateManagementServiceApp.propertyTax.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PropertyTaxDetailComponent,
    resolve: {
      propertyTax: PropertyTaxResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.propertyTax.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PropertyTaxUpdateComponent,
    resolve: {
      propertyTax: PropertyTaxResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.propertyTax.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PropertyTaxUpdateComponent,
    resolve: {
      propertyTax: PropertyTaxResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.propertyTax.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
