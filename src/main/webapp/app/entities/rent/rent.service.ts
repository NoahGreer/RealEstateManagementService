import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IRent } from 'app/shared/model/rent.model';
import { isRestElement } from '@babel/types';

type EntityResponseType = HttpResponse<IRent>;
type EntityArrayResponseType = HttpResponse<IRent[]>;

@Injectable({ providedIn: 'root' })
export class RentService {
  public resourceUrl = SERVER_API_URL + 'api/rents';

  constructor(protected http: HttpClient) {}

  create(rent: IRent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rent);
    return this.http
      .post<IRent>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(rent: IRent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rent);
    return this.http
      .put<IRent>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRent>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRent[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  pay(id: number): Observable<HttpResponse<{}>> {
    // const today = new Date();
    const temp = this.http.get<IRent>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });

    // return this.http
    //   .put<IRent>(this.resourceUrl, temp, { observe: 'response' })
    //   .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  protected convertDateFromClient(rent: IRent): IRent {
    const copy: IRent = Object.assign({}, rent, {
      dueDate: rent.dueDate && rent.dueDate.isValid() ? rent.dueDate.format(DATE_FORMAT) : undefined,
      recievedDate: rent.recievedDate && rent.recievedDate.isValid() ? rent.recievedDate.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dueDate = res.body.dueDate ? moment(res.body.dueDate) : undefined;
      res.body.recievedDate = res.body.recievedDate ? moment(res.body.recievedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((rent: IRent) => {
        rent.dueDate = rent.dueDate ? moment(rent.dueDate) : undefined;
        rent.recievedDate = rent.recievedDate ? moment(rent.recievedDate) : undefined;
      });
    }
    return res;
  }
}
