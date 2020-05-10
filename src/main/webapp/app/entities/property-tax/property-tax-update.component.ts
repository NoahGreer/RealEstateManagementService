import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPropertyTax, PropertyTax } from 'app/shared/model/property-tax.model';
import { PropertyTaxService } from './property-tax.service';
import { IBuilding } from 'app/shared/model/building.model';
import { BuildingService } from 'app/entities/building/building.service';

@Component({
  selector: 'jhi-property-tax-update',
  templateUrl: './property-tax-update.component.html'
})
export class PropertyTaxUpdateComponent implements OnInit {
  isSaving = false;
  buildings: IBuilding[] = [];
  datePaidDp: any;

  editForm = this.fb.group({
    id: [],
    taxYear: [null, [Validators.min(0)]],
    amount: [],
    datePaid: [],
    confirmationNumber: [],
    buildingId: []
  });

  constructor(
    protected propertyTaxService: PropertyTaxService,
    protected buildingService: BuildingService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ propertyTax }) => {
      this.updateForm(propertyTax);

      this.buildingService.query().subscribe((res: HttpResponse<IBuilding[]>) => (this.buildings = res.body || []));
    });
  }

  updateForm(propertyTax: IPropertyTax): void {
    this.editForm.patchValue({
      id: propertyTax.id,
      taxYear: propertyTax.taxYear,
      amount: propertyTax.amount,
      datePaid: propertyTax.datePaid,
      confirmationNumber: propertyTax.confirmationNumber,
      buildingId: propertyTax.buildingId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const propertyTax = this.createFromForm();
    if (propertyTax.id !== undefined) {
      this.subscribeToSaveResponse(this.propertyTaxService.update(propertyTax));
    } else {
      this.subscribeToSaveResponse(this.propertyTaxService.create(propertyTax));
    }
  }

  private createFromForm(): IPropertyTax {
    return {
      ...new PropertyTax(),
      id: this.editForm.get(['id'])!.value,
      taxYear: this.editForm.get(['taxYear'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      datePaid: this.editForm.get(['datePaid'])!.value,
      confirmationNumber: this.editForm.get(['confirmationNumber'])!.value,
      buildingId: this.editForm.get(['buildingId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPropertyTax>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IBuilding): any {
    return item.id;
  }
}
