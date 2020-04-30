import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILease } from 'app/shared/model/lease.model';

type EntityResponseType = HttpResponse<ILease>;
type EntityArrayResponseType = HttpResponse<ILease[]>;

@Injectable({ providedIn: 'root' })
export class LeaseService {
  public resourceUrl = SERVER_API_URL + 'api/leases';

  constructor(protected http: HttpClient) {}

  create(lease: ILease): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lease);
    return this.http
      .post<ILease>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(lease: ILease): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lease);
    return this.http
      .put<ILease>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILease>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILease[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(lease: ILease): ILease {
    const copy: ILease = Object.assign({}, lease, {
      dateSigned: lease.dateSigned && lease.dateSigned.isValid() ? lease.dateSigned.format(DATE_FORMAT) : undefined,
      moveInDate: lease.moveInDate && lease.moveInDate.isValid() ? lease.moveInDate.format(DATE_FORMAT) : undefined,
      noticeOfRemovalOrMoveoutDate:
        lease.noticeOfRemovalOrMoveoutDate && lease.noticeOfRemovalOrMoveoutDate.isValid()
          ? lease.noticeOfRemovalOrMoveoutDate.format(DATE_FORMAT)
          : undefined,
      endDate: lease.endDate && lease.endDate.isValid() ? lease.endDate.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateSigned = res.body.dateSigned ? moment(res.body.dateSigned) : undefined;
      res.body.moveInDate = res.body.moveInDate ? moment(res.body.moveInDate) : undefined;
      res.body.noticeOfRemovalOrMoveoutDate = res.body.noticeOfRemovalOrMoveoutDate
        ? moment(res.body.noticeOfRemovalOrMoveoutDate)
        : undefined;
      res.body.endDate = res.body.endDate ? moment(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((lease: ILease) => {
        lease.dateSigned = lease.dateSigned ? moment(lease.dateSigned) : undefined;
        lease.moveInDate = lease.moveInDate ? moment(lease.moveInDate) : undefined;
        lease.noticeOfRemovalOrMoveoutDate = lease.noticeOfRemovalOrMoveoutDate ? moment(lease.noticeOfRemovalOrMoveoutDate) : undefined;
        lease.endDate = lease.endDate ? moment(lease.endDate) : undefined;
      });
    }
    return res;
  }
}
