import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RealEstateManagementServiceTestModule } from '../../../test.module';
import { ContractorDetailComponent } from 'app/entities/contractor/contractor-detail.component';
import { Contractor } from 'app/shared/model/contractor.model';

describe('Component Tests', () => {
  describe('Contractor Management Detail Component', () => {
    let comp: ContractorDetailComponent;
    let fixture: ComponentFixture<ContractorDetailComponent>;
    const route = ({ data: of({ contractor: new Contractor(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RealEstateManagementServiceTestModule],
        declarations: [ContractorDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ContractorDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContractorDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load contractor on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contractor).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
