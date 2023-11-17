import { ICity, NewCity } from './city.model';

export const sampleWithRequiredData: ICity = {
  id: 15838,
  name: 'palatable instead',
};

export const sampleWithPartialData: ICity = {
  id: 27805,
  name: 'unto',
};

export const sampleWithFullData: ICity = {
  id: 32502,
  name: 'slow times recklessly',
};

export const sampleWithNewData: NewCity = {
  name: 'opposite mostly antechamber',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
