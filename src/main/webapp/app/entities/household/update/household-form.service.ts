import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IHousehold, NewHousehold } from '../household.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHousehold for edit and NewHouseholdFormGroupInput for create.
 */
type HouseholdFormGroupInput = IHousehold | PartialWithRequiredKeyOf<NewHousehold>;

type HouseholdFormDefaults = Pick<NewHousehold, 'id' | 'users'>;

type HouseholdFormGroupContent = {
  id: FormControl<IHousehold['id'] | NewHousehold['id']>;
  address: FormControl<IHousehold['address']>;
  area: FormControl<IHousehold['area']>;
  residents: FormControl<IHousehold['residents']>;
  users: FormControl<IHousehold['users']>;
  city: FormControl<IHousehold['city']>;
};

export type HouseholdFormGroup = FormGroup<HouseholdFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HouseholdFormService {
  createHouseholdFormGroup(household: HouseholdFormGroupInput = { id: null }): HouseholdFormGroup {
    const householdRawValue = {
      ...this.getFormDefaults(),
      ...household,
    };
    return new FormGroup<HouseholdFormGroupContent>({
      id: new FormControl(
        { value: householdRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      address: new FormControl(householdRawValue.address, {
        validators: [Validators.required],
      }),
      area: new FormControl(householdRawValue.area, {
        validators: [Validators.required],
      }),
      residents: new FormControl(householdRawValue.residents, {
        validators: [Validators.required],
      }),
      users: new FormControl(householdRawValue.users ?? []),
      city: new FormControl(householdRawValue.city, {
        validators: [Validators.required],
      }),
    });
  }

  getHousehold(form: HouseholdFormGroup): IHousehold | NewHousehold {
    return form.getRawValue() as IHousehold | NewHousehold;
  }

  resetForm(form: HouseholdFormGroup, household: HouseholdFormGroupInput): void {
    const householdRawValue = { ...this.getFormDefaults(), ...household };
    form.reset(
      {
        ...householdRawValue,
        id: { value: householdRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): HouseholdFormDefaults {
    return {
      id: null,
      users: [],
    };
  }
}
