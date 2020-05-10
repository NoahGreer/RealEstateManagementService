import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RealEstateManagementServiceTestModule } from '../../../test.module';
import { InfractionDetailComponent } from 'app/entities/infraction/infraction-detail.component';
import { Infraction } from 'app/shared/model/infraction.model';

describe('Component Tests', () => {
  describe('Infraction Management Detail Component', () => {
    let comp: InfractionDetailComponent;
    let fixture: ComponentFixture<InfractionDetailComponent>;
    const route = ({ data: of({ infraction: new Infraction(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RealEstateManagementServiceTestModule],
        declarations: [InfractionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(InfractionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InfractionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load infraction on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.infraction).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
