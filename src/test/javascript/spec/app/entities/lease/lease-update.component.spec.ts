import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { RealEstateManagementServiceTestModule } from '../../../test.module';
import { LeaseUpdateComponent } from 'app/entities/lease/lease-update.component';
import { LeaseService } from 'app/entities/lease/lease.service';
import { Lease } from 'app/shared/model/lease.model';

describe('Component Tests', () => {
  describe('Lease Management Update Component', () => {
    let comp: LeaseUpdateComponent;
    let fixture: ComponentFixture<LeaseUpdateComponent>;
    let service: LeaseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RealEstateManagementServiceTestModule],
        declarations: [LeaseUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(LeaseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LeaseUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LeaseService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Lease(123);
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
        const entity = new Lease();
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
