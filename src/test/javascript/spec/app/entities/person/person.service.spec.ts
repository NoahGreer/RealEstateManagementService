import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { PersonService } from 'app/entities/person/person.service';
import { IPerson, Person } from 'app/shared/model/person.model';

describe('Service Tests', () => {
  describe('Person Service', () => {
    let injector: TestBed;
    let service: PersonService;
    let httpMock: HttpTestingController;
    let elemDefault: IPerson;
    let expectedResult: IPerson | IPerson[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(PersonService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Person(
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        false,
        false,
        'AAAAAAA',
        currentDate,
        'AAAAAAA',
        currentDate,
        'AAAAAAA'
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            backgroundCheckDate: currentDate.format(DATE_FORMAT),
            employmentVerificationDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Person', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            backgroundCheckDate: currentDate.format(DATE_FORMAT),
            employmentVerificationDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            backgroundCheckDate: currentDate,
            employmentVerificationDate: currentDate
          },
          returnedFromService
        );

        service.create(new Person()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Person', () => {
        const returnedFromService = Object.assign(
          {
            firstName: 'BBBBBB',
            lastName: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            emailAddress: 'BBBBBB',
            primaryContact: true,
            isMinor: true,
            ssn: 'BBBBBB',
            backgroundCheckDate: currentDate.format(DATE_FORMAT),
            backgroundCheckConfirmationNumber: 'BBBBBB',
            employmentVerificationDate: currentDate.format(DATE_FORMAT),
            employmentVerificationConfirmationNumber: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            backgroundCheckDate: currentDate,
            employmentVerificationDate: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Person', () => {
        const returnedFromService = Object.assign(
          {
            firstName: 'BBBBBB',
            lastName: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            emailAddress: 'BBBBBB',
            primaryContact: true,
            isMinor: true,
            ssn: 'BBBBBB',
            backgroundCheckDate: currentDate.format(DATE_FORMAT),
            backgroundCheckConfirmationNumber: 'BBBBBB',
            employmentVerificationDate: currentDate.format(DATE_FORMAT),
            employmentVerificationConfirmationNumber: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            backgroundCheckDate: currentDate,
            employmentVerificationDate: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Person', () => {
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
