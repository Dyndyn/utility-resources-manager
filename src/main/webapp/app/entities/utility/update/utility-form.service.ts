import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUtility, NewUtility } from '../utility.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUtility for edit and NewUtilityFormGroupInput for create.
 */
type UtilityFormGroupInput = IUtility | PartialWithRequiredKeyOf<NewUtility>;

type UtilityFormDefaults = Pick<NewUtility, 'id' | 'constant' | 'predictable'>;

type UtilityFormGroupContent = {
  id: FormControl<IUtility['id'] | NewUtility['id']>;
  name: FormControl<IUtility['name']>;
  constant: FormControl<IUtility['constant']>;
  predictable: FormControl<IUtility['predictable']>;
};

export type UtilityFormGroup = FormGroup<UtilityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UtilityFormService {
  createUtilityFormGroup(utility: UtilityFormGroupInput = { id: null }): UtilityFormGroup {
    const utilityRawValue = {
      ...this.getFormDefaults(),
      ...utility,
    };
    return new FormGroup<UtilityFormGroupContent>({
      id: new FormControl(
        { value: utilityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(utilityRawValue.name, {
        validators: [Validators.required],
      }),
      constant: new FormControl(utilityRawValue.constant, {
        validators: [Validators.required],
      }),
      predictable: new FormControl(utilityRawValue.predictable, {
        validators: [Validators.required],
      }),
    });
  }

  getUtility(form: UtilityFormGroup): IUtility | NewUtility {
    return form.getRawValue() as IUtility | NewUtility;
  }

  resetForm(form: UtilityFormGroup, utility: UtilityFormGroupInput): void {
    const utilityRawValue = { ...this.getFormDefaults(), ...utility };
    form.reset(
      {
        ...utilityRawValue,
        id: { value: utilityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UtilityFormDefaults {
    return {
      id: null,
      constant: false,
      predictable: false,
    };
  }
}
