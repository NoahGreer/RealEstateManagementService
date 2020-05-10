import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVehicle } from 'app/shared/model/vehicle.model';

@Component({
  selector: 'jhi-vehicle-detail',
  templateUrl: './vehicle-detail.component.html'
})
export class VehicleDetailComponent implements OnInit {
  vehicle: IVehicle | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vehicle }) => (this.vehicle = vehicle));
  }

  previousState(): void {
    window.history.back();
  }
}
