import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import * as moment from 'moment';
import { ReportService } from './report.service';

@Component({
  selector: 'jhi-report',
  templateUrl: './report.component.html'
})
export class ReportComponent implements OnInit, OnDestroy {
  report?: any[];
  data?: Object;
  objectType?: string;
  eventSubscriber?: Subscription;
  reportType: string;
  reportParam: any;

  constructor(
    protected reportService: ReportService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute,
    protected router: Router
  ) {
    this.reportType = '';
    this.reportParam = '';
  }

  ngOnInit(): void {
    this.data = this.activatedRoute.snapshot.data['pageTitle'];
    this.reportType = this.reportService.reportType;
    this.reportParam = this.reportService.passedParamValue;

    if (this.reportParam !== undefined) {
      switch (this.router.url) {
        case '/report/rents-paid':
          this.objectType = 'paid rent';
          this.reportService.getRentsPaid(this.reportParam).subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
          break;

        case '/report/available-apartments':
          this.objectType = 'apartment';
          this.reportService.getAvailableApartments().subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
          break;

        case '/report/authorized-vehicles':
          this.objectType = 'authorized vehicle';
          this.reportService
            .getAuthorizedVehicles(this.reportParam)
            .subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
          break;

        case '/report/contacts':
          this.objectType = 'contact';
          this.reportService.getContacts().subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
          break;

        case '/report/emails':
          this.objectType = 'email';
          this.reportService.getEmails().subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
          break;

        case '/report/infractions-by-year':
          this.objectType = 'infraction';
          this.reportService
            .getInfractionsByYear(this.getYearByDate(this.reportParam))
            .subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
          break;

        case '/report/infractions-by-apartment':
          this.objectType = 'infraction';
          this.reportService
            .getInfractionsByApartment(this.reportParam)
            .subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
          break;
        case '/report/tenants-by-apartment':
          this.objectType = 'tenant';
          this.reportService
            .getTenantsByApartment(this.reportParam)
            .subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
          break;

        case '/report/leases-by-expiration':
          this.objectType = 'lease';
          if (typeof this.reportParam === 'number') {
            this.reportService
              .getNextExpiringLeases(this.reportParam)
              .subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
          } else {
            this.reportService.getNextExpiringLeases(5).subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
          }
          break;

        case '/report/pet-owners':
          this.objectType = 'pet owner';
          this.reportService.getPetOwners().subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
          break;

        case '/report/open-maintenance':
          this.objectType = 'open maintenance';
          this.reportService.getOpenMaintenance().subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
          break;
        case '/report/maintenance-by-contractor':
          this.objectType = 'maintenance';
          this.reportService
            .getContractorMaintenanceHistory(this.reportParam)
            .subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
          break;
        case '/report/maintenance-by-apartment':
          this.objectType = 'maintenance';
          this.reportService
            .getApartmentMaintenanceHistory(this.reportParam)
            .subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
          break;

        case '/report/contractor-by-jobtype':
          this.objectType = 'contractor';
          this.reportService
            .getContractorByJobType(this.reportParam)
            .subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
          break;
        case '/report/tax-history':
          this.objectType = 'tax entry';
          this.reportService.getPropertyTaxHistory().subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
          break;

        default:
          this.objectType = 'test';
          this.reportService.getTestReport().subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
      }
    }
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  formatDate(date: Date): string {
    return moment(date)
      .format('YYYY-MM-DD')
      .toString();
  }

  getTodaysDate(): string {
    return moment()
      .format('YYYY-MM-DD')
      .toString();
  }

  getYearByDate(date: Date): string {
    return moment(date).format('YYYY');
  }
}
