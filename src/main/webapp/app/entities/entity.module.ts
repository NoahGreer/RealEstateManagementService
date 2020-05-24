import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'building',
        loadChildren: () => import('./building/building.module').then(m => m.RealEstateManagementServiceBuildingModule)
      },
      {
        path: 'property-tax',
        loadChildren: () => import('./property-tax/property-tax.module').then(m => m.RealEstateManagementServicePropertyTaxModule)
      },
      {
        path: 'apartment',
        loadChildren: () => import('./apartment/apartment.module').then(m => m.RealEstateManagementServiceApartmentModule)
      },
      {
        path: 'maintenance',
        loadChildren: () => import('./maintenance/maintenance.module').then(m => m.RealEstateManagementServiceMaintenanceModule)
      },
      {
        path: 'contractor',
        loadChildren: () => import('./contractor/contractor.module').then(m => m.RealEstateManagementServiceContractorModule)
      },
      {
        path: 'job-type',
        loadChildren: () => import('./job-type/job-type.module').then(m => m.RealEstateManagementServiceJobTypeModule)
      },
      {
        path: 'lease',
        loadChildren: () => import('./lease/lease.module').then(m => m.RealEstateManagementServiceLeaseModule)
      },
      {
        path: 'person',
        loadChildren: () => import('./person/person.module').then(m => m.RealEstateManagementServicePersonModule)
      },
      {
        path: 'vehicle',
        loadChildren: () => import('./vehicle/vehicle.module').then(m => m.RealEstateManagementServiceVehicleModule)
      },
      {
        path: 'rent',
        loadChildren: () => import('./rent/rent.module').then(m => m.RealEstateManagementServiceRentModule)
      },
      {
        path: 'pet',
        loadChildren: () => import('./pet/pet.module').then(m => m.RealEstateManagementServicePetModule)
      },
      {
        path: 'infraction',
        loadChildren: () => import('./infraction/infraction.module').then(m => m.RealEstateManagementServiceInfractionModule)
      },
      {
        path: 'report',
        loadChildren: () => import('./report/report.module').then(m => m.RealEstateManagementServiceReportModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class RealEstateManagementServiceEntityModule {}
