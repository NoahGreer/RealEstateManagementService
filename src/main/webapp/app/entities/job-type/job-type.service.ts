import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IJobType } from 'app/shared/model/job-type.model';

type EntityResponseType = HttpResponse<IJobType>;
type EntityArrayResponseType = HttpResponse<IJobType[]>;

@Injectable({ providedIn: 'root' })
export class JobTypeService {
  public resourceUrl = SERVER_API_URL + 'api/job-types';

  constructor(protected http: HttpClient) {}

  create(jobType: IJobType): Observable<EntityResponseType> {
    return this.http.post<IJobType>(this.resourceUrl, jobType, { observe: 'response' });
  }

  update(jobType: IJobType): Observable<EntityResponseType> {
    return this.http.put<IJobType>(this.resourceUrl, jobType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IJobType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IJobType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
