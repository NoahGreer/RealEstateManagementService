import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RealEstateManagementServiceSharedModule } from 'app/shared/shared.module';
import { PetComponent } from './pet.component';
import { PetDetailComponent } from './pet-detail.component';
import { PetUpdateComponent } from './pet-update.component';
import { PetDeleteDialogComponent } from './pet-delete-dialog.component';
import { petRoute } from './pet.route';

@NgModule({
  imports: [RealEstateManagementServiceSharedModule, RouterModule.forChild(petRoute)],
  declarations: [PetComponent, PetDetailComponent, PetUpdateComponent, PetDeleteDialogComponent],
  entryComponents: [PetDeleteDialogComponent]
})
export class RealEstateManagementServicePetModule {}
