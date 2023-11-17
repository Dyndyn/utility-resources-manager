import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { UtilityProviderDetailComponent } from './utility-provider-detail.component';

describe('UtilityProvider Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UtilityProviderDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: UtilityProviderDetailComponent,
              resolve: { utilityProvider: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UtilityProviderDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load utilityProvider on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UtilityProviderDetailComponent);

      // THEN
      expect(instance.utilityProvider).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
