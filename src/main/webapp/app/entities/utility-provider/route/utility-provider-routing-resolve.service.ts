import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUtilityProvider } from '../utility-provider.model';
import { UtilityProviderService } from '../service/utility-provider.service';

export const utilityProviderResolve = (route: ActivatedRouteSnapshot): Observable<null | IUtilityProvider> => {
  const id = route.params['id'];
  if (id) {
    return inject(UtilityProviderService)
      .find(id)
      .pipe(
        mergeMap((utilityProvider: HttpResponse<IUtilityProvider>) => {
          if (utilityProvider.body) {
            return of(utilityProvider.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default utilityProviderResolve;
