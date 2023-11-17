import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHousehold } from '../household.model';
import { HouseholdService } from '../service/household.service';

export const householdResolve = (route: ActivatedRouteSnapshot): Observable<null | IHousehold> => {
  const id = route.params['id'];
  if (id) {
    return inject(HouseholdService)
      .find(id)
      .pipe(
        mergeMap((household: HttpResponse<IHousehold>) => {
          if (household.body) {
            return of(household.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default householdResolve;
