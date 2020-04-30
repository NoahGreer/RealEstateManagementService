import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPet, Pet } from 'app/shared/model/pet.model';
import { PetService } from './pet.service';

@Component({
  selector: 'jhi-pet-update',
  templateUrl: './pet-update.component.html'
})
export class PetUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    type: [],
    color: [],
    certifiedAssistanceAnimal: []
  });

  constructor(protected petService: PetService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pet }) => {
      this.updateForm(pet);
    });
  }

  updateForm(pet: IPet): void {
    this.editForm.patchValue({
      id: pet.id,
      name: pet.name,
      type: pet.type,
      color: pet.color,
      certifiedAssistanceAnimal: pet.certifiedAssistanceAnimal
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pet = this.createFromForm();
    if (pet.id !== undefined) {
      this.subscribeToSaveResponse(this.petService.update(pet));
    } else {
      this.subscribeToSaveResponse(this.petService.create(pet));
    }
  }

  private createFromForm(): IPet {
    return {
      ...new Pet(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      type: this.editForm.get(['type'])!.value,
      color: this.editForm.get(['color'])!.value,
      certifiedAssistanceAnimal: this.editForm.get(['certifiedAssistanceAnimal'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPet>>): void {
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
