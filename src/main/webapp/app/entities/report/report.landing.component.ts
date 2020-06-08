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
  reportTypes: { [key: string]: { route: string; paramType: string } };
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
    // if (!this.displayField) {this.displayField = '';}
    // if (!this.reportValue) {this.reportValue = '';}
    // if (!this.objectType) {this.objectType = '';}
    // if (!this.entityPopulation) {this.entityPopulation = null;}

    this.displayField = '';
    this.reportValue = '';
    this.entityPopulation = null;
    this.reportTypes = {
      'Rents Paid': { route: '/report/rents-paid', paramType: 'date' },
      'Available Apartments': { route: '/report/available-apartments', paramType: '' },
      'Authorized Vehicles by Building': { route: '/report/vehicles-by-building', paramType: 'building' },
      'Authorized Vehicles by Apartment': { route: '/report/vehicles-by-apartment', paramType: 'apartment' },
      'Current Tenant Contacts': { route: '/report/contacts', paramType: '' },
      'Tenant Email List': { route: '/report/emails', paramType: '' },
      'Rent Delinquencies': { route: '/report/rent-delinquencies', paramType: 'date' },
      'Infractions By Year': { route: '/report/infractions-by-year', paramType: 'date' },
      'Infractions By Apartment': { route: '/report/infractions-by-apartment', paramType: 'apartment' },
      'Tenants By Apartment': { route: '/report/tenants-by-apartment', paramType: 'apartment' },
      'Next Expiring Leases': { route: '/report/leases-by-expiration', paramType: 'number' },
      'Pets/Pet Owners': { route: '/report/pet-owners', paramType: '' },
      'Tax History': { route: '/report/tax-history', paramType: '' },
      'Open Maintenance': { route: '/report/open-maintenance', paramType: '' },
      'Maintenance By Contractor': { route: '/report/maintenance-by-contractor', paramType: 'contractor' },
      'Contractor By Job-Type': { route: '/report/contractor-by-jobtype', paramType: 'jobtype' }
    };
  }

  ngOnInit(): void {}

  onOptionsSelected(value: string): void {
    this.reportValue = value;
    switch (this.reportTypes[this.reportValue].paramType) {
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
      case 'date':
        this.editForm.controls['inputBox'].setValue(this.reportService.getTodaysDate());
        break;
      default:
    }
    this.entityPopulation = null;
  }

  formValidate(): void {
    if (this.reportValue) {
      if (this.reportTypes[this.reportValue].paramType === '') {
        this.submitReportQuery();
      }
      const currentParamInput = this.editForm.get(['inputBox'])!.value;
      if (currentParamInput) {
        if (this.reportTypes[this.reportValue].paramType === 'number') {
          if (currentParamInput >= 1) {
            this.submitReportQuery();
          }
        } else {
          this.submitReportQuery();
        }
      }
    }
  }

  submitReportQuery(): void {
    this.router.navigate([this.reportTypes[this.reportValue].route], {
      queryParams: { reportType: this.reportValue, passedParam: this.editForm.get(['inputBox'])!.value }
    });
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }
}
