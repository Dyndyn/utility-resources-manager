import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { HouseholdUtilityComponent } from './list/household-utility.component';
import { HouseholdUtilityDetailComponent } from './detail/household-utility-detail.component';
import { HouseholdUtilityUpdateComponent } from './update/household-utility-update.component';
import HouseholdUtilityResolve from './route/household-utility-routing-resolve.service';

const householdUtilityRoute: Routes = [
  {
    path: '',
    component: HouseholdUtilityComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HouseholdUtilityDetailComponent,
    resolve: {
      householdUtility: HouseholdUtilityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HouseholdUtilityUpdateComponent,
    resolve: {
      householdUtility: HouseholdUtilityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HouseholdUtilityUpdateComponent,
    resolve: {
      householdUtility: HouseholdUtilityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default householdUtilityRoute;
