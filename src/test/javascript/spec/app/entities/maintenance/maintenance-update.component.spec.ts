import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { RealEstateManagementServiceTestModule } from '../../../test.module';
import { MaintenanceUpdateComponent } from 'app/entities/maintenance/maintenance-update.component';
import { MaintenanceService } from 'app/entities/maintenance/maintenance.service';
import { Maintenance } from 'app/shared/model/maintenance.model';

describe('Component Tests', () => {
  describe('Maintenance Management Update Component', () => {
    let comp: MaintenanceUpdateComponent;
    let fixture: ComponentFixture<MaintenanceUpdateComponent>;
    let service: MaintenanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RealEstateManagementServiceTestModule],
        declarations: [MaintenanceUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MaintenanceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MaintenanceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MaintenanceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Maintenance(123);
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
        const entity = new Maintenance();
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
