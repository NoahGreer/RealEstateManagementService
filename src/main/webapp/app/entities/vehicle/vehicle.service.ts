import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IVehicle } from 'app/shared/model/vehicle.model';

type EntityResponseType = HttpResponse<IVehicle>;
type EntityArrayResponseType = HttpResponse<IVehicle[]>;

@Injectable({ providedIn: 'root' })
export class VehicleService {
  public resourceUrl = SERVER_API_URL + 'api/vehicles';

  constructor(protected http: HttpClient) {}

  create(vehicle: IVehicle): Observable<EntityResponseType> {
    return this.http.post<IVehicle>(this.resourceUrl, vehicle, { observe: 'response' });
  }

  update(vehicle: IVehicle): Observable<EntityResponseType> {
    return this.http.put<IVehicle>(this.resourceUrl, vehicle, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVehicle>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVehicle[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
