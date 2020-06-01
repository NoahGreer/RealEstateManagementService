import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormArray } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { FormBuilder, Validators } from '@angular/forms';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import * as moment from 'moment';
import { ReportService } from './report.service';

@Component({
  selector: 'jhi-report',
  templateUrl: './report.landing.component.html'
})
export class ReportLandingComponent implements OnInit, OnDestroy {
  report?: any[];
  data?: Object;
  objectType?: string;
  eventSubscriber?: Subscription;
  reportValue?: string;
  reportTypes: { [key: string]: { route: string; paramType: [string] } };
  parameters: string;

  editForm = this.fb.group({
    reportSelect: [],
    inputBox: []
  });

  constructor(
    protected reportService: ReportService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.reportTypes = {
      ['Rents Paid']: { route: '/report/rents-paid', paramType: ['date'] },
      ['Available Apartments']: { route: '/report/available-apartments', paramType: [''] },
      ['Authorized Vehicles']: { route: '/report/authorized-vehicles', paramType: ['number'] },
      ['Current Tenant Contacts']: { route: '/report/contacts', paramType: [''] },
      ['Tenant Email List']: { route: '/report/emails', paramType: [''] },
      ['Recent Infractions']: { route: '/report/infractions', paramType: [''] },
      ['Tenants By Apartment']: { route: '/report/tenants-by-apartment', paramType: ['number'] },
      ['Next Expiring Leases']: { route: '/report/leases-by-expiration', paramType: ['number'] },
      ['Pets/Pet Owners']: { route: '/report/pet-owners', paramType: [''] },
      ['Open Maintenance']: { route: '/report/open-maintenance', paramType: [''] },
      ['Maintenance By Apartment']: { route: '/report/maintenance-by-apartment', paramType: ['number'] },
      ['Contractor By Job-Type']: { route: '/report/contractor-by-jobtype', paramType: ['number'] }
    };
  }

  onOptionsSelected(value: string): void {
    this.reportValue = value;
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
