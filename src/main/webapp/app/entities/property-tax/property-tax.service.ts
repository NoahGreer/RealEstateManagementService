import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPropertyTax } from 'app/shared/model/property-tax.model';

type EntityResponseType = HttpResponse<IPropertyTax>;
type EntityArrayResponseType = HttpResponse<IPropertyTax[]>;

@Injectable({ providedIn: 'root' })
export class PropertyTaxService {
  public resourceUrl = SERVER_API_URL + 'api/property-taxes';

  constructor(protected http: HttpClient) {}

  create(propertyTax: IPropertyTax): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(propertyTax);
    return this.http
      .post<IPropertyTax>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(propertyTax: IPropertyTax): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(propertyTax);
    return this.http
      .put<IPropertyTax>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPropertyTax>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPropertyTax[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(propertyTax: IPropertyTax): IPropertyTax {
    const copy: IPropertyTax = Object.assign({}, propertyTax, {
      datePaid: propertyTax.datePaid && propertyTax.datePaid.isValid() ? propertyTax.datePaid.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.datePaid = res.body.datePaid ? moment(res.body.datePaid) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((propertyTax: IPropertyTax) => {
        propertyTax.datePaid = propertyTax.datePaid ? moment(propertyTax.datePaid) : undefined;
      });
    }
    return res;
  }
}
