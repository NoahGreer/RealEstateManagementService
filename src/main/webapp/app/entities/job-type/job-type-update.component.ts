import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IJobType, JobType } from 'app/shared/model/job-type.model';
import { JobTypeService } from './job-type.service';

@Component({
  selector: 'jhi-job-type-update',
  templateUrl: './job-type-update.component.html'
})
export class JobTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: []
  });

  constructor(protected jobTypeService: JobTypeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jobType }) => {
      this.updateForm(jobType);
    });
  }

  updateForm(jobType: IJobType): void {
    this.editForm.patchValue({
      id: jobType.id,
      name: jobType.name
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const jobType = this.createFromForm();
    if (jobType.id !== undefined) {
      this.subscribeToSaveResponse(this.jobTypeService.update(jobType));
    } else {
      this.subscribeToSaveResponse(this.jobTypeService.create(jobType));
    }
  }

  private createFromForm(): IJobType {
    return {
      ...new JobType(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJobType>>): void {
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
