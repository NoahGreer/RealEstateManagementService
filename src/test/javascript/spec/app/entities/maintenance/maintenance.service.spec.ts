import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { MaintenanceService } from 'app/entities/maintenance/maintenance.service';
import { IMaintenance, Maintenance } from 'app/shared/model/maintenance.model';

describe('Service Tests', () => {
  describe('Maintenance Service', () => {
    let injector: TestBed;
    let service: MaintenanceService;
    let httpMock: HttpTestingController;
    let elemDefault: IMaintenance;
    let expectedResult: IMaintenance | IMaintenance[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(MaintenanceService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Maintenance(0, 'AAAAAAA', currentDate, currentDate, 'AAAAAAA', 0, currentDate, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            notificationDate: currentDate.format(DATE_FORMAT),
            dateComplete: currentDate.format(DATE_FORMAT),
            repairPaidOn: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Maintenance', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            notificationDate: currentDate.format(DATE_FORMAT),
            dateComplete: currentDate.format(DATE_FORMAT),
            repairPaidOn: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            notificationDate: currentDate,
            dateComplete: currentDate,
            repairPaidOn: currentDate
          },
          returnedFromService
        );

        service.create(new Maintenance()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Maintenance', () => {
        const returnedFromService = Object.assign(
          {
            description: 'BBBBBB',
            notificationDate: currentDate.format(DATE_FORMAT),
            dateComplete: currentDate.format(DATE_FORMAT),
            contractorJobNumber: 'BBBBBB',
            repairCost: 1,
            repairPaidOn: currentDate.format(DATE_FORMAT),
            receiptOfPayment: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            notificationDate: currentDate,
            dateComplete: currentDate,
            repairPaidOn: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Maintenance', () => {
        const returnedFromService = Object.assign(
          {
            description: 'BBBBBB',
            notificationDate: currentDate.format(DATE_FORMAT),
            dateComplete: currentDate.format(DATE_FORMAT),
            contractorJobNumber: 'BBBBBB',
            repairCost: 1,
            repairPaidOn: currentDate.format(DATE_FORMAT),
            receiptOfPayment: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            notificationDate: currentDate,
            dateComplete: currentDate,
            repairPaidOn: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Maintenance', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
