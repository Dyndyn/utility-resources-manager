import { Component, Input, OnChanges, SimpleChanges, ViewChild } from '@angular/core';

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
export class LineChartComponent implements OnChanges {
  @Input() labels: string[] = [];
  @Input() data: number[] = [];
  @Input() label: string = '';
  @Input() predictedLabels: string[] = [];
  @Input() predictedData: number[] = [];
  @Input() predictedLabel: string = '';
  @ViewChild(BaseChartDirective) chart!: BaseChartDirective;

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
      {
        data: [10, 10, 10],
        label: 'Predicted',
        fill: true,
        tension: 0.5,
        borderColor: 'black',
        backgroundColor: 'rgba(0,255,0,0.3)',
      },
    ],
  };
  public lineChartOptions: ChartOptions<'line'> = {
    responsive: true,
  };
  public lineChartLegend = true;

  constructor() {}

  ngOnChanges(changes: SimpleChanges): void {
    if ('labels' in changes) {
      this.lineChartData.labels = this.predictedLabels.concat(changes.labels.currentValue) as unknown as string[];
    }
    if ('predictedLabels' in changes) {
      this.lineChartData.labels = this.labels.concat(changes.predictedLabels.currentValue) as unknown as string[];
    }
    if ('data' in changes) {
      this.lineChartData['datasets'][0]['data'] = changes.data.currentValue as unknown as number[];
      let predictedData = this.predictedData;
      if (changes.data.currentValue.length > 0) {
        predictedData = Array(changes.data.currentValue.length - 1).fill(null);
        predictedData.push(changes.data.currentValue[changes.data.currentValue - 1]);
        predictedData = predictedData.concat(this.predictedData);
      }
      this.lineChartData['datasets'][1]['data'] = predictedData;
    }
    if ('predictedData' in changes) {
      let predictedData = changes.predictedData.currentValue;
      if (this.data.length > 0) {
        predictedData = Array(this.data.length - 1).fill(null);
        predictedData.push(this.data[this.data.length - 1]);
        predictedData = predictedData.concat(changes.predictedData.currentValue);
      }
      this.lineChartData['datasets'][1]['data'] = predictedData;
    }
    if ('label' in changes) {
      this.lineChartData['datasets'][0]['label'] = changes.label.currentValue as unknown as string;
    }
    if ('predictedLabel' in changes) {
      this.lineChartData['datasets'][1]['label'] = changes.predictedLabel.currentValue as unknown as string;
    }
    this.chart.update();
  }
}
