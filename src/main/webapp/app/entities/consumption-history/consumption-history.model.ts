import dayjs from 'dayjs/esm';
import { IHouseholdUtility } from 'app/entities/household-utility/household-utility.model';

export interface IConsumptionHistory {
  id: number;
  consumption?: number | null;
  cost?: number | null;
  date?: dayjs.Dayjs | null;
  householdUtility?: Pick<IHouseholdUtility, 'id' | 'name'> | null;
}

export type NewConsumptionHistory = Omit<IConsumptionHistory, 'id'> & { id: null };
