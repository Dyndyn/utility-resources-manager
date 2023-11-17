import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { HouseholdComponent } from './list/household.component';
import { HouseholdDetailComponent } from './detail/household-detail.component';
import { HouseholdUpdateComponent } from './update/household-update.component';
import HouseholdResolve from './route/household-routing-resolve.service';

const householdRoute: Routes = [
  {
    path: '',
    component: HouseholdComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HouseholdDetailComponent,
    resolve: {
      household: HouseholdResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HouseholdUpdateComponent,
    resolve: {
      household: HouseholdResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HouseholdUpdateComponent,
    resolve: {
      household: HouseholdResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default householdRoute;
