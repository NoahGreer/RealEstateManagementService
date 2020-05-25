import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { RealEstateManagementServiceSharedModule } from 'app/shared/shared.module';

import { ReportComponent } from './report.component';
import { reportRoute } from './report.route';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'typeof'
})
export class TypeofPipe implements PipeTransform {
  transform(value: any): any {
    console.log('Pipe works ', typeof value);
    return typeof value;
  }
}
@NgModule({
  imports: [RealEstateManagementServiceSharedModule, RouterModule.forChild(reportRoute)],
  declarations: [ReportComponent]
})
export class RealEstateManagementServiceReportModule {}
