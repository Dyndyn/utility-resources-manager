import { Pipe, PipeTransform } from '@angular/core';

import dayjs from 'dayjs/esm';

@Pipe({
  standalone: true,
  name: 'formatMonthYearDate',
})
export default class FormatMonthYearDatePipe implements PipeTransform {
  transform(day: dayjs.Dayjs | null | undefined): string {
    return day ? day.format('MM/YYYY') : '';
  }
}
