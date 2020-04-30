import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ILease, Lease } from 'app/shared/model/lease.model';
import { LeaseService } from './lease.service';
import { IPerson } from 'app/shared/model/person.model';
import { PersonService } from 'app/entities/person/person.service';
import { IVehicle } from 'app/shared/model/vehicle.model';
import { VehicleService } from 'app/entities/vehicle/vehicle.service';
import { IPet } from 'app/shared/model/pet.model';
import { PetService } from 'app/entities/pet/pet.service';
import { IApartment } from 'app/shared/model/apartment.model';
import { ApartmentService } from 'app/entities/apartment/apartment.service';

type SelectableEntity = ILease | IPerson | IVehicle | IPet | IApartment;

type SelectableManyToManyEntity = IPerson | IVehicle | IPet;

@Component({
  selector: 'jhi-lease-update',
  templateUrl: './lease-update.component.html'
})
export class LeaseUpdateComponent implements OnInit {
  isSaving = false;
  leases: ILease[] = [];
  people: IPerson[] = [];
  vehicles: IVehicle[] = [];
  pets: IPet[] = [];
  apartments: IApartment[] = [];
  dateSignedDp: any;
  moveInDateDp: any;
  noticeOfRemovalOrMoveoutDateDp: any;
  endDateDp: any;

  editForm = this.fb.group({
    id: [],
    dateSigned: [],
    moveInDate: [],
    noticeOfRemovalOrMoveoutDate: [],
    endDate: [],
    amount: [],
    leaseType: [],
    notes: [],
    newLeaseId: [],
    people: [],
    vehicles: [],
    pets: [],
    apartmentId: []
  });

  constructor(
    protected leaseService: LeaseService,
    protected personService: PersonService,
    protected vehicleService: VehicleService,
    protected petService: PetService,
    protected apartmentService: ApartmentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lease }) => {
      this.updateForm(lease);

      this.leaseService.query().subscribe((res: HttpResponse<ILease[]>) => (this.leases = res.body || []));

      this.personService.query().subscribe((res: HttpResponse<IPerson[]>) => (this.people = res.body || []));

      this.vehicleService.query().subscribe((res: HttpResponse<IVehicle[]>) => (this.vehicles = res.body || []));

      this.petService.query().subscribe((res: HttpResponse<IPet[]>) => (this.pets = res.body || []));

      this.apartmentService.query().subscribe((res: HttpResponse<IApartment[]>) => (this.apartments = res.body || []));
    });
  }

  updateForm(lease: ILease): void {
    this.editForm.patchValue({
      id: lease.id,
      dateSigned: lease.dateSigned,
      moveInDate: lease.moveInDate,
      noticeOfRemovalOrMoveoutDate: lease.noticeOfRemovalOrMoveoutDate,
      endDate: lease.endDate,
      amount: lease.amount,
      leaseType: lease.leaseType,
      notes: lease.notes,
      newLeaseId: lease.newLeaseId,
      people: lease.people,
      vehicles: lease.vehicles,
      pets: lease.pets,
      apartmentId: lease.apartmentId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lease = this.createFromForm();
    if (lease.id !== undefined) {
      this.subscribeToSaveResponse(this.leaseService.update(lease));
    } else {
      this.subscribeToSaveResponse(this.leaseService.create(lease));
    }
  }

  private createFromForm(): ILease {
    return {
      ...new Lease(),
      id: this.editForm.get(['id'])!.value,
      dateSigned: this.editForm.get(['dateSigned'])!.value,
      moveInDate: this.editForm.get(['moveInDate'])!.value,
      noticeOfRemovalOrMoveoutDate: this.editForm.get(['noticeOfRemovalOrMoveoutDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      leaseType: this.editForm.get(['leaseType'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      newLeaseId: this.editForm.get(['newLeaseId'])!.value,
      people: this.editForm.get(['people'])!.value,
      vehicles: this.editForm.get(['vehicles'])!.value,
      pets: this.editForm.get(['pets'])!.value,
      apartmentId: this.editForm.get(['apartmentId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILease>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  getSelected(selectedVals: SelectableManyToManyEntity[], option: SelectableManyToManyEntity): SelectableManyToManyEntity {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
