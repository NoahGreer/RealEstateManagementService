import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { RealEstateManagementServiceTestModule } from '../../../test.module';
import { ContractorUpdateComponent } from 'app/entities/contractor/contractor-update.component';
import { ContractorService } from 'app/entities/contractor/contractor.service';
import { Contractor } from 'app/shared/model/contractor.model';

describe('Component Tests', () => {
  describe('Contractor Management Update Component', () => {
    let comp: ContractorUpdateComponent;
    let fixture: ComponentFixture<ContractorUpdateComponent>;
    let service: ContractorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RealEstateManagementServiceTestModule],
        declarations: [ContractorUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ContractorUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContractorUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ContractorService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Contractor(123);
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
        const entity = new Contractor();
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
