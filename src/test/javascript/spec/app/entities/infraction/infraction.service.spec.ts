import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { InfractionService } from 'app/entities/infraction/infraction.service';
import { IInfraction, Infraction } from 'app/shared/model/infraction.model';

describe('Service Tests', () => {
  describe('Infraction Service', () => {
    let injector: TestBed;
    let service: InfractionService;
    let httpMock: HttpTestingController;
    let elemDefault: IInfraction;
    let expectedResult: IInfraction | IInfraction[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(InfractionService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Infraction(0, currentDate, 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateOccurred: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Infraction', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateOccurred: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOccurred: currentDate
          },
          returnedFromService
        );

        service.create(new Infraction()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Infraction', () => {
        const returnedFromService = Object.assign(
          {
            dateOccurred: currentDate.format(DATE_FORMAT),
            cause: 'BBBBBB',
            resolution: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOccurred: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Infraction', () => {
        const returnedFromService = Object.assign(
          {
            dateOccurred: currentDate.format(DATE_FORMAT),
            cause: 'BBBBBB',
            resolution: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOccurred: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Infraction', () => {
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
