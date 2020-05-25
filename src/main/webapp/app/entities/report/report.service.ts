import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
// import * as moment from 'moment';

// import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IReport } from 'app/shared/model/report.model';
import { IRent } from 'app/shared/model/rent.model';

type ReportResponseType = HttpResponse<IReport>;
type TestResponseType = HttpResponse<any[]>;
type EntityArrayResponseType = HttpResponse<IReport[]>;

@Injectable({ providedIn: 'root' })
export class ReportService {
  public resourceUrl = SERVER_API_URL + 'api/reports';

  constructor(protected http: HttpClient) {}

  findRentsPaid(): Observable<TestResponseType> {
    return this.http.get<any[]>(`${this.resourceUrl}/rents/paid?date=2020-05-10`, { observe: 'response' });
  }

  findTestReport(): Observable<TestResponseType> {
    return this.http.get<any>(`${this.resourceUrl}/reportTest`, { observe: 'response' });
  }
}
