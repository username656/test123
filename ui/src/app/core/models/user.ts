export interface User {
  username: string;
  firstName: string;
  lastName: string;
  invitedGang?: boolean;
  image?: string;
  state?: 'online' | 'offline';
  badgeInbox?: number;
}
