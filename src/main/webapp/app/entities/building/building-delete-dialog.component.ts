import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBuilding } from 'app/shared/model/building.model';
import { BuildingService } from './building.service';

@Component({
  templateUrl: './building-delete-dialog.component.html'
})
export class BuildingDeleteDialogComponent {
  building?: IBuilding;

  constructor(protected buildingService: BuildingService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.buildingService.delete(id).subscribe(() => {
      this.eventManager.broadcast('buildingListModification');
      this.activeModal.close();
    });
  }
}
