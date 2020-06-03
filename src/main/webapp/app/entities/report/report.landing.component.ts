import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { FormBuilder } from '@angular/forms';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ReportService } from './report.service';
import { RentService } from '../rent/rent.service';
import { ApartmentService } from '../apartment/apartment.service';
import { JobTypeService } from '../job-type/job-type.service';
import { ContractorService } from '../contractor/contractor.service';
import { BuildingService } from '../building/building.service';

import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-report',
  templateUrl: './report.landing.component.html'
})
export class ReportLandingComponent implements OnInit, OnDestroy {
  objectType: string;
  eventSubscriber?: Subscription;
  reportValue: string;
  reportTypes: { [key: string]: { route: string; paramType: [string] } };
  displayField: string;

  entityPopulation: any[] | null;

  editForm = this.fb.group({
    reportSelect: [],
    inputBox: []
  });

  constructor(
    protected reportService: ReportService,
    protected rentService: RentService,
    protected apartmentService: ApartmentService,
    protected jobTypeService: JobTypeService,
    protected contractorService: ContractorService,
    protected buildingService: BuildingService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected fb: FormBuilder
  ) {
    this.displayField = '';
    this.reportValue = '';
    this.objectType = '';
    this.entityPopulation = null;
    this.reportTypes = {
      ['Rents Paid']: { route: '/report/rents-paid', paramType: ['date'] },
      ['Available Apartments']: { route: '/report/available-apartments', paramType: [''] },
      ['Authorized Vehicles']: { route: '/report/authorized-vehicles', paramType: ['building'] },
      ['Current Tenant Contacts']: { route: '/report/contacts', paramType: [''] },
      ['Tenant Email List']: { route: '/report/emails', paramType: [''] },
      ['Recent Infractions']: { route: '/report/infractions', paramType: [''] },
      ['Tenants By Apartment']: { route: '/report/tenants-by-apartment', paramType: ['apartment'] },
      ['Next Expiring Leases']: { route: '/report/leases-by-expiration', paramType: ['number'] },
      ['Pets/Pet Owners']: { route: '/report/pet-owners', paramType: [''] },
      ['Open Maintenance']: { route: '/report/open-maintenance', paramType: [''] },
      ['Maintenance By Contractor']: { route: '/report/maintenance-by-contractor', paramType: ['contractor'] },
      ['Contractor By Job-Type']: { route: '/report/contractor-by-jobtype', paramType: ['jobtype'] }
    };
  }

  ngOnInit(): void {}

  onOptionsSelected(value: string): void {
    this.reportValue = value;
    let i = 0;

    while (i < this.reportTypes[this.reportValue].paramType.length) {
      switch (this.reportTypes[this.reportValue].paramType[i]) {
        case 'apartment':
          this.displayField = 'unitNumber';
          this.apartmentService.query({}).subscribe((res: HttpResponse<any[]>) => (this.entityPopulation = res.body));
          break;
        case 'jobtype':
          this.displayField = 'name';
          this.jobTypeService.query({}).subscribe((res: HttpResponse<any[]>) => (this.entityPopulation = res.body));
          break;
        case 'contractor':
          this.displayField = 'companyName';
          this.contractorService.query({}).subscribe((res: HttpResponse<any[]>) => (this.entityPopulation = res.body));
          break;
        case 'building':
          this.displayField = 'name';
          this.buildingService.query({}).subscribe((res: HttpResponse<any[]>) => (this.entityPopulation = res.body));
          break;
        default:
      }
      i = i + 1;
    }
    this.entityPopulation = null;
  }

  submit(): void {
    this.reportService.reportType = this.reportValue;
    this.reportService.passedParamValue = this.editForm.get(['inputBox'])!.value;

    this.router.navigate([this.reportTypes[this.reportValue].route]);
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }
}
