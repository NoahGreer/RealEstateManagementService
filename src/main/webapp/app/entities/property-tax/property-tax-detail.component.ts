import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPropertyTax } from 'app/shared/model/property-tax.model';

@Component({
  selector: 'jhi-property-tax-detail',
  templateUrl: './property-tax-detail.component.html'
})
export class PropertyTaxDetailComponent implements OnInit {
  propertyTax: IPropertyTax | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ propertyTax }) => (this.propertyTax = propertyTax));
  }

  previousState(): void {
    window.history.back();
  }
}
