import { IUtility } from 'app/entities/utility/utility.model';

export interface IUtilityProvider {
  id: number;
  name?: string | null;
  iban?: string | null;
  usreou?: string | null;
  utility?: Pick<IUtility, 'id' | 'name'> | null;
}

export type NewUtilityProvider = Omit<IUtilityProvider, 'id'> & { id: null };
