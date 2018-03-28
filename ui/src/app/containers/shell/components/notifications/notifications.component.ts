import { Component, HostBinding, Input } from '@angular/core';
import { NotificationService } from '@app/core/services/notification.service';
import { ShellNotification, ShellNotificationByDate } from 'app/containers/shell/models/shell-notification';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.scss']
})
export class NotificationsComponent {
  @Input() public dates: ShellNotificationByDate[];
  @Input() public loading: boolean = false;
  @HostBinding('class') public classes: string = 'w-100 h-100 d-flex flex-column pt-2 mb-2';

  public constructor(
    private notificationService: NotificationService
  ) { }

  public onNotificationMouseEnter(notification: ShellNotification): void {
    if (!notification.read) {
      notification.showLink = true;
    }
  }

  public onNotificationMouseLeave(notification: ShellNotification): void {
    notification.showLink = false;
  }

  public onMarkAsReadClick(evt: Event, notification: ShellNotification): void {
    evt.preventDefault();
    notification.read = true;
    this.notificationService.updateNotificationReadState(notification, true).subscribe();
  }

  public onMarkAllAsReadClick(evt: Event): void {
    evt.preventDefault();

    for (let i: number = 0; i < this.dates.length; i++) {
      for (let j: number = 0; j < this.dates[i].notifications.length; j++) {
        this.dates[i].notifications[j].read = true;
      }
    }

    this.notificationService.updateNotificationsReadState(true).subscribe();
  }
}
