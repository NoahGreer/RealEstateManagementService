import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRent } from 'app/shared/model/rent.model';
import { RentService } from './rent.service';

@Component({
  templateUrl: './rent-pay-dialog.component.html'
})
export class RentPayDialogComponent {
  rent?: IRent;

  constructor(protected rentService: RentService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmPayment(rent: IRent): void {
    this.rentService.pay(rent).subscribe(() => {
      this.eventManager.broadcast('rentListModification');
      this.activeModal.close();
    });
  }
}
