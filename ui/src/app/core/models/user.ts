export interface User {
  username: string;
  firstName: string;
  lastName: string;
  linkedEmail?: boolean;
  linkedSocial?: boolean;
  linkedTwitter?: boolean;
  linkedFacebook?: boolean;
  activeMessenger?: boolean;
  invitedGang?: boolean;
  image?: string;
  state?: 'online' | 'offline';
  badgeInbox?: number;
  badgeWelcome?: number;
}
