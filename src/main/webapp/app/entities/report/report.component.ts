import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import * as moment from 'moment';
// import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';

// import { IReport } from 'app/shared/model/report.model';

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
  ) {}

  ngOnInit(): void {
    this.data = this.activatedRoute.snapshot.data['pageTitle'];
    this.reportType = this.reportService.reportType;
    this.reportParam = this.reportService.passedParamValue;

    // if (this.activatedRoute.snapshot.data['reportType'] == null){
    //   this.reportType = "Default Report Type";
    // }
    // else{
    //   this.reportType = this.activatedRoute.snapshot.data['reportType'];
    // }

    // if (this.activatedRoute.snapshot.data['reportParam'] == null){
    //   this.reportParam = "Default Report Parameters";
    // }
    // else{
    //   this.reportParam = this.activatedRoute.snapshot.data['reportParam'];
    // }

    switch (this.router.url) {
      case '/report/rents-paid':
        this.objectType = 'paid rent';
        this.reportService.getRentsPaid(this.reportParam).subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
        break;
      case '/report/available-apartments':
        this.objectType = 'available apartment';
        this.reportService.getAvailableApartments().subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
        break;
      case '/report/authorized-vehicles':
        this.objectType = 'authorized vehicle';
        this.reportService.getAuthorizedVehicles(this.reportParam).subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
        break;
      case '/report/contacts':
        this.objectType = 'contact';
        this.reportService.getContacts().subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
        break;
      case '/report/emails':
        this.objectType = 'email';
        this.reportService.getEmails().subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
        break;
      default:
        this.objectType = 'test';
        this.reportService.getTestReport().subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
    }
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  getTodaysDate(): string {
    return moment().format('YYYY-MM-DD');
  }
}
