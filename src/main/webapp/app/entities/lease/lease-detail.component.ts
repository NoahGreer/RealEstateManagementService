import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILease } from 'app/shared/model/lease.model';

@Component({
  selector: 'jhi-lease-detail',
  templateUrl: './lease-detail.component.html'
})
export class LeaseDetailComponent implements OnInit {
  lease: ILease | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lease }) => (this.lease = lease));
  }

  previousState(): void {
    window.history.back();
  }
}
