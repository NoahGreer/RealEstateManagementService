import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IApartment } from 'app/shared/model/apartment.model';

@Component({
  selector: 'jhi-apartment-detail',
  templateUrl: './apartment-detail.component.html'
})
export class ApartmentDetailComponent implements OnInit {
  apartment: IApartment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ apartment }) => (this.apartment = apartment));
  }

  previousState(): void {
    window.history.back();
  }
}
