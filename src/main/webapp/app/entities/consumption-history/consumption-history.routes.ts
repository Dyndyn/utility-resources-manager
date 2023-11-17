import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ConsumptionHistoryComponent } from './list/consumption-history.component';
import { ConsumptionHistoryDetailComponent } from './detail/consumption-history-detail.component';
import { ConsumptionHistoryUpdateComponent } from './update/consumption-history-update.component';
import ConsumptionHistoryResolve from './route/consumption-history-routing-resolve.service';

const consumptionHistoryRoute: Routes = [
  {
    path: '',
    component: ConsumptionHistoryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConsumptionHistoryDetailComponent,
    resolve: {
      consumptionHistory: ConsumptionHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConsumptionHistoryUpdateComponent,
    resolve: {
      consumptionHistory: ConsumptionHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConsumptionHistoryUpdateComponent,
    resolve: {
      consumptionHistory: ConsumptionHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default consumptionHistoryRoute;
