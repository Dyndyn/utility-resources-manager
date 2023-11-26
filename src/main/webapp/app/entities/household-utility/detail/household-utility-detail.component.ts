import { Component, Input, OnChanges, SimpleChange, SimpleChanges, ViewChild } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { LineChartComponent } from 'app/entities/chart/household-utility-chart.component';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IHouseholdUtility } from '../household-utility.model';
import { HouseholdUtilityService } from '../service/household-utility.service';

@Component({
  standalone: true,
  selector: 'jhi-household-utility-detail',
  templateUrl: './household-utility-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, LineChartComponent],
})
export class HouseholdUtilityDetailComponent implements OnChanges {
  @Input() householdUtility: IHouseholdUtility | null = null;
  @ViewChild('consumtionChart') consumtionChart: LineChartComponent | null = null;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected householdUtilityService: HouseholdUtilityService,
  ) {}

  previousState(): void {
    window.history.back();
  }

  regeneratePrediction(): void {
    this.householdUtilityService.generatePredictions(this.householdUtility!.id).subscribe(() => {
      this.householdUtilityService.find(this.householdUtility!.id).subscribe(hu => {
        this.ngOnChanges({ householdUtility: new SimpleChange(this.householdUtility, hu.body, false) });
      });
    });
  }
  ngOnChanges(changes: SimpleChanges): void {
    this.householdUtility = changes.householdUtility.currentValue;
    if (this.householdUtility != null && this.consumtionChart != null) {
      this.consumtionChart.ngOnChanges({
        predictedData: new SimpleChange(
          changes.householdUtility.previousValue.predictedConsumption!.data,
          changes.householdUtility.currentValue.predictedConsumption!.data,
          false,
        ),
        predictedLabels: new SimpleChange(
          changes.householdUtility.previousValue.predictedConsumption!.month,
          changes.householdUtility.currentValue.predictedConsumption!.month,
          false,
        ),
      });
    }
  }
}
