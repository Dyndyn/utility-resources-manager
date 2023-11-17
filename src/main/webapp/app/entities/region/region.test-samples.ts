import { IRegion, NewRegion } from './region.model';

export const sampleWithRequiredData: IRegion = {
  id: 29003,
  name: 'disembowel remote',
};

export const sampleWithPartialData: IRegion = {
  id: 4333,
  name: 'impractical closely',
};

export const sampleWithFullData: IRegion = {
  id: 1149,
  name: 'acquiesce anti',
};

export const sampleWithNewData: NewRegion = {
  name: 'for',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
