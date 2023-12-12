import { IUser } from 'app/entities/user/user.model';
import { ICity } from 'app/entities/city/city.model';
import { GraphData } from '../household-utility/household-utility.model';

export interface IHousehold {
  id: number;
  address?: string | null;
  area?: number | null;
  residents?: number | null;
  users?: Pick<IUser, 'id' | 'firstName' | 'lastName'>[] | null;
  city?: Pick<ICity, 'id' | 'name'> | null;
  costs?: GraphData | null;
  predictedCosts?: GraphData | null;
}

export type NewHousehold = Omit<IHousehold, 'id'> & { id: null };
