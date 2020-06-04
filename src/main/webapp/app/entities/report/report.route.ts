import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, Routes, Router } from '@angular/router';
// import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
// import { IReport, Report } from 'app/shared/model/report.model';
// import { IRent, Rent } from 'app/shared/model/rent.model';
import { ReportService } from './report.service';
import { ReportComponent } from './report.component';
import { ReportLandingComponent } from './report.landing.component';

@Injectable({ providedIn: 'root' })
export class ReportResolve implements Resolve<any> {
  constructor(private service: ReportService, private router: Router) {}

  resolve(): Observable<any[]> | Observable<never> {
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

  resolve(): Observable<any[]> | Observable<never> {
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
export class ReportLandingResolve implements Resolve<any> {
  constructor(private service: ReportService, private router: Router) {}

  resolve(): Observable<any[]> | Observable<never> {
    return EMPTY;
  }
}

@Injectable({ providedIn: 'root' })
export class AuthorizedVehicleReportResolve implements Resolve<any> {
  constructor(private service: ReportService, private router: Router) {}

  resolve(): Observable<any[]> | Observable<never> {
    return this.service.getAuthorizedVehicles(this.service.passedParamValue).pipe(
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
    component: ReportLandingComponent,
    resolve: {
      report: ReportLandingResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.report.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'landing',
    component: ReportLandingComponent,
    resolve: {
      report: ReportLandingResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.report.home.title'
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
  },
  {
    path: 'available-apartments',
    component: ReportComponent,
    resolve: {
      report: ReportResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.report.home.available-apartments'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'contacts',
    component: ReportComponent,
    resolve: {
      report: ReportResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.report.home.contacts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'emails',
    component: ReportComponent,
    resolve: {
      report: ReportResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.report.home.emails'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'infractions-by-year',
    component: ReportComponent,
    resolve: {
      report: ReportResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.report.home.infractions-by-year'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'infractions-by-apartment',
    component: ReportComponent,
    resolve: {
      report: ReportResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.report.home.infractions-by-apartment'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'tenants-by-apartment',
    component: ReportComponent,
    resolve: {
      report: ReportResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.report.home.tenants-by-apartment'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'leases-by-expiration',
    component: ReportComponent,
    resolve: {
      report: ReportResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.report.home.leases-by-expiration'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'pet-owners',
    component: ReportComponent,
    resolve: {
      report: ReportResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.report.home.pet-owners'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'tax-history',
    component: ReportComponent,
    resolve: {
      report: ReportResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.report.home.tax-history'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'open-maintenance',
    component: ReportComponent,
    resolve: {
      report: ReportResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.report.home.open-maintenance'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'maintenance-by-apartment',
    component: ReportComponent,
    resolve: {
      report: ReportResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.report.home.maintenance-by-apartment'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'maintenance-by-contractor',
    component: ReportComponent,
    resolve: {
      report: ReportResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.report.home.maintenance-by-contractor'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'contractor-by-jobtype',
    component: ReportComponent,
    resolve: {
      report: ReportResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.report.home.contractor-by-jobtype'
    },
    canActivate: [UserRouteAccessService]
  }
];
