import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RealEstateManagementServiceTestModule } from '../../../test.module';
import { RentDetailComponent } from 'app/entities/rent/rent-detail.component';
import { Rent } from 'app/shared/model/rent.model';

describe('Component Tests', () => {
  describe('Rent Management Detail Component', () => {
    let comp: RentDetailComponent;
    let fixture: ComponentFixture<RentDetailComponent>;
    const route = ({ data: of({ rent: new Rent(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RealEstateManagementServiceTestModule],
        declarations: [RentDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(RentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load rent on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.rent).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
