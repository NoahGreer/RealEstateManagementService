import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IInfraction, Infraction } from 'app/shared/model/infraction.model';
import { InfractionService } from './infraction.service';
import { InfractionComponent } from './infraction.component';
import { InfractionDetailComponent } from './infraction-detail.component';
import { InfractionUpdateComponent } from './infraction-update.component';

@Injectable({ providedIn: 'root' })
export class InfractionResolve implements Resolve<IInfraction> {
  constructor(private service: InfractionService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInfraction> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((infraction: HttpResponse<Infraction>) => {
          if (infraction.body) {
            return of(infraction.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Infraction());
  }
}

export const infractionRoute: Routes = [
  {
    path: '',
    component: InfractionComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'realEstateManagementServiceApp.infraction.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: InfractionDetailComponent,
    resolve: {
      infraction: InfractionResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.infraction.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: InfractionUpdateComponent,
    resolve: {
      infraction: InfractionResolve
    },
    data: {
      authorities: [Authority.MANAGER],
      pageTitle: 'realEstateManagementServiceApp.infraction.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: InfractionUpdateComponent,
    resolve: {
      infraction: InfractionResolve
    },
    data: {
      authorities: [Authority.MANAGER],
      pageTitle: 'realEstateManagementServiceApp.infraction.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
