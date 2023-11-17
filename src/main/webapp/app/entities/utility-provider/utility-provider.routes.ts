import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { UtilityProviderComponent } from './list/utility-provider.component';
import { UtilityProviderDetailComponent } from './detail/utility-provider-detail.component';
import { UtilityProviderUpdateComponent } from './update/utility-provider-update.component';
import UtilityProviderResolve from './route/utility-provider-routing-resolve.service';

const utilityProviderRoute: Routes = [
  {
    path: '',
    component: UtilityProviderComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UtilityProviderDetailComponent,
    resolve: {
      utilityProvider: UtilityProviderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UtilityProviderUpdateComponent,
    resolve: {
      utilityProvider: UtilityProviderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UtilityProviderUpdateComponent,
    resolve: {
      utilityProvider: UtilityProviderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default utilityProviderRoute;
