import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IContractor } from 'app/shared/model/contractor.model';
import { ContractorService } from './contractor.service';

@Component({
  templateUrl: './contractor-delete-dialog.component.html'
})
export class ContractorDeleteDialogComponent {
  contractor?: IContractor;

  constructor(
    protected contractorService: ContractorService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contractorService.delete(id).subscribe(() => {
      this.eventManager.broadcast('contractorListModification');
      this.activeModal.close();
    });
  }
}
