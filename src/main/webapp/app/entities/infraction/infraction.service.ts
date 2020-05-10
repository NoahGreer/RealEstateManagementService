import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IInfraction } from 'app/shared/model/infraction.model';

type EntityResponseType = HttpResponse<IInfraction>;
type EntityArrayResponseType = HttpResponse<IInfraction[]>;

@Injectable({ providedIn: 'root' })
export class InfractionService {
  public resourceUrl = SERVER_API_URL + 'api/infractions';

  constructor(protected http: HttpClient) {}

  create(infraction: IInfraction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(infraction);
    return this.http
      .post<IInfraction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(infraction: IInfraction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(infraction);
    return this.http
      .put<IInfraction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInfraction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInfraction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(infraction: IInfraction): IInfraction {
    const copy: IInfraction = Object.assign({}, infraction, {
      dateOccurred: infraction.dateOccurred && infraction.dateOccurred.isValid() ? infraction.dateOccurred.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOccurred = res.body.dateOccurred ? moment(res.body.dateOccurred) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((infraction: IInfraction) => {
        infraction.dateOccurred = infraction.dateOccurred ? moment(infraction.dateOccurred) : undefined;
      });
    }
    return res;
  }
}
