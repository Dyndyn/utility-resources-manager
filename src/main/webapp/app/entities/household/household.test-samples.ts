import { IHousehold, NewHousehold } from './household.model';

export const sampleWithRequiredData: IHousehold = {
  id: 6902,
  address: 'even',
  area: 25977.1,
  residents: 26415,
};

export const sampleWithPartialData: IHousehold = {
  id: 15189,
  address: 'dense testimonial',
  area: 17160.49,
  residents: 16972,
};

export const sampleWithFullData: IHousehold = {
  id: 30449,
  address: 'although',
  area: 28601.82,
  residents: 32613,
};

export const sampleWithNewData: NewHousehold = {
  address: 'psst emanate daring',
  area: 24013.14,
  residents: 6793,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
