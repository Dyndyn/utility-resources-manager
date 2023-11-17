import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUtilityProvider } from '../utility-provider.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../utility-provider.test-samples';

import { UtilityProviderService } from './utility-provider.service';

const requireRestSample: IUtilityProvider = {
  ...sampleWithRequiredData,
};

describe('UtilityProvider Service', () => {
  let service: UtilityProviderService;
  let httpMock: HttpTestingController;
  let expectedResult: IUtilityProvider | IUtilityProvider[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UtilityProviderService);
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

    it('should create a UtilityProvider', () => {
      const utilityProvider = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(utilityProvider).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UtilityProvider', () => {
      const utilityProvider = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(utilityProvider).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UtilityProvider', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UtilityProvider', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UtilityProvider', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUtilityProviderToCollectionIfMissing', () => {
      it('should add a UtilityProvider to an empty array', () => {
        const utilityProvider: IUtilityProvider = sampleWithRequiredData;
        expectedResult = service.addUtilityProviderToCollectionIfMissing([], utilityProvider);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(utilityProvider);
      });

      it('should not add a UtilityProvider to an array that contains it', () => {
        const utilityProvider: IUtilityProvider = sampleWithRequiredData;
        const utilityProviderCollection: IUtilityProvider[] = [
          {
            ...utilityProvider,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUtilityProviderToCollectionIfMissing(utilityProviderCollection, utilityProvider);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UtilityProvider to an array that doesn't contain it", () => {
        const utilityProvider: IUtilityProvider = sampleWithRequiredData;
        const utilityProviderCollection: IUtilityProvider[] = [sampleWithPartialData];
        expectedResult = service.addUtilityProviderToCollectionIfMissing(utilityProviderCollection, utilityProvider);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(utilityProvider);
      });

      it('should add only unique UtilityProvider to an array', () => {
        const utilityProviderArray: IUtilityProvider[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const utilityProviderCollection: IUtilityProvider[] = [sampleWithRequiredData];
        expectedResult = service.addUtilityProviderToCollectionIfMissing(utilityProviderCollection, ...utilityProviderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const utilityProvider: IUtilityProvider = sampleWithRequiredData;
        const utilityProvider2: IUtilityProvider = sampleWithPartialData;
        expectedResult = service.addUtilityProviderToCollectionIfMissing([], utilityProvider, utilityProvider2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(utilityProvider);
        expect(expectedResult).toContain(utilityProvider2);
      });

      it('should accept null and undefined values', () => {
        const utilityProvider: IUtilityProvider = sampleWithRequiredData;
        expectedResult = service.addUtilityProviderToCollectionIfMissing([], null, utilityProvider, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(utilityProvider);
      });

      it('should return initial array if no UtilityProvider is added', () => {
        const utilityProviderCollection: IUtilityProvider[] = [sampleWithRequiredData];
        expectedResult = service.addUtilityProviderToCollectionIfMissing(utilityProviderCollection, undefined, null);
        expect(expectedResult).toEqual(utilityProviderCollection);
      });
    });

    describe('compareUtilityProvider', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUtilityProvider(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUtilityProvider(entity1, entity2);
        const compareResult2 = service.compareUtilityProvider(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUtilityProvider(entity1, entity2);
        const compareResult2 = service.compareUtilityProvider(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUtilityProvider(entity1, entity2);
        const compareResult2 = service.compareUtilityProvider(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
