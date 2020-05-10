import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { RealEstateManagementServiceTestModule } from '../../../test.module';
import { InfractionUpdateComponent } from 'app/entities/infraction/infraction-update.component';
import { InfractionService } from 'app/entities/infraction/infraction.service';
import { Infraction } from 'app/shared/model/infraction.model';

describe('Component Tests', () => {
  describe('Infraction Management Update Component', () => {
    let comp: InfractionUpdateComponent;
    let fixture: ComponentFixture<InfractionUpdateComponent>;
    let service: InfractionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RealEstateManagementServiceTestModule],
        declarations: [InfractionUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(InfractionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InfractionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InfractionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Infraction(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Infraction();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
