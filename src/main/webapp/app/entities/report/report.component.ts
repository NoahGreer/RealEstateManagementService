import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
// import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';

// import { IReport } from 'app/shared/model/report.model';

import { ReportService } from './report.service';

@Component({
  selector: 'jhi-report',
  templateUrl: './report.component.html'
})
export class ReportComponent implements OnInit, OnDestroy {
  report?: String[];
  eventSubscriber?: Subscription;

  constructor(
    protected reportService: ReportService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {}

  // loadRentsPaid(): void {
  //   this.reportService.findRentsPaid().subscribe(
  //     (res: HttpResponse<String[]>) => (this.report = res.body || []));
  // }

  ngOnInit(): void {
    this.reportService.findRentsPaid().subscribe((res: HttpResponse<String[]>) => (this.report = res.body || []));
    // this.activatedRoute.data.subscribe(({ report }) => {
    //   this.report = report;
    //   // eslint-disable-next-line no-console
    //   console.log("Report Assigned in ngOnInit");
    // });
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }
}
