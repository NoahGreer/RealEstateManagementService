import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IRent, Rent } from 'app/shared/model/rent.model';
import { RentService } from './rent.service';
import { ILease } from 'app/shared/model/lease.model';
import { LeaseService } from 'app/entities/lease/lease.service';

@Component({
  selector: 'jhi-rent-update',
  templateUrl: './rent-update.component.html'
})
export class RentUpdateComponent implements OnInit {
  isSaving = false;
  leases: ILease[] = [];
  dueDateDp: any;
  recievedDateDp: any;

  editForm = this.fb.group({
    id: [],
    dueDate: [],
    recievedDate: [],
    amount: [],
    leaseId: []
  });

  constructor(
    protected rentService: RentService,
    protected leaseService: LeaseService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rent }) => {
      this.updateForm(rent);

      this.leaseService.query().subscribe((res: HttpResponse<ILease[]>) => (this.leases = res.body || []));
    });
  }

  updateForm(rent: IRent): void {
    this.editForm.patchValue({
      id: rent.id,
      dueDate: rent.dueDate,
      recievedDate: rent.recievedDate,
      amount: rent.amount,
      leaseId: rent.leaseId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rent = this.createFromForm();
    if (rent.id !== undefined) {
      this.subscribeToSaveResponse(this.rentService.update(rent));
    } else {
      this.subscribeToSaveResponse(this.rentService.create(rent));
    }
  }

  private createFromForm(): IRent {
    return {
      ...new Rent(),
      id: this.editForm.get(['id'])!.value,
      dueDate: this.editForm.get(['dueDate'])!.value,
      recievedDate: this.editForm.get(['recievedDate'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      leaseId: this.editForm.get(['leaseId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRent>>): void {
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

  trackById(index: number, item: ILease): any {
    return item.id;
  }
}
