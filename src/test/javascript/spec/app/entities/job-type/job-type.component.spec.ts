import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { RealEstateManagementServiceTestModule } from '../../../test.module';
import { JobTypeComponent } from 'app/entities/job-type/job-type.component';
import { JobTypeService } from 'app/entities/job-type/job-type.service';
import { JobType } from 'app/shared/model/job-type.model';

describe('Component Tests', () => {
  describe('JobType Management Component', () => {
    let comp: JobTypeComponent;
    let fixture: ComponentFixture<JobTypeComponent>;
    let service: JobTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RealEstateManagementServiceTestModule],
        declarations: [JobTypeComponent]
      })
        .overrideTemplate(JobTypeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JobTypeComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(JobTypeService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new JobType(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.jobTypes && comp.jobTypes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
