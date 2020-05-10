import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IVehicle, Vehicle } from 'app/shared/model/vehicle.model';
import { VehicleService } from './vehicle.service';

@Component({
  selector: 'jhi-vehicle-update',
  templateUrl: './vehicle-update.component.html'
})
export class VehicleUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    make: [],
    model: [],
    modelYear: [null, [Validators.min(0)]],
    licensePlateNumber: [],
    licensePlateState: [],
    reservedParkingStallNumber: []
  });

  constructor(protected vehicleService: VehicleService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vehicle }) => {
      this.updateForm(vehicle);
    });
  }

  updateForm(vehicle: IVehicle): void {
    this.editForm.patchValue({
      id: vehicle.id,
      make: vehicle.make,
      model: vehicle.model,
      modelYear: vehicle.modelYear,
      licensePlateNumber: vehicle.licensePlateNumber,
      licensePlateState: vehicle.licensePlateState,
      reservedParkingStallNumber: vehicle.reservedParkingStallNumber
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vehicle = this.createFromForm();
    if (vehicle.id !== undefined) {
      this.subscribeToSaveResponse(this.vehicleService.update(vehicle));
    } else {
      this.subscribeToSaveResponse(this.vehicleService.create(vehicle));
    }
  }

  private createFromForm(): IVehicle {
    return {
      ...new Vehicle(),
      id: this.editForm.get(['id'])!.value,
      make: this.editForm.get(['make'])!.value,
      model: this.editForm.get(['model'])!.value,
      modelYear: this.editForm.get(['modelYear'])!.value,
      licensePlateNumber: this.editForm.get(['licensePlateNumber'])!.value,
      licensePlateState: this.editForm.get(['licensePlateState'])!.value,
      reservedParkingStallNumber: this.editForm.get(['reservedParkingStallNumber'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVehicle>>): void {
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
