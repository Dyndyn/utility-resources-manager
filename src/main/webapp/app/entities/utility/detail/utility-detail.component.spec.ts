import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { UtilityDetailComponent } from './utility-detail.component';

describe('Utility Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UtilityDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: UtilityDetailComponent,
              resolve: { utility: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UtilityDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load utility on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UtilityDetailComponent);

      // THEN
      expect(instance.utility).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
