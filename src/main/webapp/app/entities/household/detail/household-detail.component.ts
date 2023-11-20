import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { LineChartComponent } from 'app/entities/chart/household-utility-chart.component';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IHousehold } from '../household.model';

@Component({
  standalone: true,
  selector: 'jhi-household-detail',
  templateUrl: './household-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, LineChartComponent],
})
export class HouseholdDetailComponent {
  @Input() household: IHousehold | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
