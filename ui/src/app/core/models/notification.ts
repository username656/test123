export interface Notification {
  id: string;
  relativeTime: string;
  image: string;
  read: boolean;
  title: string;
}

export interface NotificationByDate {
  date: string;
  notifications: Notification[];
}
