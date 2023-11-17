import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConsumptionHistory, NewConsumptionHistory } from '../consumption-history.model';

export type PartialUpdateConsumptionHistory = Partial<IConsumptionHistory> & Pick<IConsumptionHistory, 'id'>;

type RestOf<T extends IConsumptionHistory | NewConsumptionHistory> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestConsumptionHistory = RestOf<IConsumptionHistory>;

export type NewRestConsumptionHistory = RestOf<NewConsumptionHistory>;

export type PartialUpdateRestConsumptionHistory = RestOf<PartialUpdateConsumptionHistory>;

export type EntityResponseType = HttpResponse<IConsumptionHistory>;
export type EntityArrayResponseType = HttpResponse<IConsumptionHistory[]>;

@Injectable({ providedIn: 'root' })
export class ConsumptionHistoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/consumption-histories');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(consumptionHistory: NewConsumptionHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(consumptionHistory);
    return this.http
      .post<RestConsumptionHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(consumptionHistory: IConsumptionHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(consumptionHistory);
    return this.http
      .put<RestConsumptionHistory>(`${this.resourceUrl}/${this.getConsumptionHistoryIdentifier(consumptionHistory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(consumptionHistory: PartialUpdateConsumptionHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(consumptionHistory);
    return this.http
      .patch<RestConsumptionHistory>(`${this.resourceUrl}/${this.getConsumptionHistoryIdentifier(consumptionHistory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestConsumptionHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestConsumptionHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getConsumptionHistoryIdentifier(consumptionHistory: Pick<IConsumptionHistory, 'id'>): number {
    return consumptionHistory.id;
  }

  compareConsumptionHistory(o1: Pick<IConsumptionHistory, 'id'> | null, o2: Pick<IConsumptionHistory, 'id'> | null): boolean {
    return o1 && o2 ? this.getConsumptionHistoryIdentifier(o1) === this.getConsumptionHistoryIdentifier(o2) : o1 === o2;
  }

  addConsumptionHistoryToCollectionIfMissing<Type extends Pick<IConsumptionHistory, 'id'>>(
    consumptionHistoryCollection: Type[],
    ...consumptionHistoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const consumptionHistories: Type[] = consumptionHistoriesToCheck.filter(isPresent);
    if (consumptionHistories.length > 0) {
      const consumptionHistoryCollectionIdentifiers = consumptionHistoryCollection.map(
        consumptionHistoryItem => this.getConsumptionHistoryIdentifier(consumptionHistoryItem)!,
      );
      const consumptionHistoriesToAdd = consumptionHistories.filter(consumptionHistoryItem => {
        const consumptionHistoryIdentifier = this.getConsumptionHistoryIdentifier(consumptionHistoryItem);
        if (consumptionHistoryCollectionIdentifiers.includes(consumptionHistoryIdentifier)) {
          return false;
        }
        consumptionHistoryCollectionIdentifiers.push(consumptionHistoryIdentifier);
        return true;
      });
      return [...consumptionHistoriesToAdd, ...consumptionHistoryCollection];
    }
    return consumptionHistoryCollection;
  }

  protected convertDateFromClient<T extends IConsumptionHistory | NewConsumptionHistory | PartialUpdateConsumptionHistory>(
    consumptionHistory: T,
  ): RestOf<T> {
    return {
      ...consumptionHistory,
      date: consumptionHistory.date?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restConsumptionHistory: RestConsumptionHistory): IConsumptionHistory {
    return {
      ...restConsumptionHistory,
      date: restConsumptionHistory.date ? dayjs(restConsumptionHistory.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestConsumptionHistory>): HttpResponse<IConsumptionHistory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestConsumptionHistory[]>): HttpResponse<IConsumptionHistory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
