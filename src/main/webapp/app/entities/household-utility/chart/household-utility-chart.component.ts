import { Component, Input, SimpleChanges } from '@angular/core';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ChartConfiguration, ChartOptions, ChartType } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';

@Component({
  standalone: true,
  selector: 'jhi-household-utility-chart',
  templateUrl: './household-utility-chart.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LineChartComponent {
  @Input() labels: string[] = [];
  @Input() data: number[] = [];
  @Input() label: string = '';

  public lineChartData: ChartConfiguration<'line'>['data'] = {
    labels: this.labels,
    datasets: [
      {
        data: this.data,
        label: this.label,
        fill: true,
        tension: 0.5,
        borderColor: 'black',
        backgroundColor: 'rgba(255,0,0,0.3)',
      },
    ],
  };
  public lineChartOptions: ChartOptions<'line'> = {
    responsive: false,
  };
  public lineChartLegend = true;

  constructor() {}

  ngOnInit() {}
  ngOnChanges(changes: SimpleChanges) {
    if (changes.labels) {
      this.lineChartData.labels = changes.labels.currentValue as unknown as string[];
    }
    if (changes.data) {
      this.lineChartData['datasets'][0]['data'] = changes.data.currentValue as unknown as number[];
    }
    if (changes.label) {
      this.lineChartData['datasets'][0]['label'] = changes.label.currentValue as unknown as string;
    }
  }
}
