import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../household-utility.test-samples';

import { HouseholdUtilityFormService } from './household-utility-form.service';

describe('HouseholdUtility Form Service', () => {
  let service: HouseholdUtilityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HouseholdUtilityFormService);
  });

  describe('Service methods', () => {
    describe('createHouseholdUtilityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHouseholdUtilityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            accountId: expect.any(Object),
            active: expect.any(Object),
            household: expect.any(Object),
            utilityProvider: expect.any(Object),
          }),
        );
      });

      it('passing IHouseholdUtility should create a new form with FormGroup', () => {
        const formGroup = service.createHouseholdUtilityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            accountId: expect.any(Object),
            active: expect.any(Object),
            household: expect.any(Object),
            utilityProvider: expect.any(Object),
          }),
        );
      });
    });

    describe('getHouseholdUtility', () => {
      it('should return NewHouseholdUtility for default HouseholdUtility initial value', () => {
        const formGroup = service.createHouseholdUtilityFormGroup(sampleWithNewData);

        const householdUtility = service.getHouseholdUtility(formGroup) as any;

        expect(householdUtility).toMatchObject(sampleWithNewData);
      });

      it('should return NewHouseholdUtility for empty HouseholdUtility initial value', () => {
        const formGroup = service.createHouseholdUtilityFormGroup();

        const householdUtility = service.getHouseholdUtility(formGroup) as any;

        expect(householdUtility).toMatchObject({});
      });

      it('should return IHouseholdUtility', () => {
        const formGroup = service.createHouseholdUtilityFormGroup(sampleWithRequiredData);

        const householdUtility = service.getHouseholdUtility(formGroup) as any;

        expect(householdUtility).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHouseholdUtility should not enable id FormControl', () => {
        const formGroup = service.createHouseholdUtilityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHouseholdUtility should disable id FormControl', () => {
        const formGroup = service.createHouseholdUtilityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
