import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { HouseholdUtilityDetailComponent } from './household-utility-detail.component';
import { HouseholdUtilityService } from '../service/household-utility.service';

describe('HouseholdUtility Management Detail Component', () => {
  let service: HouseholdUtilityService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HouseholdUtilityDetailComponent,
        HttpClientTestingModule,
        RouterTestingModule.withRoutes([], { bindToComponentInputs: true }),
      ],
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
    service = TestBed.inject(HouseholdUtilityService);
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
