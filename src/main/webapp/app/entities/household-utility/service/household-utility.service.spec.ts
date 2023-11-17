import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IHouseholdUtility } from '../household-utility.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../household-utility.test-samples';

import { HouseholdUtilityService } from './household-utility.service';

const requireRestSample: IHouseholdUtility = {
  ...sampleWithRequiredData,
};

describe('HouseholdUtility Service', () => {
  let service: HouseholdUtilityService;
  let httpMock: HttpTestingController;
  let expectedResult: IHouseholdUtility | IHouseholdUtility[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HouseholdUtilityService);
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

    it('should create a HouseholdUtility', () => {
      const householdUtility = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(householdUtility).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a HouseholdUtility', () => {
      const householdUtility = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(householdUtility).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a HouseholdUtility', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of HouseholdUtility', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a HouseholdUtility', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addHouseholdUtilityToCollectionIfMissing', () => {
      it('should add a HouseholdUtility to an empty array', () => {
        const householdUtility: IHouseholdUtility = sampleWithRequiredData;
        expectedResult = service.addHouseholdUtilityToCollectionIfMissing([], householdUtility);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(householdUtility);
      });

      it('should not add a HouseholdUtility to an array that contains it', () => {
        const householdUtility: IHouseholdUtility = sampleWithRequiredData;
        const householdUtilityCollection: IHouseholdUtility[] = [
          {
            ...householdUtility,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHouseholdUtilityToCollectionIfMissing(householdUtilityCollection, householdUtility);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a HouseholdUtility to an array that doesn't contain it", () => {
        const householdUtility: IHouseholdUtility = sampleWithRequiredData;
        const householdUtilityCollection: IHouseholdUtility[] = [sampleWithPartialData];
        expectedResult = service.addHouseholdUtilityToCollectionIfMissing(householdUtilityCollection, householdUtility);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(householdUtility);
      });

      it('should add only unique HouseholdUtility to an array', () => {
        const householdUtilityArray: IHouseholdUtility[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const householdUtilityCollection: IHouseholdUtility[] = [sampleWithRequiredData];
        expectedResult = service.addHouseholdUtilityToCollectionIfMissing(householdUtilityCollection, ...householdUtilityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const householdUtility: IHouseholdUtility = sampleWithRequiredData;
        const householdUtility2: IHouseholdUtility = sampleWithPartialData;
        expectedResult = service.addHouseholdUtilityToCollectionIfMissing([], householdUtility, householdUtility2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(householdUtility);
        expect(expectedResult).toContain(householdUtility2);
      });

      it('should accept null and undefined values', () => {
        const householdUtility: IHouseholdUtility = sampleWithRequiredData;
        expectedResult = service.addHouseholdUtilityToCollectionIfMissing([], null, householdUtility, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(householdUtility);
      });

      it('should return initial array if no HouseholdUtility is added', () => {
        const householdUtilityCollection: IHouseholdUtility[] = [sampleWithRequiredData];
        expectedResult = service.addHouseholdUtilityToCollectionIfMissing(householdUtilityCollection, undefined, null);
        expect(expectedResult).toEqual(householdUtilityCollection);
      });
    });

    describe('compareHouseholdUtility', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHouseholdUtility(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareHouseholdUtility(entity1, entity2);
        const compareResult2 = service.compareHouseholdUtility(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareHouseholdUtility(entity1, entity2);
        const compareResult2 = service.compareHouseholdUtility(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareHouseholdUtility(entity1, entity2);
        const compareResult2 = service.compareHouseholdUtility(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
