import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RealEstateManagementServiceSharedModule } from 'app/shared/shared.module';
import { MaintenanceComponent } from './maintenance.component';
import { MaintenanceDetailComponent } from './maintenance-detail.component';
import { MaintenanceUpdateComponent } from './maintenance-update.component';
import { MaintenanceDeleteDialogComponent } from './maintenance-delete-dialog.component';
import { maintenanceRoute } from './maintenance.route';

@NgModule({
  imports: [RealEstateManagementServiceSharedModule, RouterModule.forChild(maintenanceRoute)],
  declarations: [MaintenanceComponent, MaintenanceDetailComponent, MaintenanceUpdateComponent, MaintenanceDeleteDialogComponent],
  entryComponents: [MaintenanceDeleteDialogComponent]
})
export class RealEstateManagementServiceMaintenanceModule {}
