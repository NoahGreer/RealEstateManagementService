import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RealEstateManagementServiceTestModule } from '../../../test.module';
import { ApartmentDetailComponent } from 'app/entities/apartment/apartment-detail.component';
import { Apartment } from 'app/shared/model/apartment.model';

describe('Component Tests', () => {
  describe('Apartment Management Detail Component', () => {
    let comp: ApartmentDetailComponent;
    let fixture: ComponentFixture<ApartmentDetailComponent>;
    const route = ({ data: of({ apartment: new Apartment(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RealEstateManagementServiceTestModule],
        declarations: [ApartmentDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ApartmentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ApartmentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load apartment on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.apartment).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
