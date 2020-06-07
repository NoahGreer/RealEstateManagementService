import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IJobType, JobType } from 'app/shared/model/job-type.model';
import { JobTypeService } from './job-type.service';
import { JobTypeComponent } from './job-type.component';
import { JobTypeDetailComponent } from './job-type-detail.component';
import { JobTypeUpdateComponent } from './job-type-update.component';

@Injectable({ providedIn: 'root' })
export class JobTypeResolve implements Resolve<IJobType> {
  constructor(private service: JobTypeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IJobType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((jobType: HttpResponse<JobType>) => {
          if (jobType.body) {
            return of(jobType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new JobType());
  }
}

export const jobTypeRoute: Routes = [
  {
    path: '',
    component: JobTypeComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.jobType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: JobTypeDetailComponent,
    resolve: {
      jobType: JobTypeResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.jobType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: JobTypeUpdateComponent,
    resolve: {
      jobType: JobTypeResolve
    },
    data: {
      authorities: [Authority.MANAGER],
      pageTitle: 'realEstateManagementServiceApp.jobType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: JobTypeUpdateComponent,
    resolve: {
      jobType: JobTypeResolve
    },
    data: {
      authorities: [Authority.MANAGER],
      pageTitle: 'realEstateManagementServiceApp.jobType.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
