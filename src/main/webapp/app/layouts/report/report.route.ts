import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IReport, Report } from 'app/shared/model/report.model';
import { ReportService } from './report.service';
import { ReportComponent } from './report.component';

@Injectable({ providedIn: 'root' })
export class ReportResolve implements Resolve<IReport> {
  constructor(private service: ReportService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReport> | Observable<never> {
    const testReport = new Report(12, 'Report Name', ['House', 'Apple', 'Words']);

    return of(testReport);
  }
}

export const reportRoute: Routes = [
  {
    path: '',
    component: ReportComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'realEstateManagementServiceApp.building.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
