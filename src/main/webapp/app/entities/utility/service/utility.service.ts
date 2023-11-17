import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUtility, NewUtility } from '../utility.model';

export type PartialUpdateUtility = Partial<IUtility> & Pick<IUtility, 'id'>;

export type EntityResponseType = HttpResponse<IUtility>;
export type EntityArrayResponseType = HttpResponse<IUtility[]>;

@Injectable({ providedIn: 'root' })
export class UtilityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/utilities');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(utility: NewUtility): Observable<EntityResponseType> {
    return this.http.post<IUtility>(this.resourceUrl, utility, { observe: 'response' });
  }

  update(utility: IUtility): Observable<EntityResponseType> {
    return this.http.put<IUtility>(`${this.resourceUrl}/${this.getUtilityIdentifier(utility)}`, utility, { observe: 'response' });
  }

  partialUpdate(utility: PartialUpdateUtility): Observable<EntityResponseType> {
    return this.http.patch<IUtility>(`${this.resourceUrl}/${this.getUtilityIdentifier(utility)}`, utility, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUtility>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUtility[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUtilityIdentifier(utility: Pick<IUtility, 'id'>): number {
    return utility.id;
  }

  compareUtility(o1: Pick<IUtility, 'id'> | null, o2: Pick<IUtility, 'id'> | null): boolean {
    return o1 && o2 ? this.getUtilityIdentifier(o1) === this.getUtilityIdentifier(o2) : o1 === o2;
  }

  addUtilityToCollectionIfMissing<Type extends Pick<IUtility, 'id'>>(
    utilityCollection: Type[],
    ...utilitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const utilities: Type[] = utilitiesToCheck.filter(isPresent);
    if (utilities.length > 0) {
      const utilityCollectionIdentifiers = utilityCollection.map(utilityItem => this.getUtilityIdentifier(utilityItem)!);
      const utilitiesToAdd = utilities.filter(utilityItem => {
        const utilityIdentifier = this.getUtilityIdentifier(utilityItem);
        if (utilityCollectionIdentifiers.includes(utilityIdentifier)) {
          return false;
        }
        utilityCollectionIdentifiers.push(utilityIdentifier);
        return true;
      });
      return [...utilitiesToAdd, ...utilityCollection];
    }
    return utilityCollection;
  }
}
