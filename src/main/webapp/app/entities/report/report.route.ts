import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
// import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
// import { IReport, Report } from 'app/shared/model/report.model';
// import { IRent, Rent } from 'app/shared/model/rent.model';
import { ReportService } from './report.service';
import { ReportComponent } from './report.component';

@Injectable({ providedIn: 'root' })
export class ReportResolve implements Resolve<any> {
  constructor(private service: ReportService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<any[]> | Observable<never> {
    return this.service.getTestReport().pipe(
      flatMap((report: HttpResponse<any[]>) => {
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

@Injectable({ providedIn: 'root' })
export class RentsPaidReportResolve implements Resolve<any> {
  constructor(private service: ReportService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<any[]> | Observable<never> {
    return this.service.getRentsPaid('2020-05-01').pipe(
      flatMap((report: HttpResponse<any[]>) => {
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

@Injectable({ providedIn: 'root' })
export class AuthorizedVehicleReportResolve implements Resolve<any> {
  constructor(private service: ReportService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<any[]> | Observable<never> {
    return this.service.getAuthorizedVehicles(1).pipe(
      flatMap((report: HttpResponse<any[]>) => {
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
      pageTitle: 'realEstateManagementServiceApp.report.home.test-report'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'rents-paid',
    component: ReportComponent,
    resolve: {
      report: RentsPaidReportResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.report.home.rents-paid'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'authorized-vehicles',
    component: ReportComponent,
    resolve: {
      report: AuthorizedVehicleReportResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.report.home.authorized-vehicles'
    },
    canActivate: [UserRouteAccessService]
  }
];
