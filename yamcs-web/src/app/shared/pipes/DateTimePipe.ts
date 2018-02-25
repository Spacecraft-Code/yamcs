import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'datetime' })
export class DateTimePipe implements PipeTransform {

  transform(date: Date | string): string | null {
    if (!date) {
      return null;
    }
    if (typeof date === 'string') {
      return date.replace('T', ' ').replace('Z', ' UTC');
    } else {
      const dateString = date.toISOString();
      return dateString.replace('T', ' ').replace('Z', ' UTC');
    }
  }
}
