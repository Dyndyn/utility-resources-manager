import dayjs from 'dayjs/esm';

import { IConsumptionHistory, NewConsumptionHistory } from './consumption-history.model';

export const sampleWithRequiredData: IConsumptionHistory = {
  id: 18247,
  consumption: 18615.37,
};

export const sampleWithPartialData: IConsumptionHistory = {
  id: 8116,
  consumption: 7696.11,
};

export const sampleWithFullData: IConsumptionHistory = {
  id: 15876,
  consumption: 5397.39,
  date: dayjs('2023-11-17'),
};

export const sampleWithNewData: NewConsumptionHistory = {
  consumption: 7273.4,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
