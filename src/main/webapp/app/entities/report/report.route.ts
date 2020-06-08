import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';
import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ReportService } from './report.service';
import { ReportComponent } from './report.component';
import { ReportLandingComponent } from './report.landing.component';

@Injectable({ providedIn: 'root' })
export class ReportResolve implements Resolve<any> {
  constructor(private service: ReportService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<any[]> | Observable<never> {
    switch (route.url.toString()) {
      case 'available-apartments':
        return this.service.getAvailableApartments().pipe(
          flatMap((report: HttpResponse<any[]>) => {
            if (report.body) {
              return of(report.body);
            } else {
              return EMPTY;
            }
          })
        );
      case 'contacts':
        return this.service.getContacts().pipe(
          flatMap((report: HttpResponse<any[]>) => {
            if (report.body) {
              return of(report.body);
            } else {
              return EMPTY;
            }
          })
        );
      case 'emails':
        return this.service.getEmails().pipe(
          flatMap((report: HttpResponse<any[]>) => {
            if (report.body) {
              return of(report.body);
            } else {
              return EMPTY;
            }
          })
        );

      case 'pet-owners':
        return this.service.getPetOwners().pipe(
          flatMap((report: HttpResponse<any[]>) => {
            if (report.body) {
              return of(report.body);
            } else {
              return EMPTY;
            }
          })
        );
      case 'open-maintenance':
        return this.service.getOpenMaintenance().pipe(
          flatMap((report: HttpResponse<any[]>) => {
            if (report.body) {
              return of(report.body);
            } else {
              return EMPTY;
            }
          })
        );
      case 'tax-history':
        return this.service.getPropertyTaxHistory().pipe(
          flatMap((report: HttpResponse<any[]>) => {
            if (report.body) {
              return of(report.body);
            } else {
              return EMPTY;
            }
          })
        );
      case 'vehicles-by-apartment':
        return this.service.getVehiclesByApartment(route.queryParams['passedParam']).pipe(
          flatMap((report: HttpResponse<any[]>) => {
            if (report.body) {
              return of(report.body);
            } else {
              return EMPTY;
            }
          })
        );
      case 'vehicles-by-building':
        return this.service.getVehiclesByBuilding(route.queryParams['passedParam']).pipe(
          flatMap((report: HttpResponse<any[]>) => {
            if (report.body) {
              return of(report.body);
            } else {
              return EMPTY;
            }
          })
        );
      case 'infractions-by-year':
        return this.service.getInfractionsByYear(route.queryParams['passedParam'].substring(0, 4)).pipe(
          flatMap((report: HttpResponse<any[]>) => {
            if (report.body) {
              return of(report.body);
            } else {
              return EMPTY;
            }
          })
        );
      case 'infractions-by-apartment':
        return this.service.getInfractionsByApartment(route.queryParams['passedParam']).pipe(
          flatMap((report: HttpResponse<any[]>) => {
            if (report.body) {
              return of(report.body);
            } else {
              return EMPTY;
            }
          })
        );
      case 'leases-by-expiration':
        return this.service.getNextExpiringLeases(route.queryParams['passedParam']).pipe(
          flatMap((report: HttpResponse<any[]>) => {
            if (report.body) {
              return of(report.body);
            } else {
              return EMPTY;
            }
          })
        );
      case 'rent-delinquencies':
        return this.service.getRentDelinquencies(route.queryParams['passedParam']).pipe(
          flatMap((report: HttpResponse<any[]>) => {
            if (report.body) {
              return of(report.body);
            } else {
              return EMPTY;
            }
          })
        );
      case 'maintenance-by-contractor':
        return this.service.getContractorMaintenanceHistory(route.queryParams['passedParam']).pipe(
          flatMap((report: HttpResponse<any[]>) => {
            if (report.body) {
              return of(report.body);
            } else {
              return EMPTY;
            }
          })
        );
      case 'maintenance-by-apartment':
        return this.service.getApartmentMaintenanceHistory(route.queryParams['passedParam']).pipe(
          flatMap((report: HttpResponse<any[]>) => {
            if (report.body) {
              return of(report.body);
            } else {
              return EMPTY;
            }
          })
        );
      case 'contractor-by-jobtype':
        return this.service.getContractorByJobType(route.queryParams['passedParam']).pipe(
          flatMap((report: HttpResponse<any[]>) => {
            if (report.body) {
              return of(report.body);
            } else {
              return EMPTY;
            }
          })
        );
      case 'tenants-by-apartment':
        return this.service.getTenantsByApartment(route.queryParams['passedParam']).pipe(
          flatMap((report: HttpResponse<any[]>) => {
            if (report.body) {
              return of(report.body);
            } else {
              return EMPTY;
            }
          })
        );
      case 'rents-paid':
        return this.service.getRentsPaid(route.queryParams['passedParam']).pipe(
          flatMap((report: HttpResponse<any[]>) => {
            if (report.body) {
              return of(report.body);
            } else {
              return EMPTY;
            }
          })
        );
      default:
        return EMPTY;
    }
  }
}

@Injectable({ providedIn: 'root' })
export class ReportLandingResolve implements Resolve<any> {
  constructor(private service: ReportService, private router: Router) {}
  resolve(): Observable<any[]> | Observable<never> {
    return EMPTY;
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
      pageTitle: 'realEstateManagementServiceApp.report.home.title',
      objectType: ''
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
      report: ReportResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.report.home.rents-paid',
      objectType: 'paid rent'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'vehicles-by-building',
    component: ReportComponent,
    resolve: {
      report: ReportResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.report.home.vehicles-by-building',
      objectType: 'authorized vehicle'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'vehicles-by-apartment',
    component: ReportComponent,
    resolve: {
      report: ReportResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.report.home.vehicles-by-apartment',
      objectType: 'authorized vehicle'
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
      pageTitle: 'realEstateManagementServiceApp.report.home.available-apartments',
      objectType: 'available apartment'
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
      pageTitle: 'realEstateManagementServiceApp.report.home.contacts',
      objectType: 'tenant contact'
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
      pageTitle: 'realEstateManagementServiceApp.report.home.emails',
      objectType: 'tenant email'
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
      pageTitle: 'realEstateManagementServiceApp.report.home.infractions-by-year',
      objectType: 'infraction'
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
      pageTitle: 'realEstateManagementServiceApp.report.home.infractions-by-apartment',
      objectType: 'infraction'
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
      pageTitle: 'realEstateManagementServiceApp.report.home.tenants-by-apartment',
      objectType: 'tenant'
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
      pageTitle: 'realEstateManagementServiceApp.report.home.leases-by-expiration',
      objectType: 'lease'
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
      pageTitle: 'realEstateManagementServiceApp.report.home.pet-owners',
      objectType: 'pet/pet owner'
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
      pageTitle: 'realEstateManagementServiceApp.report.home.tax-history',
      objectType: 'tax report'
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
      pageTitle: 'realEstateManagementServiceApp.report.home.open-maintenance',
      objectType: 'maintenance request'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'rent-delinquencies',
    component: ReportComponent,
    resolve: {
      report: ReportResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'realEstateManagementServiceApp.report.home.rent-delinquencies',
      objectType: 'rent delinquency'
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
      pageTitle: 'realEstateManagementServiceApp.report.home.maintenance-by-apartment',
      objectType: 'maintenance'
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
      pageTitle: 'realEstateManagementServiceApp.report.home.maintenance-by-contractor',
      objectType: 'maintenance'
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
      pageTitle: 'realEstateManagementServiceApp.report.home.contractor-by-jobtype',
      objectType: 'contractor'
    },
    canActivate: [UserRouteAccessService]
  }
];
