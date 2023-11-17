import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IHousehold } from '../household.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../household.test-samples';

import { HouseholdService } from './household.service';

const requireRestSample: IHousehold = {
  ...sampleWithRequiredData,
};

describe('Household Service', () => {
  let service: HouseholdService;
  let httpMock: HttpTestingController;
  let expectedResult: IHousehold | IHousehold[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HouseholdService);
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

    it('should create a Household', () => {
      const household = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(household).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Household', () => {
      const household = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(household).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Household', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Household', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Household', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addHouseholdToCollectionIfMissing', () => {
      it('should add a Household to an empty array', () => {
        const household: IHousehold = sampleWithRequiredData;
        expectedResult = service.addHouseholdToCollectionIfMissing([], household);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(household);
      });

      it('should not add a Household to an array that contains it', () => {
        const household: IHousehold = sampleWithRequiredData;
        const householdCollection: IHousehold[] = [
          {
            ...household,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHouseholdToCollectionIfMissing(householdCollection, household);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Household to an array that doesn't contain it", () => {
        const household: IHousehold = sampleWithRequiredData;
        const householdCollection: IHousehold[] = [sampleWithPartialData];
        expectedResult = service.addHouseholdToCollectionIfMissing(householdCollection, household);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(household);
      });

      it('should add only unique Household to an array', () => {
        const householdArray: IHousehold[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const householdCollection: IHousehold[] = [sampleWithRequiredData];
        expectedResult = service.addHouseholdToCollectionIfMissing(householdCollection, ...householdArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const household: IHousehold = sampleWithRequiredData;
        const household2: IHousehold = sampleWithPartialData;
        expectedResult = service.addHouseholdToCollectionIfMissing([], household, household2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(household);
        expect(expectedResult).toContain(household2);
      });

      it('should accept null and undefined values', () => {
        const household: IHousehold = sampleWithRequiredData;
        expectedResult = service.addHouseholdToCollectionIfMissing([], null, household, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(household);
      });

      it('should return initial array if no Household is added', () => {
        const householdCollection: IHousehold[] = [sampleWithRequiredData];
        expectedResult = service.addHouseholdToCollectionIfMissing(householdCollection, undefined, null);
        expect(expectedResult).toEqual(householdCollection);
      });
    });

    describe('compareHousehold', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHousehold(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareHousehold(entity1, entity2);
        const compareResult2 = service.compareHousehold(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareHousehold(entity1, entity2);
        const compareResult2 = service.compareHousehold(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareHousehold(entity1, entity2);
        const compareResult2 = service.compareHousehold(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
