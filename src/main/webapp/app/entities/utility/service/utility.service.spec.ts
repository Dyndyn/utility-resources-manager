import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUtility } from '../utility.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../utility.test-samples';

import { UtilityService } from './utility.service';

const requireRestSample: IUtility = {
  ...sampleWithRequiredData,
};

describe('Utility Service', () => {
  let service: UtilityService;
  let httpMock: HttpTestingController;
  let expectedResult: IUtility | IUtility[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UtilityService);
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

    it('should create a Utility', () => {
      const utility = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(utility).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Utility', () => {
      const utility = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(utility).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Utility', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Utility', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Utility', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUtilityToCollectionIfMissing', () => {
      it('should add a Utility to an empty array', () => {
        const utility: IUtility = sampleWithRequiredData;
        expectedResult = service.addUtilityToCollectionIfMissing([], utility);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(utility);
      });

      it('should not add a Utility to an array that contains it', () => {
        const utility: IUtility = sampleWithRequiredData;
        const utilityCollection: IUtility[] = [
          {
            ...utility,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUtilityToCollectionIfMissing(utilityCollection, utility);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Utility to an array that doesn't contain it", () => {
        const utility: IUtility = sampleWithRequiredData;
        const utilityCollection: IUtility[] = [sampleWithPartialData];
        expectedResult = service.addUtilityToCollectionIfMissing(utilityCollection, utility);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(utility);
      });

      it('should add only unique Utility to an array', () => {
        const utilityArray: IUtility[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const utilityCollection: IUtility[] = [sampleWithRequiredData];
        expectedResult = service.addUtilityToCollectionIfMissing(utilityCollection, ...utilityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const utility: IUtility = sampleWithRequiredData;
        const utility2: IUtility = sampleWithPartialData;
        expectedResult = service.addUtilityToCollectionIfMissing([], utility, utility2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(utility);
        expect(expectedResult).toContain(utility2);
      });

      it('should accept null and undefined values', () => {
        const utility: IUtility = sampleWithRequiredData;
        expectedResult = service.addUtilityToCollectionIfMissing([], null, utility, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(utility);
      });

      it('should return initial array if no Utility is added', () => {
        const utilityCollection: IUtility[] = [sampleWithRequiredData];
        expectedResult = service.addUtilityToCollectionIfMissing(utilityCollection, undefined, null);
        expect(expectedResult).toEqual(utilityCollection);
      });
    });

    describe('compareUtility', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUtility(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUtility(entity1, entity2);
        const compareResult2 = service.compareUtility(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUtility(entity1, entity2);
        const compareResult2 = service.compareUtility(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUtility(entity1, entity2);
        const compareResult2 = service.compareUtility(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
