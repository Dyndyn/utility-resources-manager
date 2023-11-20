import { IHouseholdUtility, NewHouseholdUtility } from './household-utility.model';

export const sampleWithRequiredData: IHouseholdUtility = {
  id: 26206,
  name: 'limply worried except',
  accountId: 'now wisely sweets',
  rate: 12655.5,
  active: false,
};

export const sampleWithPartialData: IHouseholdUtility = {
  id: 9374,
  name: 'harmonize',
  accountId: 'lunchmeat semester',
  rate: 17204.51,
  active: false,
};

export const sampleWithFullData: IHouseholdUtility = {
  id: 4478,
  name: 'dip',
  accountId: 'reassign as than',
  rate: 8680.67,
  active: true,
};

export const sampleWithNewData: NewHouseholdUtility = {
  name: 'pelican yahoo',
  accountId: 'extroverted so although',
  rate: 16312.79,
  active: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
