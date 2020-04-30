import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IMaintenance } from 'app/shared/model/maintenance.model';

type EntityResponseType = HttpResponse<IMaintenance>;
type EntityArrayResponseType = HttpResponse<IMaintenance[]>;

@Injectable({ providedIn: 'root' })
export class MaintenanceService {
  public resourceUrl = SERVER_API_URL + 'api/maintenances';

  constructor(protected http: HttpClient) {}

  create(maintenance: IMaintenance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(maintenance);
    return this.http
      .post<IMaintenance>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(maintenance: IMaintenance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(maintenance);
    return this.http
      .put<IMaintenance>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMaintenance>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMaintenance[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(maintenance: IMaintenance): IMaintenance {
    const copy: IMaintenance = Object.assign({}, maintenance, {
      notificationDate:
        maintenance.notificationDate && maintenance.notificationDate.isValid()
          ? maintenance.notificationDate.format(DATE_FORMAT)
          : undefined,
      dateComplete:
        maintenance.dateComplete && maintenance.dateComplete.isValid() ? maintenance.dateComplete.format(DATE_FORMAT) : undefined,
      repairPaidOn:
        maintenance.repairPaidOn && maintenance.repairPaidOn.isValid() ? maintenance.repairPaidOn.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.notificationDate = res.body.notificationDate ? moment(res.body.notificationDate) : undefined;
      res.body.dateComplete = res.body.dateComplete ? moment(res.body.dateComplete) : undefined;
      res.body.repairPaidOn = res.body.repairPaidOn ? moment(res.body.repairPaidOn) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((maintenance: IMaintenance) => {
        maintenance.notificationDate = maintenance.notificationDate ? moment(maintenance.notificationDate) : undefined;
        maintenance.dateComplete = maintenance.dateComplete ? moment(maintenance.dateComplete) : undefined;
        maintenance.repairPaidOn = maintenance.repairPaidOn ? moment(maintenance.repairPaidOn) : undefined;
      });
    }
    return res;
  }
}
