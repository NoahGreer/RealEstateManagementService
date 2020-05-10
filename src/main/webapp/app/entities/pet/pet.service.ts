import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPet } from 'app/shared/model/pet.model';

type EntityResponseType = HttpResponse<IPet>;
type EntityArrayResponseType = HttpResponse<IPet[]>;

@Injectable({ providedIn: 'root' })
export class PetService {
  public resourceUrl = SERVER_API_URL + 'api/pets';

  constructor(protected http: HttpClient) {}

  create(pet: IPet): Observable<EntityResponseType> {
    return this.http.post<IPet>(this.resourceUrl, pet, { observe: 'response' });
  }

  update(pet: IPet): Observable<EntityResponseType> {
    return this.http.put<IPet>(this.resourceUrl, pet, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPet>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPet[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
