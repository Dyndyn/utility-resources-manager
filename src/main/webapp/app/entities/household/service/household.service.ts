import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHousehold, NewHousehold } from '../household.model';

export type PartialUpdateHousehold = Partial<IHousehold> & Pick<IHousehold, 'id'>;

export type EntityResponseType = HttpResponse<IHousehold>;
export type EntityArrayResponseType = HttpResponse<IHousehold[]>;

@Injectable({ providedIn: 'root' })
export class HouseholdService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/households');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(household: NewHousehold): Observable<EntityResponseType> {
    return this.http.post<IHousehold>(this.resourceUrl, household, { observe: 'response' });
  }

  update(household: IHousehold): Observable<EntityResponseType> {
    return this.http.put<IHousehold>(`${this.resourceUrl}/${this.getHouseholdIdentifier(household)}`, household, { observe: 'response' });
  }

  partialUpdate(household: PartialUpdateHousehold): Observable<EntityResponseType> {
    return this.http.patch<IHousehold>(`${this.resourceUrl}/${this.getHouseholdIdentifier(household)}`, household, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHousehold>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHousehold[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getHouseholdIdentifier(household: Pick<IHousehold, 'id'>): number {
    return household.id;
  }

  compareHousehold(o1: Pick<IHousehold, 'id'> | null, o2: Pick<IHousehold, 'id'> | null): boolean {
    return o1 && o2 ? this.getHouseholdIdentifier(o1) === this.getHouseholdIdentifier(o2) : o1 === o2;
  }

  addHouseholdToCollectionIfMissing<Type extends Pick<IHousehold, 'id'>>(
    householdCollection: Type[],
    ...householdsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const households: Type[] = householdsToCheck.filter(isPresent);
    if (households.length > 0) {
      const householdCollectionIdentifiers = householdCollection.map(householdItem => this.getHouseholdIdentifier(householdItem)!);
      const householdsToAdd = households.filter(householdItem => {
        const householdIdentifier = this.getHouseholdIdentifier(householdItem);
        if (householdCollectionIdentifiers.includes(householdIdentifier)) {
          return false;
        }
        householdCollectionIdentifiers.push(householdIdentifier);
        return true;
      });
      return [...householdsToAdd, ...householdCollection];
    }
    return householdCollection;
  }
}
