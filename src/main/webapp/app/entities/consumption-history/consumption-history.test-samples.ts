import dayjs from 'dayjs/esm';

import { IConsumptionHistory, NewConsumptionHistory } from './consumption-history.model';

export const sampleWithRequiredData: IConsumptionHistory = {
  id: 20990,
  consumption: 17666.78,
  date: dayjs('2023-11-16'),
};

export const sampleWithPartialData: IConsumptionHistory = {
  id: 5368,
  consumption: 5372.92,
  cost: 3527.12,
  date: dayjs('2023-11-17'),
};

export const sampleWithFullData: IConsumptionHistory = {
  id: 116,
  consumption: 8394.92,
  cost: 9361.09,
  date: dayjs('2023-11-17'),
};

export const sampleWithNewData: NewConsumptionHistory = {
  consumption: 12807.41,
  date: dayjs('2023-11-16'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
