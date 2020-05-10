import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRent } from 'app/shared/model/rent.model';
import { RentService } from './rent.service';

@Component({
  templateUrl: './rent-delete-dialog.component.html'
})
export class RentDeleteDialogComponent {
  rent?: IRent;

  constructor(protected rentService: RentService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rentService.delete(id).subscribe(() => {
      this.eventManager.broadcast('rentListModification');
      this.activeModal.close();
    });
  }
}
