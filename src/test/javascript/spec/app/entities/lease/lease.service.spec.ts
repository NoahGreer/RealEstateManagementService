import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { LeaseService } from 'app/entities/lease/lease.service';
import { ILease, Lease } from 'app/shared/model/lease.model';

describe('Service Tests', () => {
  describe('Lease Service', () => {
    let injector: TestBed;
    let service: LeaseService;
    let httpMock: HttpTestingController;
    let elemDefault: ILease;
    let expectedResult: ILease | ILease[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(LeaseService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Lease(0, currentDate, currentDate, currentDate, currentDate, 0, 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateSigned: currentDate.format(DATE_FORMAT),
            moveInDate: currentDate.format(DATE_FORMAT),
            noticeOfRemovalOrMoveoutDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Lease', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateSigned: currentDate.format(DATE_FORMAT),
            moveInDate: currentDate.format(DATE_FORMAT),
            noticeOfRemovalOrMoveoutDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateSigned: currentDate,
            moveInDate: currentDate,
            noticeOfRemovalOrMoveoutDate: currentDate,
            endDate: currentDate
          },
          returnedFromService
        );

        service.create(new Lease()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Lease', () => {
        const returnedFromService = Object.assign(
          {
            dateSigned: currentDate.format(DATE_FORMAT),
            moveInDate: currentDate.format(DATE_FORMAT),
            noticeOfRemovalOrMoveoutDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            amount: 1,
            leaseType: 'BBBBBB',
            notes: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateSigned: currentDate,
            moveInDate: currentDate,
            noticeOfRemovalOrMoveoutDate: currentDate,
            endDate: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Lease', () => {
        const returnedFromService = Object.assign(
          {
            dateSigned: currentDate.format(DATE_FORMAT),
            moveInDate: currentDate.format(DATE_FORMAT),
            noticeOfRemovalOrMoveoutDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            amount: 1,
            leaseType: 'BBBBBB',
            notes: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateSigned: currentDate,
            moveInDate: currentDate,
            noticeOfRemovalOrMoveoutDate: currentDate,
            endDate: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Lease', () => {
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
