import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'city',
        data: { pageTitle: 'utilityResourcesManagerApp.city.home.title' },
        loadChildren: () => import('./city/city.routes'),
      },
      {
        path: 'region',
        data: { pageTitle: 'utilityResourcesManagerApp.region.home.title' },
        loadChildren: () => import('./region/region.routes'),
      },
      {
        path: 'country',
        data: { pageTitle: 'utilityResourcesManagerApp.country.home.title' },
        loadChildren: () => import('./country/country.routes'),
      },
      {
        path: 'utility',
        data: { pageTitle: 'utilityResourcesManagerApp.utility.home.title' },
        loadChildren: () => import('./utility/utility.routes'),
      },
      {
        path: 'utility-provider',
        data: { pageTitle: 'utilityResourcesManagerApp.utilityProvider.home.title' },
        loadChildren: () => import('./utility-provider/utility-provider.routes'),
      },
      {
        path: 'household',
        data: { pageTitle: 'utilityResourcesManagerApp.household.home.title' },
        loadChildren: () => import('./household/household.routes'),
      },
      {
        path: 'household-utility',
        data: { pageTitle: 'utilityResourcesManagerApp.householdUtility.home.title' },
        loadChildren: () => import('./household-utility/household-utility.routes'),
      },
      {
        path: 'consumption-history',
        data: { pageTitle: 'utilityResourcesManagerApp.consumptionHistory.home.title' },
        loadChildren: () => import('./consumption-history/consumption-history.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
