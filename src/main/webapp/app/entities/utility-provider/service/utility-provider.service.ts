import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUtilityProvider, NewUtilityProvider } from '../utility-provider.model';

export type PartialUpdateUtilityProvider = Partial<IUtilityProvider> & Pick<IUtilityProvider, 'id'>;

export type EntityResponseType = HttpResponse<IUtilityProvider>;
export type EntityArrayResponseType = HttpResponse<IUtilityProvider[]>;

@Injectable({ providedIn: 'root' })
export class UtilityProviderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/utility-providers');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(utilityProvider: NewUtilityProvider): Observable<EntityResponseType> {
    return this.http.post<IUtilityProvider>(this.resourceUrl, utilityProvider, { observe: 'response' });
  }

  update(utilityProvider: IUtilityProvider): Observable<EntityResponseType> {
    return this.http.put<IUtilityProvider>(`${this.resourceUrl}/${this.getUtilityProviderIdentifier(utilityProvider)}`, utilityProvider, {
      observe: 'response',
    });
  }

  partialUpdate(utilityProvider: PartialUpdateUtilityProvider): Observable<EntityResponseType> {
    return this.http.patch<IUtilityProvider>(`${this.resourceUrl}/${this.getUtilityProviderIdentifier(utilityProvider)}`, utilityProvider, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUtilityProvider>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUtilityProvider[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUtilityProviderIdentifier(utilityProvider: Pick<IUtilityProvider, 'id'>): number {
    return utilityProvider.id;
  }

  compareUtilityProvider(o1: Pick<IUtilityProvider, 'id'> | null, o2: Pick<IUtilityProvider, 'id'> | null): boolean {
    return o1 && o2 ? this.getUtilityProviderIdentifier(o1) === this.getUtilityProviderIdentifier(o2) : o1 === o2;
  }

  addUtilityProviderToCollectionIfMissing<Type extends Pick<IUtilityProvider, 'id'>>(
    utilityProviderCollection: Type[],
    ...utilityProvidersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const utilityProviders: Type[] = utilityProvidersToCheck.filter(isPresent);
    if (utilityProviders.length > 0) {
      const utilityProviderCollectionIdentifiers = utilityProviderCollection.map(
        utilityProviderItem => this.getUtilityProviderIdentifier(utilityProviderItem)!,
      );
      const utilityProvidersToAdd = utilityProviders.filter(utilityProviderItem => {
        const utilityProviderIdentifier = this.getUtilityProviderIdentifier(utilityProviderItem);
        if (utilityProviderCollectionIdentifiers.includes(utilityProviderIdentifier)) {
          return false;
        }
        utilityProviderCollectionIdentifiers.push(utilityProviderIdentifier);
        return true;
      });
      return [...utilityProvidersToAdd, ...utilityProviderCollection];
    }
    return utilityProviderCollection;
  }
}
