import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IContractor, Contractor } from 'app/shared/model/contractor.model';
import { ContractorService } from './contractor.service';
import { IJobType } from 'app/shared/model/job-type.model';
import { JobTypeService } from 'app/entities/job-type/job-type.service';

@Component({
  selector: 'jhi-contractor-update',
  templateUrl: './contractor-update.component.html'
})
export class ContractorUpdateComponent implements OnInit {
  isSaving = false;
  jobtypes: IJobType[] = [];

  editForm = this.fb.group({
    id: [],
    companyName: [],
    streetAddress: [],
    city: [],
    stateCode: [null, [Validators.minLength(2), Validators.maxLength(2)]],
    zipCode: [null, [Validators.maxLength(10)]],
    phoneNumber: [],
    contactPerson: [],
    ratingOfWork: [null, [Validators.min(0), Validators.max(5)]],
    ratingOfResponsiveness: [null, [Validators.min(0), Validators.max(5)]],
    jobTypes: []
  });

  constructor(
    protected contractorService: ContractorService,
    protected jobTypeService: JobTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contractor }) => {
      this.updateForm(contractor);

      this.jobTypeService.query().subscribe((res: HttpResponse<IJobType[]>) => (this.jobtypes = res.body || []));
    });
  }

  updateForm(contractor: IContractor): void {
    this.editForm.patchValue({
      id: contractor.id,
      companyName: contractor.companyName,
      streetAddress: contractor.streetAddress,
      city: contractor.city,
      stateCode: contractor.stateCode,
      zipCode: contractor.zipCode,
      phoneNumber: contractor.phoneNumber,
      contactPerson: contractor.contactPerson,
      ratingOfWork: contractor.ratingOfWork,
      ratingOfResponsiveness: contractor.ratingOfResponsiveness,
      jobTypes: contractor.jobTypes
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contractor = this.createFromForm();
    if (contractor.id !== undefined) {
      this.subscribeToSaveResponse(this.contractorService.update(contractor));
    } else {
      this.subscribeToSaveResponse(this.contractorService.create(contractor));
    }
  }

  private createFromForm(): IContractor {
    return {
      ...new Contractor(),
      id: this.editForm.get(['id'])!.value,
      companyName: this.editForm.get(['companyName'])!.value,
      streetAddress: this.editForm.get(['streetAddress'])!.value,
      city: this.editForm.get(['city'])!.value,
      stateCode: this.editForm.get(['stateCode'])!.value,
      zipCode: this.editForm.get(['zipCode'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      contactPerson: this.editForm.get(['contactPerson'])!.value,
      ratingOfWork: this.editForm.get(['ratingOfWork'])!.value,
      ratingOfResponsiveness: this.editForm.get(['ratingOfResponsiveness'])!.value,
      jobTypes: this.editForm.get(['jobTypes'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContractor>>): void {
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

  trackById(index: number, item: IJobType): any {
    return item.id;
  }

  getSelected(selectedVals: IJobType[], option: IJobType): IJobType {
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
