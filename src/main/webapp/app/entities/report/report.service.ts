import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';

type TestResponseType = HttpResponse<Object[]>;

// type EntityArrayResponseType = HttpResponse<IReport[]>;

@Injectable({ providedIn: 'root' })
export class ReportService {
  public resourceUrl = SERVER_API_URL + 'api/reports';
  public reportType: string;
  public passedParamValue: any;

  constructor(protected http: HttpClient) {
    this.reportType = '';
    this.passedParamValue = '';
  }

  getRentsPaid(date: string): Observable<TestResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/rents/paid?date=${date}`, { observe: 'response' });
  }

  getAvailableApartments(): Observable<TestResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/apartments/available`, { observe: 'response' });
  }

  getAuthorizedVehicles(buildingId: number): Observable<TestResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/buildings/${buildingId}/vehicles/authorized`, { observe: 'response' });
  }

  getContacts(): Observable<TestResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/people/contact`, { observe: 'response' });
  }

  getEmails(): Observable<TestResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/people/email`, { observe: 'response' });
  }

  getInfractions(): Observable<TestResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/reportTest`, { observe: 'response' });
  }

  getTenantsByApartment(apartmentId: number): Observable<TestResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/apartments/` + apartmentId + `/tenants`, { observe: 'response' });
  }

  getNextExpiringLeases(count: number): Observable<TestResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/lease/expire?count=` + count, { observe: 'response' });
  }

  getContractorByJobType(jobTypeId: number): Observable<TestResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/contractor/jobtype?id=` + jobTypeId, { observe: 'response' });
  }

  getApartmentMaintenanceHistory(apartmentId: number): Observable<TestResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/apartments/` + apartmentId + `/maintenance/history`, { observe: 'response' });
  }

  getContractorMaintenanceHistory(contractorId: number): Observable<TestResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/contractors/` + contractorId + `/maintenance/history`, { observe: 'response' });
  }

  getPropertyTaxHistory(): Observable<TestResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/tax/property`, { observe: 'response' });
  }

  getOpenMaintenance(): Observable<TestResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/maintenance/open`, { observe: 'response' });
  }

  getPetOwners(): Observable<TestResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/pet/owner`, { observe: 'response' });
  }

  getTestReport(): Observable<TestResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/reportTest`, { observe: 'response' });
  }
}
