import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';
import * as moment from 'moment';

type ObjectResponseType = HttpResponse<Object[]>;

@Injectable({ providedIn: 'root' })
export class ReportService {
  public resourceUrl = SERVER_API_URL + 'api/reports';

  constructor(protected http: HttpClient) {}

  getRentsPaid(date: string): Observable<ObjectResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/rents/paid?date=${date}`, { observe: 'response' });
  }

  getAvailableApartments(): Observable<ObjectResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/apartments/available`, { observe: 'response' });
  }

  getVehiclesByBuilding(buildingId: number): Observable<ObjectResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/buildings/${buildingId}/vehicles/authorized`, { observe: 'response' });
  }

  getVehiclesByApartment(apartmentId: number): Observable<ObjectResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/apartments/${apartmentId}/vehicles`, { observe: 'response' });
  }

  getRentDelinquencies(date: string): Observable<ObjectResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/rents/delinquencies?date=` + date, { observe: 'response' });
  }

  getContacts(): Observable<ObjectResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/people/contact`, { observe: 'response' });
  }

  getEmails(): Observable<ObjectResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/people/email`, { observe: 'response' });
  }

  getInfractionsByYear(year: string): Observable<ObjectResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/infractions?year=` + year, { observe: 'response' });
  }

  getInfractionsByApartment(apartmentId: number): Observable<ObjectResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/apartment/${apartmentId}/infractions`, { observe: 'response' });
  }

  getTenantsByApartment(apartmentId: number): Observable<ObjectResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/apartments/${apartmentId}/tenants`, { observe: 'response' });
  }

  getNextExpiringLeases(count: number): Observable<ObjectResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/lease/expire?count=` + count, { observe: 'response' });
  }

  getContractorByJobType(jobTypeId: number): Observable<ObjectResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/contractor/jobtype?id=` + jobTypeId, { observe: 'response' });
  }

  getApartmentMaintenanceHistory(apartmentId: number): Observable<ObjectResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/apartments/` + apartmentId + `/maintenance/history`, { observe: 'response' });
  }

  getContractorMaintenanceHistory(contractorId: number): Observable<ObjectResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/contractors/` + contractorId + `/maintenance/history`, { observe: 'response' });
  }

  getPropertyTaxHistory(): Observable<ObjectResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/tax/property`, { observe: 'response' });
  }

  getOpenMaintenance(): Observable<ObjectResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/maintenance/open`, { observe: 'response' });
  }

  getPetOwners(): Observable<ObjectResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/pet/owner`, { observe: 'response' });
  }

  getTestReport(): Observable<ObjectResponseType> {
    return this.http.get<Object[]>(`${this.resourceUrl}/reportTest`, { observe: 'response' });
  }

  formatDate(date: Date): string {
    return moment(date)
      .format('YYYY-MM-DD')
      .toString();
  }

  getTodaysDate(): string {
    return moment()
      .format('YYYY-MM-DD')
      .toString();
  }

  getYearByDate(date: Date): string {
    return moment(date)
      .format('YYYY')
      .toString();
  }
}
