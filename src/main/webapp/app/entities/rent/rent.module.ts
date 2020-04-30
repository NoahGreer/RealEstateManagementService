import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RealEstateManagementServiceSharedModule } from 'app/shared/shared.module';
import { RentComponent } from './rent.component';
import { RentDetailComponent } from './rent-detail.component';
import { RentUpdateComponent } from './rent-update.component';
import { RentDeleteDialogComponent } from './rent-delete-dialog.component';
import { rentRoute } from './rent.route';

@NgModule({
  imports: [RealEstateManagementServiceSharedModule, RouterModule.forChild(rentRoute)],
  declarations: [RentComponent, RentDetailComponent, RentUpdateComponent, RentDeleteDialogComponent],
  entryComponents: [RentDeleteDialogComponent]
})
export class RealEstateManagementServiceRentModule {}
