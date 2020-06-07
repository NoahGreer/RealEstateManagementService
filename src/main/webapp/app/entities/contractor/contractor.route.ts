import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IContractor, Contractor } from 'app/shared/model/contractor.model';
import { ContractorService } from './contractor.service';
import { ContractorComponent } from './contractor.component';
import { ContractorDetailComponent } from './contractor-detail.component';
import { ContractorUpdateComponent } from './contractor-update.component';

@Injectable({ providedIn: 'root' })
export class ContractorResolve implements Resolve<IContractor> {
  constructor(private service: ContractorService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContractor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((contractor: HttpResponse<Contractor>) => {
          if (contractor.body) {
            return of(contractor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Contractor());
  }
}

export const contractorRoute: Routes = [
  {
    path: '',
    component: ContractorComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'realEstateManagementServiceApp.contractor.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ContractorDetailComponent,
    resolve: {
      contractor: ContractorResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.contractor.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ContractorUpdateComponent,
    resolve: {
      contractor: ContractorResolve
    },
    data: {
      authorities: [Authority.MANAGER],
      pageTitle: 'realEstateManagementServiceApp.contractor.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ContractorUpdateComponent,
    resolve: {
      contractor: ContractorResolve
    },
    data: {
      authorities: [Authority.MANAGER],
      pageTitle: 'realEstateManagementServiceApp.contractor.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
