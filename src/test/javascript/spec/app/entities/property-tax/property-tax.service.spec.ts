import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { PropertyTaxService } from 'app/entities/property-tax/property-tax.service';
import { IPropertyTax, PropertyTax } from 'app/shared/model/property-tax.model';

describe('Service Tests', () => {
  describe('PropertyTax Service', () => {
    let injector: TestBed;
    let service: PropertyTaxService;
    let httpMock: HttpTestingController;
    let elemDefault: IPropertyTax;
    let expectedResult: IPropertyTax | IPropertyTax[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(PropertyTaxService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new PropertyTax(0, 0, 0, currentDate, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            datePaid: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a PropertyTax', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            datePaid: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            datePaid: currentDate
          },
          returnedFromService
        );

        service.create(new PropertyTax()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PropertyTax', () => {
        const returnedFromService = Object.assign(
          {
            taxYear: 1,
            amount: 1,
            datePaid: currentDate.format(DATE_FORMAT),
            confirmationNumber: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            datePaid: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PropertyTax', () => {
        const returnedFromService = Object.assign(
          {
            taxYear: 1,
            amount: 1,
            datePaid: currentDate.format(DATE_FORMAT),
            confirmationNumber: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            datePaid: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a PropertyTax', () => {
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
