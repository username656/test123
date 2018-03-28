import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs/Observable';

import { Notification, NotificationByDate } from '@app/core/models/notification';

import { image, internet, random } from 'faker';

const API_URL: string = 'http://mockbin.com/request';

export class NotificationServiceMock {
  public notifications: Notification[];

  public constructor(private http: HttpClient) {
    this.generateNotifications();
  }

  public getNotificationsByDay(): Observable<{status: string, dates: NotificationByDate[]}> {
    return this.http.get(API_URL)
    .pipe(map(() => {
      const dates: NotificationByDate[] = [
        {date: 'Today', notifications: [this.notifications[0]]},
        {date: '22/03/2018', notifications: [this.notifications[1], this.notifications[2]]},
        {date: '21/03/2018', notifications: [this.notifications[3], this.notifications[4]]}
      ];

      return {status: 'success', dates};
    }));
  }

  public updateNotificationReadState(notification: Notification, state: boolean): Observable<{status: string}> {
    return this.http.get(API_URL)
      .pipe(map(() => {
        this.notifications = this.notifications.map(item => item.id === notification.id
          ? Object.assign(item, state)
          : item
        );
        return {status: 'success'};
      }));
  }

  public updateNotificationsReadState(state: boolean): Observable<{status: string}> {
    return this.http.get(API_URL)
      .pipe(map(() => {
        this.notifications = this.notifications.map(notification => Object.assign(notification, {read: state}));
        return {status: 'success'};
      }));
  }

  public generateNotifications(): void {
    const amount: number = 5;
    this.notifications = [];

    for (let i: number = 0; i < amount; i++) {
      let relativeTime: string;
      if (i === 0) {
        relativeTime = `${random.number({min: 2, max: 23})} hours ago`;
      }

      if (i > 0 && i <= 2) {
        relativeTime = `1 day ago`;
      }

      if (i > 2) {
        relativeTime = `2 days ago`;
      }

      this.notifications.push({
        id: `${i + 1}`,
        relativeTime,
        image: image.avatar(),
        read: false,
        title: `Your team <b>General</b> was assigned to <b class="text-sm-break">${internet.email().toLowerCase()}</b>'s conversation`
      });
    }
  }
}
