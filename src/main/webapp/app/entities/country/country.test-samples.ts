import { ICountry, NewCountry } from './country.model';

export const sampleWithRequiredData: ICountry = {
  id: 5597,
  name: 'yippee',
};

export const sampleWithPartialData: ICountry = {
  id: 17930,
  name: 'winding caress',
};

export const sampleWithFullData: ICountry = {
  id: 7794,
  name: 'fort personify',
};

export const sampleWithNewData: NewCountry = {
  name: 'feast',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
