import { IHousehold } from 'app/entities/household/household.model';
import { IUtilityProvider } from 'app/entities/utility-provider/utility-provider.model';

export interface GraphData {
  month: string[] | [];
  data: number[] | [];
}

export interface IHouseholdUtility {
  id: number;
  name?: string | null;
  accountId?: string | null;
  rate?: number | null;
  active?: boolean | null;
  household?: Pick<IHousehold, 'id' | 'address'> | null;
  utilityProvider?: Pick<IUtilityProvider, 'id' | 'name'> | null;
  consumption?: GraphData | null;
  predictedConsumption?: GraphData | null;
  cost?: GraphData | null;
  predictedCost?: GraphData | null;
}

export type NewHouseholdUtility = Omit<IHouseholdUtility, 'id'> & { id: null };
