import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RealEstateManagementServiceSharedModule } from 'app/shared/shared.module';
import { LeaseComponent } from './lease.component';
import { LeaseDetailComponent } from './lease-detail.component';
import { LeaseUpdateComponent } from './lease-update.component';
import { LeaseDeleteDialogComponent } from './lease-delete-dialog.component';
import { leaseRoute } from './lease.route';

@NgModule({
  imports: [RealEstateManagementServiceSharedModule, RouterModule.forChild(leaseRoute)],
  declarations: [LeaseComponent, LeaseDetailComponent, LeaseUpdateComponent, LeaseDeleteDialogComponent],
  entryComponents: [LeaseDeleteDialogComponent]
})
export class RealEstateManagementServiceLeaseModule {}
