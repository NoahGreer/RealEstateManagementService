import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RealEstateManagementServiceSharedModule } from 'app/shared/shared.module';
import { InfractionComponent } from './infraction.component';
import { InfractionDetailComponent } from './infraction-detail.component';
import { InfractionUpdateComponent } from './infraction-update.component';
import { InfractionDeleteDialogComponent } from './infraction-delete-dialog.component';
import { infractionRoute } from './infraction.route';

@NgModule({
  imports: [RealEstateManagementServiceSharedModule, RouterModule.forChild(infractionRoute)],
  declarations: [InfractionComponent, InfractionDetailComponent, InfractionUpdateComponent, InfractionDeleteDialogComponent],
  entryComponents: [InfractionDeleteDialogComponent]
})
export class RealEstateManagementServiceInfractionModule {}
