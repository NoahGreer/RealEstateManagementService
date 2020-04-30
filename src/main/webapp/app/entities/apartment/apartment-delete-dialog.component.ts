import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IApartment } from 'app/shared/model/apartment.model';
import { ApartmentService } from './apartment.service';

@Component({
  templateUrl: './apartment-delete-dialog.component.html'
})
export class ApartmentDeleteDialogComponent {
  apartment?: IApartment;

  constructor(protected apartmentService: ApartmentService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.apartmentService.delete(id).subscribe(() => {
      this.eventManager.broadcast('apartmentListModification');
      this.activeModal.close();
    });
  }
}
