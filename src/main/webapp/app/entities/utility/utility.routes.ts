import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { UtilityComponent } from './list/utility.component';
import { UtilityDetailComponent } from './detail/utility-detail.component';
import { UtilityUpdateComponent } from './update/utility-update.component';
import UtilityResolve from './route/utility-routing-resolve.service';

const utilityRoute: Routes = [
  {
    path: '',
    component: UtilityComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UtilityDetailComponent,
    resolve: {
      utility: UtilityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UtilityUpdateComponent,
    resolve: {
      utility: UtilityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UtilityUpdateComponent,
    resolve: {
      utility: UtilityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default utilityRoute;
