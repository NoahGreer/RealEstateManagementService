import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { PetService } from 'app/entities/pet/pet.service';
import { IPet, Pet } from 'app/shared/model/pet.model';

describe('Service Tests', () => {
  describe('Pet Service', () => {
    let injector: TestBed;
    let service: PetService;
    let httpMock: HttpTestingController;
    let elemDefault: IPet;
    let expectedResult: IPet | IPet[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(PetService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Pet(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Pet', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Pet()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Pet', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            type: 'BBBBBB',
            color: 'BBBBBB',
            certifiedAssistanceAnimal: true
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Pet', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            type: 'BBBBBB',
            color: 'BBBBBB',
            certifiedAssistanceAnimal: true
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Pet', () => {
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
