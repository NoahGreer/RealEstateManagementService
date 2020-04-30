import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInfraction } from 'app/shared/model/infraction.model';
import { InfractionService } from './infraction.service';

@Component({
  templateUrl: './infraction-delete-dialog.component.html'
})
export class InfractionDeleteDialogComponent {
  infraction?: IInfraction;

  constructor(
    protected infractionService: InfractionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.infractionService.delete(id).subscribe(() => {
      this.eventManager.broadcast('infractionListModification');
      this.activeModal.close();
    });
  }
}
