import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IConsumptionHistory, NewConsumptionHistory } from '../consumption-history.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IConsumptionHistory for edit and NewConsumptionHistoryFormGroupInput for create.
 */
type ConsumptionHistoryFormGroupInput = IConsumptionHistory | PartialWithRequiredKeyOf<NewConsumptionHistory>;

type ConsumptionHistoryFormDefaults = Pick<NewConsumptionHistory, 'id'>;

type ConsumptionHistoryFormGroupContent = {
  id: FormControl<IConsumptionHistory['id'] | NewConsumptionHistory['id']>;
  consumption: FormControl<IConsumptionHistory['consumption']>;
  cost: FormControl<IConsumptionHistory['cost']>;
  date: FormControl<IConsumptionHistory['date']>;
  householdUtility: FormControl<IConsumptionHistory['householdUtility']>;
};

export type ConsumptionHistoryFormGroup = FormGroup<ConsumptionHistoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ConsumptionHistoryFormService {
  createConsumptionHistoryFormGroup(consumptionHistory: ConsumptionHistoryFormGroupInput = { id: null }): ConsumptionHistoryFormGroup {
    const consumptionHistoryRawValue = {
      ...this.getFormDefaults(),
      ...consumptionHistory,
    };
    return new FormGroup<ConsumptionHistoryFormGroupContent>({
      id: new FormControl(
        { value: consumptionHistoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      consumption: new FormControl(consumptionHistoryRawValue.consumption, {
        validators: [Validators.required],
      }),
      cost: new FormControl(consumptionHistoryRawValue.cost),
      date: new FormControl(consumptionHistoryRawValue.date, {
        validators: [Validators.required],
      }),
      householdUtility: new FormControl(consumptionHistoryRawValue.householdUtility, {
        validators: [Validators.required],
      }),
    });
  }

  getConsumptionHistory(form: ConsumptionHistoryFormGroup): IConsumptionHistory | NewConsumptionHistory {
    return form.getRawValue() as IConsumptionHistory | NewConsumptionHistory;
  }

  resetForm(form: ConsumptionHistoryFormGroup, consumptionHistory: ConsumptionHistoryFormGroupInput): void {
    const consumptionHistoryRawValue = { ...this.getFormDefaults(), ...consumptionHistory };
    form.reset(
      {
        ...consumptionHistoryRawValue,
        id: { value: consumptionHistoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ConsumptionHistoryFormDefaults {
    return {
      id: null,
    };
  }
}
