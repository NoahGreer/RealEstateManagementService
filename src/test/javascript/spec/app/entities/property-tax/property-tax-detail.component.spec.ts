import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RealEstateManagementServiceTestModule } from '../../../test.module';
import { PropertyTaxDetailComponent } from 'app/entities/property-tax/property-tax-detail.component';
import { PropertyTax } from 'app/shared/model/property-tax.model';

describe('Component Tests', () => {
  describe('PropertyTax Management Detail Component', () => {
    let comp: PropertyTaxDetailComponent;
    let fixture: ComponentFixture<PropertyTaxDetailComponent>;
    const route = ({ data: of({ propertyTax: new PropertyTax(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RealEstateManagementServiceTestModule],
        declarations: [PropertyTaxDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PropertyTaxDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PropertyTaxDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load propertyTax on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.propertyTax).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
