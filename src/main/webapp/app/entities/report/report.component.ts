import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
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
      this.activatedRoute.data.subscribe(({ report }) => (this.report = report));
      this.activatedRoute.data.subscribe(({ objectType }) => (this.objectType = objectType));
    }
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }
}
