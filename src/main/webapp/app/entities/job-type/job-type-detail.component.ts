import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IJobType } from 'app/shared/model/job-type.model';

@Component({
  selector: 'jhi-job-type-detail',
  templateUrl: './job-type-detail.component.html'
})
export class JobTypeDetailComponent implements OnInit {
  jobType: IJobType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jobType }) => (this.jobType = jobType));
  }

  previousState(): void {
    window.history.back();
  }
}
