import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../utility.test-samples';

import { UtilityFormService } from './utility-form.service';

describe('Utility Form Service', () => {
  let service: UtilityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UtilityFormService);
  });

  describe('Service methods', () => {
    describe('createUtilityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUtilityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            constant: expect.any(Object),
            predictable: expect.any(Object),
          }),
        );
      });

      it('passing IUtility should create a new form with FormGroup', () => {
        const formGroup = service.createUtilityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            constant: expect.any(Object),
            predictable: expect.any(Object),
          }),
        );
      });
    });

    describe('getUtility', () => {
      it('should return NewUtility for default Utility initial value', () => {
        const formGroup = service.createUtilityFormGroup(sampleWithNewData);

        const utility = service.getUtility(formGroup) as any;

        expect(utility).toMatchObject(sampleWithNewData);
      });

      it('should return NewUtility for empty Utility initial value', () => {
        const formGroup = service.createUtilityFormGroup();

        const utility = service.getUtility(formGroup) as any;

        expect(utility).toMatchObject({});
      });

      it('should return IUtility', () => {
        const formGroup = service.createUtilityFormGroup(sampleWithRequiredData);

        const utility = service.getUtility(formGroup) as any;

        expect(utility).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUtility should not enable id FormControl', () => {
        const formGroup = service.createUtilityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUtility should disable id FormControl', () => {
        const formGroup = service.createUtilityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
