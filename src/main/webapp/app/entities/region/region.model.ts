import { ICountry } from 'app/entities/country/country.model';

export interface IRegion {
  id: number;
  name?: string | null;
  country?: Pick<ICountry, 'id' | 'name'> | null;
}

export type NewRegion = Omit<IRegion, 'id'> & { id: null };
