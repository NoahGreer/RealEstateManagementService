import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { RealEstateManagementServiceTestModule } from '../../../test.module';
import { RentUpdateComponent } from 'app/entities/rent/rent-update.component';
import { RentService } from 'app/entities/rent/rent.service';
import { Rent } from 'app/shared/model/rent.model';

describe('Component Tests', () => {
  describe('Rent Management Update Component', () => {
    let comp: RentUpdateComponent;
    let fixture: ComponentFixture<RentUpdateComponent>;
    let service: RentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RealEstateManagementServiceTestModule],
        declarations: [RentUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(RentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RentUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RentService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Rent(123);
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
        const entity = new Rent();
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
