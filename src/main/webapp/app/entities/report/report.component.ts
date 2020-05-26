import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';

import { IReport } from 'app/shared/model/report.model';

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

  constructor(
    protected reportService: ReportService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute,
    protected router: Router
  ) {}

  ngOnInit(): void {
    this.data = this.router.url;
    switch (this.router.url) {
      case '/report/rents-paid':
        this.objectType = 'rent';
        this.reportService.getRentsPaid('2020-05-01').subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
        break;
      case '/report/authorized-vehicles':
        this.objectType = 'vehicle';
        this.reportService.getAuthorizedVehicles(1).subscribe((res: HttpResponse<any[]>) => (this.report = res.body || []));
        break;
    }
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }
}
