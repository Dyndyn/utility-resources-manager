import { IHouseholdUtility, NewHouseholdUtility } from './household-utility.model';

export const sampleWithRequiredData: IHouseholdUtility = {
  id: 3151,
  name: 'award regale oof',
  accountId: 'beyond justly',
  active: false,
};

export const sampleWithPartialData: IHouseholdUtility = {
  id: 15935,
  name: 'retrench against',
  accountId: 'outside likewise',
  active: true,
};

export const sampleWithFullData: IHouseholdUtility = {
  id: 25770,
  name: 'whether laboratory',
  accountId: 'except verge homestead',
  active: false,
};

export const sampleWithNewData: NewHouseholdUtility = {
  name: 'jaggedly hoon',
  accountId: 'afore',
  active: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
