import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IBuilding, Building } from 'app/shared/model/building.model';
import { BuildingService } from './building.service';

@Component({
  selector: 'jhi-building-update',
  templateUrl: './building-update.component.html'
})
export class BuildingUpdateComponent implements OnInit {
  isSaving = false;
  purchaseDateDp: any;

  editForm = this.fb.group({
    id: [],
    name: [],
    purchaseDate: [],
    propertyNumber: [],
    streetAddress: [],
    city: [],
    stateCode: [null, [Validators.minLength(2), Validators.maxLength(2)]],
    zipCode: [null, [Validators.maxLength(10)]]
  });

  constructor(protected buildingService: BuildingService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ building }) => {
      this.updateForm(building);
    });
  }

  updateForm(building: IBuilding): void {
    this.editForm.patchValue({
      id: building.id,
      name: building.name,
      purchaseDate: building.purchaseDate,
      propertyNumber: building.propertyNumber,
      streetAddress: building.streetAddress,
      city: building.city,
      stateCode: building.stateCode,
      zipCode: building.zipCode
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const building = this.createFromForm();
    if (building.id !== undefined) {
      this.subscribeToSaveResponse(this.buildingService.update(building));
    } else {
      this.subscribeToSaveResponse(this.buildingService.create(building));
    }
  }

  private createFromForm(): IBuilding {
    return {
      ...new Building(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      purchaseDate: this.editForm.get(['purchaseDate'])!.value,
      propertyNumber: this.editForm.get(['propertyNumber'])!.value,
      streetAddress: this.editForm.get(['streetAddress'])!.value,
      city: this.editForm.get(['city'])!.value,
      stateCode: this.editForm.get(['stateCode'])!.value,
      zipCode: this.editForm.get(['zipCode'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBuilding>>): void {
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
}
