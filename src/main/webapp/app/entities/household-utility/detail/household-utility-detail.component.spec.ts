import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { HouseholdUtilityDetailComponent } from './household-utility-detail.component';

describe('HouseholdUtility Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HouseholdUtilityDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: HouseholdUtilityDetailComponent,
              resolve: { householdUtility: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(HouseholdUtilityDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load householdUtility on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', HouseholdUtilityDetailComponent);

      // THEN
      expect(instance.householdUtility).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
