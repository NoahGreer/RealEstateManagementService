import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMaintenance } from 'app/shared/model/maintenance.model';
import { MaintenanceService } from './maintenance.service';

@Component({
  templateUrl: './maintenance-delete-dialog.component.html'
})
export class MaintenanceDeleteDialogComponent {
  maintenance?: IMaintenance;

  constructor(
    protected maintenanceService: MaintenanceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.maintenanceService.delete(id).subscribe(() => {
      this.eventManager.broadcast('maintenanceListModification');
      this.activeModal.close();
    });
  }
}
