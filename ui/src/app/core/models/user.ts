export interface User {
  username: string;
  firstName: string;
  lastName: string;
  invitedGang?: boolean;
  image?: string;
  status?: 'online' | 'offline';
  badgeInbox?: number;
}
