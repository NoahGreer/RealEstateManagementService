import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBuilding } from 'app/shared/model/building.model';

type EntityResponseType = HttpResponse<IBuilding>;
type EntityArrayResponseType = HttpResponse<IBuilding[]>;

@Injectable({ providedIn: 'root' })
export class BuildingService {
  public resourceUrl = SERVER_API_URL + 'api/buildings';

  constructor(protected http: HttpClient) {}

  create(building: IBuilding): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(building);
    return this.http
      .post<IBuilding>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(building: IBuilding): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(building);
    return this.http
      .put<IBuilding>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBuilding>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBuilding[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(building: IBuilding): IBuilding {
    const copy: IBuilding = Object.assign({}, building, {
      purchaseDate: building.purchaseDate && building.purchaseDate.isValid() ? building.purchaseDate.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.purchaseDate = res.body.purchaseDate ? moment(res.body.purchaseDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((building: IBuilding) => {
        building.purchaseDate = building.purchaseDate ? moment(building.purchaseDate) : undefined;
      });
    }
    return res;
  }
}
