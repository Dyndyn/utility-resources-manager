import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../consumption-history.test-samples';

import { ConsumptionHistoryFormService } from './consumption-history-form.service';

describe('ConsumptionHistory Form Service', () => {
  let service: ConsumptionHistoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConsumptionHistoryFormService);
  });

  describe('Service methods', () => {
    describe('createConsumptionHistoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createConsumptionHistoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            consumption: expect.any(Object),
            cost: expect.any(Object),
            date: expect.any(Object),
            householdUtility: expect.any(Object),
          }),
        );
      });

      it('passing IConsumptionHistory should create a new form with FormGroup', () => {
        const formGroup = service.createConsumptionHistoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            consumption: expect.any(Object),
            cost: expect.any(Object),
            date: expect.any(Object),
            householdUtility: expect.any(Object),
          }),
        );
      });
    });

    describe('getConsumptionHistory', () => {
      it('should return NewConsumptionHistory for default ConsumptionHistory initial value', () => {
        const formGroup = service.createConsumptionHistoryFormGroup(sampleWithNewData);

        const consumptionHistory = service.getConsumptionHistory(formGroup) as any;

        expect(consumptionHistory).toMatchObject(sampleWithNewData);
      });

      it('should return NewConsumptionHistory for empty ConsumptionHistory initial value', () => {
        const formGroup = service.createConsumptionHistoryFormGroup();

        const consumptionHistory = service.getConsumptionHistory(formGroup) as any;

        expect(consumptionHistory).toMatchObject({});
      });

      it('should return IConsumptionHistory', () => {
        const formGroup = service.createConsumptionHistoryFormGroup(sampleWithRequiredData);

        const consumptionHistory = service.getConsumptionHistory(formGroup) as any;

        expect(consumptionHistory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IConsumptionHistory should not enable id FormControl', () => {
        const formGroup = service.createConsumptionHistoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewConsumptionHistory should disable id FormControl', () => {
        const formGroup = service.createConsumptionHistoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
