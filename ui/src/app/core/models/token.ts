import { User } from './user';

export interface Token {
  token: string;
  expiration_date: string;
  type: 'FORGOT_PASSWORD';
  user: User;
}
