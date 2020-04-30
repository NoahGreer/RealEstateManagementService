import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IApartment, Apartment } from 'app/shared/model/apartment.model';
import { ApartmentService } from './apartment.service';
import { IBuilding } from 'app/shared/model/building.model';
import { BuildingService } from 'app/entities/building/building.service';

@Component({
  selector: 'jhi-apartment-update',
  templateUrl: './apartment-update.component.html'
})
export class ApartmentUpdateComponent implements OnInit {
  isSaving = false;
  buildings: IBuilding[] = [];

  editForm = this.fb.group({
    id: [],
    unitNumber: [],
    moveInReady: [],
    buildingId: []
  });

  constructor(
    protected apartmentService: ApartmentService,
    protected buildingService: BuildingService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ apartment }) => {
      this.updateForm(apartment);

      this.buildingService.query().subscribe((res: HttpResponse<IBuilding[]>) => (this.buildings = res.body || []));
    });
  }

  updateForm(apartment: IApartment): void {
    this.editForm.patchValue({
      id: apartment.id,
      unitNumber: apartment.unitNumber,
      moveInReady: apartment.moveInReady,
      buildingId: apartment.buildingId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const apartment = this.createFromForm();
    if (apartment.id !== undefined) {
      this.subscribeToSaveResponse(this.apartmentService.update(apartment));
    } else {
      this.subscribeToSaveResponse(this.apartmentService.create(apartment));
    }
  }

  private createFromForm(): IApartment {
    return {
      ...new Apartment(),
      id: this.editForm.get(['id'])!.value,
      unitNumber: this.editForm.get(['unitNumber'])!.value,
      moveInReady: this.editForm.get(['moveInReady'])!.value,
      buildingId: this.editForm.get(['buildingId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApartment>>): void {
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
