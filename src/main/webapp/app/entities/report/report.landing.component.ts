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
      ['Current Contacts']: { route: '/report/contacts', paramType: [''] },
      ['Current Emails']: { route: '/report/emails', paramType: [''] }
    };

    this.updateForm();
  }

  onOptionsSelected(value: string): void {
    console.log(value);
    this.reportValue = value;
  }

  submit(): void {
    if (this.reportValue !== null && this.reportValue !== undefined) {
      this.reportService.reportType = this.reportValue;
      this.reportService.passedParamValue = this.editForm.get(['inputBox'])!.value;
      this.router.navigate([this.reportTypes[this.reportValue].route]);
    }
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  getTodaysDate(): string {
    return moment().format('YYYY-MM-DD');
  }

  updateForm(): void {
    this.editForm.patchValue({
      reportSelect: ['testType'],
      inputBox: ['testParams']
    });
  }

  addSomething(): void {
    console.log(this.fb.array.toString());
  }
}
