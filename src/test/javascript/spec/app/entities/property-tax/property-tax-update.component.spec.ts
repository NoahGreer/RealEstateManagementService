import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { RealEstateManagementServiceTestModule } from '../../../test.module';
import { PropertyTaxUpdateComponent } from 'app/entities/property-tax/property-tax-update.component';
import { PropertyTaxService } from 'app/entities/property-tax/property-tax.service';
import { PropertyTax } from 'app/shared/model/property-tax.model';

describe('Component Tests', () => {
  describe('PropertyTax Management Update Component', () => {
    let comp: PropertyTaxUpdateComponent;
    let fixture: ComponentFixture<PropertyTaxUpdateComponent>;
    let service: PropertyTaxService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RealEstateManagementServiceTestModule],
        declarations: [PropertyTaxUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PropertyTaxUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PropertyTaxUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PropertyTaxService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PropertyTax(123);
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
        const entity = new PropertyTax();
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
