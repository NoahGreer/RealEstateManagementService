import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPerson, Person } from 'app/shared/model/person.model';
import { PersonService } from './person.service';

@Component({
  selector: 'jhi-person-update',
  templateUrl: './person-update.component.html'
})
export class PersonUpdateComponent implements OnInit {
  isSaving = false;
  backgroundCheckDateDp: any;
  employmentVerificationDateDp: any;

  editForm = this.fb.group({
    id: [],
    firstName: [],
    lastName: [],
    phoneNumber: [],
    emailAddress: [],
    primaryContact: [],
    isMinor: [],
    ssn: [],
    backgroundCheckDate: [],
    backgroundCheckConfirmationNumber: [],
    employmentVerificationDate: [],
    employmentVerificationConfirmationNumber: []
  });

  constructor(protected personService: PersonService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ person }) => {
      this.updateForm(person);
    });
  }

  updateForm(person: IPerson): void {
    this.editForm.patchValue({
      id: person.id,
      firstName: person.firstName,
      lastName: person.lastName,
      phoneNumber: person.phoneNumber,
      emailAddress: person.emailAddress,
      primaryContact: person.primaryContact,
      isMinor: person.isMinor,
      ssn: person.ssn,
      backgroundCheckDate: person.backgroundCheckDate,
      backgroundCheckConfirmationNumber: person.backgroundCheckConfirmationNumber,
      employmentVerificationDate: person.employmentVerificationDate,
      employmentVerificationConfirmationNumber: person.employmentVerificationConfirmationNumber
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const person = this.createFromForm();
    if (person.id !== undefined) {
      this.subscribeToSaveResponse(this.personService.update(person));
    } else {
      this.subscribeToSaveResponse(this.personService.create(person));
    }
  }

  private createFromForm(): IPerson {
    return {
      ...new Person(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      emailAddress: this.editForm.get(['emailAddress'])!.value,
      primaryContact: this.editForm.get(['primaryContact'])!.value,
      isMinor: this.editForm.get(['isMinor'])!.value,
      ssn: this.editForm.get(['ssn'])!.value,
      backgroundCheckDate: this.editForm.get(['backgroundCheckDate'])!.value,
      backgroundCheckConfirmationNumber: this.editForm.get(['backgroundCheckConfirmationNumber'])!.value,
      employmentVerificationDate: this.editForm.get(['employmentVerificationDate'])!.value,
      employmentVerificationConfirmationNumber: this.editForm.get(['employmentVerificationConfirmationNumber'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerson>>): void {
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
