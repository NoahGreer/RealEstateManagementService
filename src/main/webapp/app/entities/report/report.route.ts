import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IReport, Report } from 'app/shared/model/report.model';
import { ReportService } from './report.service';
import { ReportComponent } from './report.component';

@Injectable({ providedIn: 'root' })
export class ReportResolve implements Resolve<String[]> {
  constructor(private service: ReportService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<String[]> | Observable<never> {
    return this.service.findRentsPaid().pipe(
      flatMap((report: HttpResponse<String[]>) => {
        if (report.body) {
          return of(report.body);
        } else {
          this.router.navigate(['404']);
          return EMPTY;
        }
      })
    );
  }
}

export const reportRoute: Routes = [
  {
    path: '',
    component: ReportComponent,
    resolve: {
      report: ReportResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'WORKING REPORT'
    },
    canActivate: [UserRouteAccessService]
  }
];
