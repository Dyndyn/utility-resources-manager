import { IUtility, NewUtility } from './utility.model';

export const sampleWithRequiredData: IUtility = {
  id: 20607,
  name: 'meh until virtuous',
  constant: false,
  predictable: false,
};

export const sampleWithPartialData: IUtility = {
  id: 388,
  name: 'disunite boo',
  constant: true,
  predictable: true,
};

export const sampleWithFullData: IUtility = {
  id: 1138,
  name: 'speculation',
  constant: true,
  predictable: false,
};

export const sampleWithNewData: NewUtility = {
  name: 'cruelly',
  constant: true,
  predictable: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
