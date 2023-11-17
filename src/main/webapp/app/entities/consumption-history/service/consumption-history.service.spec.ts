import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IConsumptionHistory } from '../consumption-history.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../consumption-history.test-samples';

import { ConsumptionHistoryService, RestConsumptionHistory } from './consumption-history.service';

const requireRestSample: RestConsumptionHistory = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
};

describe('ConsumptionHistory Service', () => {
  let service: ConsumptionHistoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IConsumptionHistory | IConsumptionHistory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ConsumptionHistoryService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ConsumptionHistory', () => {
      const consumptionHistory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(consumptionHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ConsumptionHistory', () => {
      const consumptionHistory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(consumptionHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ConsumptionHistory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ConsumptionHistory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ConsumptionHistory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addConsumptionHistoryToCollectionIfMissing', () => {
      it('should add a ConsumptionHistory to an empty array', () => {
        const consumptionHistory: IConsumptionHistory = sampleWithRequiredData;
        expectedResult = service.addConsumptionHistoryToCollectionIfMissing([], consumptionHistory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(consumptionHistory);
      });

      it('should not add a ConsumptionHistory to an array that contains it', () => {
        const consumptionHistory: IConsumptionHistory = sampleWithRequiredData;
        const consumptionHistoryCollection: IConsumptionHistory[] = [
          {
            ...consumptionHistory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addConsumptionHistoryToCollectionIfMissing(consumptionHistoryCollection, consumptionHistory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ConsumptionHistory to an array that doesn't contain it", () => {
        const consumptionHistory: IConsumptionHistory = sampleWithRequiredData;
        const consumptionHistoryCollection: IConsumptionHistory[] = [sampleWithPartialData];
        expectedResult = service.addConsumptionHistoryToCollectionIfMissing(consumptionHistoryCollection, consumptionHistory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(consumptionHistory);
      });

      it('should add only unique ConsumptionHistory to an array', () => {
        const consumptionHistoryArray: IConsumptionHistory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const consumptionHistoryCollection: IConsumptionHistory[] = [sampleWithRequiredData];
        expectedResult = service.addConsumptionHistoryToCollectionIfMissing(consumptionHistoryCollection, ...consumptionHistoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const consumptionHistory: IConsumptionHistory = sampleWithRequiredData;
        const consumptionHistory2: IConsumptionHistory = sampleWithPartialData;
        expectedResult = service.addConsumptionHistoryToCollectionIfMissing([], consumptionHistory, consumptionHistory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(consumptionHistory);
        expect(expectedResult).toContain(consumptionHistory2);
      });

      it('should accept null and undefined values', () => {
        const consumptionHistory: IConsumptionHistory = sampleWithRequiredData;
        expectedResult = service.addConsumptionHistoryToCollectionIfMissing([], null, consumptionHistory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(consumptionHistory);
      });

      it('should return initial array if no ConsumptionHistory is added', () => {
        const consumptionHistoryCollection: IConsumptionHistory[] = [sampleWithRequiredData];
        expectedResult = service.addConsumptionHistoryToCollectionIfMissing(consumptionHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(consumptionHistoryCollection);
      });
    });

    describe('compareConsumptionHistory', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareConsumptionHistory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareConsumptionHistory(entity1, entity2);
        const compareResult2 = service.compareConsumptionHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareConsumptionHistory(entity1, entity2);
        const compareResult2 = service.compareConsumptionHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareConsumptionHistory(entity1, entity2);
        const compareResult2 = service.compareConsumptionHistory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
