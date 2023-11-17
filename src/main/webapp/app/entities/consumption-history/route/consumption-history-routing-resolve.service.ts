import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConsumptionHistory } from '../consumption-history.model';
import { ConsumptionHistoryService } from '../service/consumption-history.service';

export const consumptionHistoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IConsumptionHistory> => {
  const id = route.params['id'];
  if (id) {
    return inject(ConsumptionHistoryService)
      .find(id)
      .pipe(
        mergeMap((consumptionHistory: HttpResponse<IConsumptionHistory>) => {
          if (consumptionHistory.body) {
            return of(consumptionHistory.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default consumptionHistoryResolve;
