import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { HouseholdDetailComponent } from './household-detail.component';

describe('Household Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HouseholdDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: HouseholdDetailComponent,
              resolve: { household: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(HouseholdDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load household on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', HouseholdDetailComponent);

      // THEN
      expect(instance.household).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
