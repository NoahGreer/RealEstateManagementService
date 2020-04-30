import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RealEstateManagementServiceTestModule } from '../../../test.module';
import { LeaseDetailComponent } from 'app/entities/lease/lease-detail.component';
import { Lease } from 'app/shared/model/lease.model';

describe('Component Tests', () => {
  describe('Lease Management Detail Component', () => {
    let comp: LeaseDetailComponent;
    let fixture: ComponentFixture<LeaseDetailComponent>;
    const route = ({ data: of({ lease: new Lease(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RealEstateManagementServiceTestModule],
        declarations: [LeaseDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(LeaseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LeaseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load lease on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.lease).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
