import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RealEstateManagementServiceSharedModule } from 'app/shared/shared.module';
import { JobTypeComponent } from './job-type.component';
import { JobTypeDetailComponent } from './job-type-detail.component';
import { JobTypeUpdateComponent } from './job-type-update.component';
import { JobTypeDeleteDialogComponent } from './job-type-delete-dialog.component';
import { jobTypeRoute } from './job-type.route';

@NgModule({
  imports: [RealEstateManagementServiceSharedModule, RouterModule.forChild(jobTypeRoute)],
  declarations: [JobTypeComponent, JobTypeDetailComponent, JobTypeUpdateComponent, JobTypeDeleteDialogComponent],
  entryComponents: [JobTypeDeleteDialogComponent]
})
export class RealEstateManagementServiceJobTypeModule {}
