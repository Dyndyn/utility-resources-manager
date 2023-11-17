import { IUtilityProvider, NewUtilityProvider } from './utility-provider.model';

export const sampleWithRequiredData: IUtilityProvider = {
  id: 28415,
  name: 'toggle',
  rate: 21171.17,
};

export const sampleWithPartialData: IUtilityProvider = {
  id: 25812,
  name: 'photodiode',
  iban: 'QA76TVBG4083295835X0954BI6B39',
  rate: 8377.32,
};

export const sampleWithFullData: IUtilityProvider = {
  id: 21379,
  name: 'unaccountably supposing',
  iban: 'GI50ETAT549353803996880',
  usreou: 'reorganisation promptly',
  rate: 18034.46,
};

export const sampleWithNewData: NewUtilityProvider = {
  name: 'frenetically mole austere',
  rate: 18493.01,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
