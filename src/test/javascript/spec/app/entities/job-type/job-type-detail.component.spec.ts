import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RealEstateManagementServiceTestModule } from '../../../test.module';
import { JobTypeDetailComponent } from 'app/entities/job-type/job-type-detail.component';
import { JobType } from 'app/shared/model/job-type.model';

describe('Component Tests', () => {
  describe('JobType Management Detail Component', () => {
    let comp: JobTypeDetailComponent;
    let fixture: ComponentFixture<JobTypeDetailComponent>;
    const route = ({ data: of({ jobType: new JobType(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RealEstateManagementServiceTestModule],
        declarations: [JobTypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(JobTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(JobTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load jobType on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.jobType).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
