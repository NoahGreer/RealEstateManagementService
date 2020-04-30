import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInfraction } from 'app/shared/model/infraction.model';

@Component({
  selector: 'jhi-infraction-detail',
  templateUrl: './infraction-detail.component.html'
})
export class InfractionDetailComponent implements OnInit {
  infraction: IInfraction | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ infraction }) => (this.infraction = infraction));
  }

  previousState(): void {
    window.history.back();
  }
}
