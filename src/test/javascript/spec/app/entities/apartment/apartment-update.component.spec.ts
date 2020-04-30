import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { RealEstateManagementServiceTestModule } from '../../../test.module';
import { ApartmentUpdateComponent } from 'app/entities/apartment/apartment-update.component';
import { ApartmentService } from 'app/entities/apartment/apartment.service';
import { Apartment } from 'app/shared/model/apartment.model';

describe('Component Tests', () => {
  describe('Apartment Management Update Component', () => {
    let comp: ApartmentUpdateComponent;
    let fixture: ComponentFixture<ApartmentUpdateComponent>;
    let service: ApartmentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RealEstateManagementServiceTestModule],
        declarations: [ApartmentUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ApartmentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ApartmentUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ApartmentService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Apartment(123);
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
        const entity = new Apartment();
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
