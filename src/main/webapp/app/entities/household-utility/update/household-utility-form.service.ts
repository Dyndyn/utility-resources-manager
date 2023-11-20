import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IHouseholdUtility, NewHouseholdUtility } from '../household-utility.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHouseholdUtility for edit and NewHouseholdUtilityFormGroupInput for create.
 */
type HouseholdUtilityFormGroupInput = IHouseholdUtility | PartialWithRequiredKeyOf<NewHouseholdUtility>;

type HouseholdUtilityFormDefaults = Pick<NewHouseholdUtility, 'id' | 'active'>;

type HouseholdUtilityFormGroupContent = {
  id: FormControl<IHouseholdUtility['id'] | NewHouseholdUtility['id']>;
  name: FormControl<IHouseholdUtility['name']>;
  accountId: FormControl<IHouseholdUtility['accountId']>;
  rate: FormControl<IHouseholdUtility['rate']>;
  active: FormControl<IHouseholdUtility['active']>;
  household: FormControl<IHouseholdUtility['household']>;
  utilityProvider: FormControl<IHouseholdUtility['utilityProvider']>;
};

export type HouseholdUtilityFormGroup = FormGroup<HouseholdUtilityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HouseholdUtilityFormService {
  createHouseholdUtilityFormGroup(householdUtility: HouseholdUtilityFormGroupInput = { id: null }): HouseholdUtilityFormGroup {
    const householdUtilityRawValue = {
      ...this.getFormDefaults(),
      ...householdUtility,
    };
    return new FormGroup<HouseholdUtilityFormGroupContent>({
      id: new FormControl(
        { value: householdUtilityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(householdUtilityRawValue.name, {
        validators: [Validators.required],
      }),
      accountId: new FormControl(householdUtilityRawValue.accountId, {
        validators: [Validators.required],
      }),
      rate: new FormControl(householdUtilityRawValue.rate, {
        validators: [Validators.required],
      }),
      active: new FormControl(householdUtilityRawValue.active, {
        validators: [Validators.required],
      }),
      household: new FormControl(householdUtilityRawValue.household, {
        validators: [Validators.required],
      }),
      utilityProvider: new FormControl(householdUtilityRawValue.utilityProvider, {
        validators: [Validators.required],
      }),
    });
  }

  getHouseholdUtility(form: HouseholdUtilityFormGroup): IHouseholdUtility | NewHouseholdUtility {
    return form.getRawValue() as IHouseholdUtility | NewHouseholdUtility;
  }

  resetForm(form: HouseholdUtilityFormGroup, householdUtility: HouseholdUtilityFormGroupInput): void {
    const householdUtilityRawValue = { ...this.getFormDefaults(), ...householdUtility };
    form.reset(
      {
        ...householdUtilityRawValue,
        id: { value: householdUtilityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): HouseholdUtilityFormDefaults {
    return {
      id: null,
      active: false,
    };
  }
}
