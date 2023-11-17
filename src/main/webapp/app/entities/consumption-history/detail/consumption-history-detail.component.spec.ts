import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ConsumptionHistoryDetailComponent } from './consumption-history-detail.component';

describe('ConsumptionHistory Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConsumptionHistoryDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ConsumptionHistoryDetailComponent,
              resolve: { consumptionHistory: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ConsumptionHistoryDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load consumptionHistory on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ConsumptionHistoryDetailComponent);

      // THEN
      expect(instance.consumptionHistory).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
