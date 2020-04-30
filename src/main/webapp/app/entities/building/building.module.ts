import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RealEstateManagementServiceSharedModule } from 'app/shared/shared.module';
import { BuildingComponent } from './building.component';
import { BuildingDetailComponent } from './building-detail.component';
import { BuildingUpdateComponent } from './building-update.component';
import { BuildingDeleteDialogComponent } from './building-delete-dialog.component';
import { buildingRoute } from './building.route';

@NgModule({
  imports: [RealEstateManagementServiceSharedModule, RouterModule.forChild(buildingRoute)],
  declarations: [BuildingComponent, BuildingDetailComponent, BuildingUpdateComponent, BuildingDeleteDialogComponent],
  entryComponents: [BuildingDeleteDialogComponent]
})
export class RealEstateManagementServiceBuildingModule {}
