import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHouseholdUtility } from '../household-utility.model';
import { HouseholdUtilityService } from '../service/household-utility.service';

export const householdUtilityResolve = (route: ActivatedRouteSnapshot): Observable<null | IHouseholdUtility> => {
  const id = route.params['id'];
  if (id) {
    return inject(HouseholdUtilityService)
      .find(id)
      .pipe(
        mergeMap((householdUtility: HttpResponse<IHouseholdUtility>) => {
          if (householdUtility.body) {
            return of(householdUtility.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default householdUtilityResolve;
