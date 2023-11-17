import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUtility } from '../utility.model';
import { UtilityService } from '../service/utility.service';

export const utilityResolve = (route: ActivatedRouteSnapshot): Observable<null | IUtility> => {
  const id = route.params['id'];
  if (id) {
    return inject(UtilityService)
      .find(id)
      .pipe(
        mergeMap((utility: HttpResponse<IUtility>) => {
          if (utility.body) {
            return of(utility.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default utilityResolve;
