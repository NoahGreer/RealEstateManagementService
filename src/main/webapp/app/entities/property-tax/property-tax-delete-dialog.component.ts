import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPropertyTax } from 'app/shared/model/property-tax.model';
import { PropertyTaxService } from './property-tax.service';

@Component({
  templateUrl: './property-tax-delete-dialog.component.html'
})
export class PropertyTaxDeleteDialogComponent {
  propertyTax?: IPropertyTax;

  constructor(
    protected propertyTaxService: PropertyTaxService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.propertyTaxService.delete(id).subscribe(() => {
      this.eventManager.broadcast('propertyTaxListModification');
      this.activeModal.close();
    });
  }
}
