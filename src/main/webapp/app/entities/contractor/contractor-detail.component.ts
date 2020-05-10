import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContractor } from 'app/shared/model/contractor.model';

@Component({
  selector: 'jhi-contractor-detail',
  templateUrl: './contractor-detail.component.html'
})
export class ContractorDetailComponent implements OnInit {
  contractor: IContractor | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contractor }) => (this.contractor = contractor));
  }

  previousState(): void {
    window.history.back();
  }
}
