import { Notification, NotificationByDate } from '@app/core/models/notification';

export interface ShellNotification extends Notification {
  showLink: boolean;
}

export interface ShellNotificationByDate extends NotificationByDate {
  notifications: ShellNotification[];
}
