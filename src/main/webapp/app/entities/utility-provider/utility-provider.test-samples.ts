import { IUtilityProvider, NewUtilityProvider } from './utility-provider.model';

export const sampleWithRequiredData: IUtilityProvider = {
  id: 5117,
  name: 'despite fondly since',
};

export const sampleWithPartialData: IUtilityProvider = {
  id: 2309,
  name: 'artery',
};

export const sampleWithFullData: IUtilityProvider = {
  id: 11428,
  name: 'which',
  iban: 'MD6168J0Q7992813G301KQ06',
  usreou: 'likewise',
};

export const sampleWithNewData: NewUtilityProvider = {
  name: 'excepting along',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
