import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { RealEstateManagementServiceTestModule } from '../../../test.module';
import { JobTypeUpdateComponent } from 'app/entities/job-type/job-type-update.component';
import { JobTypeService } from 'app/entities/job-type/job-type.service';
import { JobType } from 'app/shared/model/job-type.model';

describe('Component Tests', () => {
  describe('JobType Management Update Component', () => {
    let comp: JobTypeUpdateComponent;
    let fixture: ComponentFixture<JobTypeUpdateComponent>;
    let service: JobTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RealEstateManagementServiceTestModule],
        declarations: [JobTypeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(JobTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JobTypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(JobTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new JobType(123);
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
        const entity = new JobType();
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
