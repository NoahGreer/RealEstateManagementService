import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IMaintenance, Maintenance } from 'app/shared/model/maintenance.model';
import { MaintenanceService } from './maintenance.service';
import { IApartment } from 'app/shared/model/apartment.model';
import { ApartmentService } from 'app/entities/apartment/apartment.service';
import { IContractor } from 'app/shared/model/contractor.model';
import { ContractorService } from 'app/entities/contractor/contractor.service';

type SelectableEntity = IApartment | IContractor;

@Component({
  selector: 'jhi-maintenance-update',
  templateUrl: './maintenance-update.component.html'
})
export class MaintenanceUpdateComponent implements OnInit {
  isSaving = false;
  apartments: IApartment[] = [];
  contractors: IContractor[] = [];
  notificationDateDp: any;
  dateCompleteDp: any;
  repairPaidOnDp: any;

  editForm = this.fb.group({
    id: [],
    description: [],
    notificationDate: [],
    dateComplete: [],
    contractorJobNumber: [],
    repairCost: [],
    repairPaidOn: [],
    receiptOfPayment: [],
    apartmentId: [],
    contractorId: []
  });

  constructor(
    protected maintenanceService: MaintenanceService,
    protected apartmentService: ApartmentService,
    protected contractorService: ContractorService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ maintenance }) => {
      this.updateForm(maintenance);

      this.apartmentService.query().subscribe((res: HttpResponse<IApartment[]>) => (this.apartments = res.body || []));

      this.contractorService.query().subscribe((res: HttpResponse<IContractor[]>) => (this.contractors = res.body || []));
    });
  }

  updateForm(maintenance: IMaintenance): void {
    this.editForm.patchValue({
      id: maintenance.id,
      description: maintenance.description,
      notificationDate: maintenance.notificationDate,
      dateComplete: maintenance.dateComplete,
      contractorJobNumber: maintenance.contractorJobNumber,
      repairCost: maintenance.repairCost,
      repairPaidOn: maintenance.repairPaidOn,
      receiptOfPayment: maintenance.receiptOfPayment,
      apartmentId: maintenance.apartmentId,
      contractorId: maintenance.contractorId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const maintenance = this.createFromForm();
    if (maintenance.id !== undefined) {
      this.subscribeToSaveResponse(this.maintenanceService.update(maintenance));
    } else {
      this.subscribeToSaveResponse(this.maintenanceService.create(maintenance));
    }
  }

  private createFromForm(): IMaintenance {
    return {
      ...new Maintenance(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      notificationDate: this.editForm.get(['notificationDate'])!.value,
      dateComplete: this.editForm.get(['dateComplete'])!.value,
      contractorJobNumber: this.editForm.get(['contractorJobNumber'])!.value,
      repairCost: this.editForm.get(['repairCost'])!.value,
      repairPaidOn: this.editForm.get(['repairPaidOn'])!.value,
      receiptOfPayment: this.editForm.get(['receiptOfPayment'])!.value,
      apartmentId: this.editForm.get(['apartmentId'])!.value,
      contractorId: this.editForm.get(['contractorId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMaintenance>>): void {
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
}
