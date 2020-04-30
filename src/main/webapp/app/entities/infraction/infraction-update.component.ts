import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IInfraction, Infraction } from 'app/shared/model/infraction.model';
import { InfractionService } from './infraction.service';
import { ILease } from 'app/shared/model/lease.model';
import { LeaseService } from 'app/entities/lease/lease.service';

@Component({
  selector: 'jhi-infraction-update',
  templateUrl: './infraction-update.component.html'
})
export class InfractionUpdateComponent implements OnInit {
  isSaving = false;
  leases: ILease[] = [];
  dateOccurredDp: any;

  editForm = this.fb.group({
    id: [],
    dateOccurred: [],
    cause: [],
    resolution: [],
    leaseId: []
  });

  constructor(
    protected infractionService: InfractionService,
    protected leaseService: LeaseService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ infraction }) => {
      this.updateForm(infraction);

      this.leaseService.query().subscribe((res: HttpResponse<ILease[]>) => (this.leases = res.body || []));
    });
  }

  updateForm(infraction: IInfraction): void {
    this.editForm.patchValue({
      id: infraction.id,
      dateOccurred: infraction.dateOccurred,
      cause: infraction.cause,
      resolution: infraction.resolution,
      leaseId: infraction.leaseId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const infraction = this.createFromForm();
    if (infraction.id !== undefined) {
      this.subscribeToSaveResponse(this.infractionService.update(infraction));
    } else {
      this.subscribeToSaveResponse(this.infractionService.create(infraction));
    }
  }

  private createFromForm(): IInfraction {
    return {
      ...new Infraction(),
      id: this.editForm.get(['id'])!.value,
      dateOccurred: this.editForm.get(['dateOccurred'])!.value,
      cause: this.editForm.get(['cause'])!.value,
      resolution: this.editForm.get(['resolution'])!.value,
      leaseId: this.editForm.get(['leaseId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInfraction>>): void {
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
