import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IVehicle } from 'app/shared/model/vehicle.model';
import { VehicleService } from './vehicle.service';

@Component({
  templateUrl: './vehicle-delete-dialog.component.html'
})
export class VehicleDeleteDialogComponent {
  vehicle?: IVehicle;

  constructor(protected vehicleService: VehicleService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vehicleService.delete(id).subscribe(() => {
      this.eventManager.broadcast('vehicleListModification');
      this.activeModal.close();
    });
  }
}
