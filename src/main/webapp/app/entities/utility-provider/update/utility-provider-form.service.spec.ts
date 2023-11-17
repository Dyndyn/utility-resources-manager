import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../utility-provider.test-samples';

import { UtilityProviderFormService } from './utility-provider-form.service';

describe('UtilityProvider Form Service', () => {
  let service: UtilityProviderFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UtilityProviderFormService);
  });

  describe('Service methods', () => {
    describe('createUtilityProviderFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUtilityProviderFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            iban: expect.any(Object),
            usreou: expect.any(Object),
            rate: expect.any(Object),
            utility: expect.any(Object),
          }),
        );
      });

      it('passing IUtilityProvider should create a new form with FormGroup', () => {
        const formGroup = service.createUtilityProviderFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            iban: expect.any(Object),
            usreou: expect.any(Object),
            rate: expect.any(Object),
            utility: expect.any(Object),
          }),
        );
      });
    });

    describe('getUtilityProvider', () => {
      it('should return NewUtilityProvider for default UtilityProvider initial value', () => {
        const formGroup = service.createUtilityProviderFormGroup(sampleWithNewData);

        const utilityProvider = service.getUtilityProvider(formGroup) as any;

        expect(utilityProvider).toMatchObject(sampleWithNewData);
      });

      it('should return NewUtilityProvider for empty UtilityProvider initial value', () => {
        const formGroup = service.createUtilityProviderFormGroup();

        const utilityProvider = service.getUtilityProvider(formGroup) as any;

        expect(utilityProvider).toMatchObject({});
      });

      it('should return IUtilityProvider', () => {
        const formGroup = service.createUtilityProviderFormGroup(sampleWithRequiredData);

        const utilityProvider = service.getUtilityProvider(formGroup) as any;

        expect(utilityProvider).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUtilityProvider should not enable id FormControl', () => {
        const formGroup = service.createUtilityProviderFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUtilityProvider should disable id FormControl', () => {
        const formGroup = service.createUtilityProviderFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
