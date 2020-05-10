import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILease } from 'app/shared/model/lease.model';
import { LeaseService } from './lease.service';

@Component({
  templateUrl: './lease-delete-dialog.component.html'
})
export class LeaseDeleteDialogComponent {
  lease?: ILease;

  constructor(protected leaseService: LeaseService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leaseService.delete(id).subscribe(() => {
      this.eventManager.broadcast('leaseListModification');
      this.activeModal.close();
    });
  }
}
