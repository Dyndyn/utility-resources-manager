import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../household.test-samples';

import { HouseholdFormService } from './household-form.service';

describe('Household Form Service', () => {
  let service: HouseholdFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HouseholdFormService);
  });

  describe('Service methods', () => {
    describe('createHouseholdFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHouseholdFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            address: expect.any(Object),
            area: expect.any(Object),
            residents: expect.any(Object),
            city: expect.any(Object),
          }),
        );
      });

      it('passing IHousehold should create a new form with FormGroup', () => {
        const formGroup = service.createHouseholdFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            address: expect.any(Object),
            area: expect.any(Object),
            residents: expect.any(Object),
            city: expect.any(Object),
          }),
        );
      });
    });

    describe('getHousehold', () => {
      it('should return NewHousehold for default Household initial value', () => {
        const formGroup = service.createHouseholdFormGroup(sampleWithNewData);

        const household = service.getHousehold(formGroup) as any;

        expect(household).toMatchObject(sampleWithNewData);
      });

      it('should return NewHousehold for empty Household initial value', () => {
        const formGroup = service.createHouseholdFormGroup();

        const household = service.getHousehold(formGroup) as any;

        expect(household).toMatchObject({});
      });

      it('should return IHousehold', () => {
        const formGroup = service.createHouseholdFormGroup(sampleWithRequiredData);

        const household = service.getHousehold(formGroup) as any;

        expect(household).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHousehold should not enable id FormControl', () => {
        const formGroup = service.createHouseholdFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHousehold should disable id FormControl', () => {
        const formGroup = service.createHouseholdFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
