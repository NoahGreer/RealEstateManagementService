import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RealEstateManagementServiceSharedModule } from 'app/shared/shared.module';
import { ReportComponent } from './report.component';

import { reportRoute } from './report.route';

@NgModule({
  imports: [RealEstateManagementServiceSharedModule, RouterModule.forChild(reportRoute)],
  declarations: [ReportComponent]
})
export class RealEstateManagementServiceReportModule {}
