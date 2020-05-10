import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RealEstateManagementServiceSharedModule } from 'app/shared/shared.module';
import { ApartmentComponent } from './apartment.component';
import { ApartmentDetailComponent } from './apartment-detail.component';
import { ApartmentUpdateComponent } from './apartment-update.component';
import { ApartmentDeleteDialogComponent } from './apartment-delete-dialog.component';
import { apartmentRoute } from './apartment.route';

@NgModule({
  imports: [RealEstateManagementServiceSharedModule, RouterModule.forChild(apartmentRoute)],
  declarations: [ApartmentComponent, ApartmentDetailComponent, ApartmentUpdateComponent, ApartmentDeleteDialogComponent],
  entryComponents: [ApartmentDeleteDialogComponent]
})
export class RealEstateManagementServiceApartmentModule {}
