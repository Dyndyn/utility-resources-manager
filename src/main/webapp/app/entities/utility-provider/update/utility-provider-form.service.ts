import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUtilityProvider, NewUtilityProvider } from '../utility-provider.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUtilityProvider for edit and NewUtilityProviderFormGroupInput for create.
 */
type UtilityProviderFormGroupInput = IUtilityProvider | PartialWithRequiredKeyOf<NewUtilityProvider>;

type UtilityProviderFormDefaults = Pick<NewUtilityProvider, 'id'>;

type UtilityProviderFormGroupContent = {
  id: FormControl<IUtilityProvider['id'] | NewUtilityProvider['id']>;
  name: FormControl<IUtilityProvider['name']>;
  iban: FormControl<IUtilityProvider['iban']>;
  usreou: FormControl<IUtilityProvider['usreou']>;
  rate: FormControl<IUtilityProvider['rate']>;
  utility: FormControl<IUtilityProvider['utility']>;
};

export type UtilityProviderFormGroup = FormGroup<UtilityProviderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UtilityProviderFormService {
  createUtilityProviderFormGroup(utilityProvider: UtilityProviderFormGroupInput = { id: null }): UtilityProviderFormGroup {
    const utilityProviderRawValue = {
      ...this.getFormDefaults(),
      ...utilityProvider,
    };
    return new FormGroup<UtilityProviderFormGroupContent>({
      id: new FormControl(
        { value: utilityProviderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(utilityProviderRawValue.name, {
        validators: [Validators.required],
      }),
      iban: new FormControl(utilityProviderRawValue.iban),
      usreou: new FormControl(utilityProviderRawValue.usreou),
      rate: new FormControl(utilityProviderRawValue.rate, {
        validators: [Validators.required],
      }),
      utility: new FormControl(utilityProviderRawValue.utility, {
        validators: [Validators.required],
      }),
    });
  }

  getUtilityProvider(form: UtilityProviderFormGroup): IUtilityProvider | NewUtilityProvider {
    return form.getRawValue() as IUtilityProvider | NewUtilityProvider;
  }

  resetForm(form: UtilityProviderFormGroup, utilityProvider: UtilityProviderFormGroupInput): void {
    const utilityProviderRawValue = { ...this.getFormDefaults(), ...utilityProvider };
    form.reset(
      {
        ...utilityProviderRawValue,
        id: { value: utilityProviderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UtilityProviderFormDefaults {
    return {
      id: null,
    };
  }
}
