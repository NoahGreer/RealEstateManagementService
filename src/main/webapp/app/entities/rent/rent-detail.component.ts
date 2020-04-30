import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRent } from 'app/shared/model/rent.model';

@Component({
  selector: 'jhi-rent-detail',
  templateUrl: './rent-detail.component.html'
})
export class RentDetailComponent implements OnInit {
  rent: IRent | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rent }) => (this.rent = rent));
  }

  previousState(): void {
    window.history.back();
  }
}
