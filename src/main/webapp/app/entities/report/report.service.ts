import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
// import { map } from 'rxjs/operators';
// import * as moment from 'moment';

// import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
// import { createRequestOption } from 'app/shared/util/request-util';
// import { IReport } from 'app/shared/model/report.model';
// import { IRent } from 'app/shared/model/rent.model';

// type ReportResponseType = HttpResponse<IReport>;
type TestResponseType = HttpResponse<Object[]>;
// type EntityArrayResponseType = HttpResponse<IReport[]>;

@Injectable({ providedIn: 'root' })
export class ReportService {
  public resourceUrl = SERVER_API_URL + 'api/reports';

  constructor(protected http: HttpClient) {}

  getRentsPaid(date: string): Observable<TestResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/rents/paid?date=${date}`, { observe: 'response' });
  }

  getAuthorizedVehicles(buildingId: number): Observable<TestResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/buildings/${buildingId}/vehicles/authorized`, { observe: 'response' });
  }

  getAvailableApartments(): Observable<TestResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/apartments/available`, { observe: 'response' });
  }

  getContacts(): Observable<TestResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/people/contact`, { observe: 'response' });
  }

  getEmails(): Observable<TestResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/people/email`, { observe: 'response' });
  }

  getTestReport(): Observable<TestResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/reportTest`, { observe: 'response' });
  }
}
