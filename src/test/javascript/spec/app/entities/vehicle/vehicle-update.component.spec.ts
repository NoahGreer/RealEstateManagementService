import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { RealEstateManagementServiceTestModule } from '../../../test.module';
import { VehicleUpdateComponent } from 'app/entities/vehicle/vehicle-update.component';
import { VehicleService } from 'app/entities/vehicle/vehicle.service';
import { Vehicle } from 'app/shared/model/vehicle.model';

describe('Component Tests', () => {
  describe('Vehicle Management Update Component', () => {
    let comp: VehicleUpdateComponent;
    let fixture: ComponentFixture<VehicleUpdateComponent>;
    let service: VehicleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RealEstateManagementServiceTestModule],
        declarations: [VehicleUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(VehicleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VehicleUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VehicleService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Vehicle(123);
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
        const entity = new Vehicle();
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
