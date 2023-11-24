import { Component, Input, SimpleChanges, ViewChild } from '@angular/core';

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

  ngOnInit() {}
  ngOnChanges(changes: SimpleChanges) {
    if (changes.labels) {
      this.lineChartData.labels = this.predictedLabels.concat(changes.labels.currentValue) as unknown as string[];
    }
    if (changes.predictedLabels) {
      this.lineChartData.labels = this.labels.concat(changes.predictedLabels.currentValue) as unknown as string[];
    }
    if (changes.data) {
      this.lineChartData['datasets'][0]['data'] = changes.data.currentValue as unknown as number[];
      let predictedData = this.predictedData;
      if (changes.data.currentValue.length > 0) {
        predictedData = Array(changes.data.currentValue.length - 1).fill(null);
        predictedData.push(changes.data.currentValue[changes.data.currentValue - 1]);
        predictedData = predictedData.concat(this.predictedData);
      }
      this.lineChartData['datasets'][1]['data'] = predictedData;
    }
    if (changes.predictedData) {
      let predictedData = changes.predictedData.currentValue;
      if (this.data.length > 0) {
        predictedData = Array(this.data.length - 1).fill(null);
        predictedData.push(this.data[this.data.length - 1]);
        predictedData = predictedData.concat(changes.predictedData.currentValue);
      }
      this.lineChartData['datasets'][1]['data'] = predictedData;
    }
    if (changes.label) {
      this.lineChartData['datasets'][0]['label'] = changes.label.currentValue as unknown as string;
    }
    if (changes.predictedLabel) {
      this.lineChartData['datasets'][1]['label'] = changes.predictedLabel.currentValue as unknown as string;
    }
    this.chart.update();
  }
}
