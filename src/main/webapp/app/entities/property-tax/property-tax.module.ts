import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RealEstateManagementServiceSharedModule } from 'app/shared/shared.module';
import { PropertyTaxComponent } from './property-tax.component';
import { PropertyTaxDetailComponent } from './property-tax-detail.component';
import { PropertyTaxUpdateComponent } from './property-tax-update.component';
import { PropertyTaxDeleteDialogComponent } from './property-tax-delete-dialog.component';
import { propertyTaxRoute } from './property-tax.route';

@NgModule({
  imports: [RealEstateManagementServiceSharedModule, RouterModule.forChild(propertyTaxRoute)],
  declarations: [PropertyTaxComponent, PropertyTaxDetailComponent, PropertyTaxUpdateComponent, PropertyTaxDeleteDialogComponent],
  entryComponents: [PropertyTaxDeleteDialogComponent]
})
export class RealEstateManagementServicePropertyTaxModule {}
