import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { LineChartComponent } from 'app/entities/household-utility/chart/household-utility-chart.component';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IHouseholdUtility } from '../household-utility.model';

@Component({
  standalone: true,
  selector: 'jhi-household-utility-detail',
  templateUrl: './household-utility-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, LineChartComponent],
})
export class HouseholdUtilityDetailComponent {
  @Input() householdUtility: IHouseholdUtility | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
