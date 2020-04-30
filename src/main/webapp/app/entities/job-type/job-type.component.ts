import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IJobType } from 'app/shared/model/job-type.model';
import { JobTypeService } from './job-type.service';
import { JobTypeDeleteDialogComponent } from './job-type-delete-dialog.component';

@Component({
  selector: 'jhi-job-type',
  templateUrl: './job-type.component.html'
})
export class JobTypeComponent implements OnInit, OnDestroy {
  jobTypes?: IJobType[];
  eventSubscriber?: Subscription;

  constructor(protected jobTypeService: JobTypeService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.jobTypeService.query().subscribe((res: HttpResponse<IJobType[]>) => (this.jobTypes = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInJobTypes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IJobType): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInJobTypes(): void {
    this.eventSubscriber = this.eventManager.subscribe('jobTypeListModification', () => this.loadAll());
  }

  delete(jobType: IJobType): void {
    const modalRef = this.modalService.open(JobTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.jobType = jobType;
  }
}
