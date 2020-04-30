import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IJobType } from 'app/shared/model/job-type.model';
import { JobTypeService } from './job-type.service';

@Component({
  templateUrl: './job-type-delete-dialog.component.html'
})
export class JobTypeDeleteDialogComponent {
  jobType?: IJobType;

  constructor(protected jobTypeService: JobTypeService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.jobTypeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('jobTypeListModification');
      this.activeModal.close();
    });
  }
}
