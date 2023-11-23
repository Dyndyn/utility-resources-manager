import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHouseholdUtility, NewHouseholdUtility } from '../household-utility.model';

export type PartialUpdateHouseholdUtility = Partial<IHouseholdUtility> & Pick<IHouseholdUtility, 'id'>;

export type EntityResponseType = HttpResponse<IHouseholdUtility>;
export type EntityArrayResponseType = HttpResponse<IHouseholdUtility[]>;

@Injectable({ providedIn: 'root' })
export class HouseholdUtilityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/household-utilities');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(householdUtility: NewHouseholdUtility): Observable<EntityResponseType> {
    return this.http.post<IHouseholdUtility>(this.resourceUrl, householdUtility, { observe: 'response' });
  }

  update(householdUtility: IHouseholdUtility): Observable<EntityResponseType> {
    return this.http.put<IHouseholdUtility>(
      `${this.resourceUrl}/${this.getHouseholdUtilityIdentifier(householdUtility)}`,
      householdUtility,
      { observe: 'response' },
    );
  }

  partialUpdate(householdUtility: PartialUpdateHouseholdUtility): Observable<EntityResponseType> {
    return this.http.patch<IHouseholdUtility>(
      `${this.resourceUrl}/${this.getHouseholdUtilityIdentifier(householdUtility)}`,
      householdUtility,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHouseholdUtility>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHouseholdUtility[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  generatePredictions(id: number): Observable<HttpResponse<{}>> {
    return this.http.post(`${this.resourceUrl}/${id}/consumption-predictions`, null, { observe: 'response' });
  }

  getHouseholdUtilityIdentifier(householdUtility: Pick<IHouseholdUtility, 'id'>): number {
    return householdUtility.id;
  }

  compareHouseholdUtility(o1: Pick<IHouseholdUtility, 'id'> | null, o2: Pick<IHouseholdUtility, 'id'> | null): boolean {
    return o1 && o2 ? this.getHouseholdUtilityIdentifier(o1) === this.getHouseholdUtilityIdentifier(o2) : o1 === o2;
  }

  addHouseholdUtilityToCollectionIfMissing<Type extends Pick<IHouseholdUtility, 'id'>>(
    householdUtilityCollection: Type[],
    ...householdUtilitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const householdUtilities: Type[] = householdUtilitiesToCheck.filter(isPresent);
    if (householdUtilities.length > 0) {
      const householdUtilityCollectionIdentifiers = householdUtilityCollection.map(
        householdUtilityItem => this.getHouseholdUtilityIdentifier(householdUtilityItem)!,
      );
      const householdUtilitiesToAdd = householdUtilities.filter(householdUtilityItem => {
        const householdUtilityIdentifier = this.getHouseholdUtilityIdentifier(householdUtilityItem);
        if (householdUtilityCollectionIdentifiers.includes(householdUtilityIdentifier)) {
          return false;
        }
        householdUtilityCollectionIdentifiers.push(householdUtilityIdentifier);
        return true;
      });
      return [...householdUtilitiesToAdd, ...householdUtilityCollection];
    }
    return householdUtilityCollection;
  }
}
