import {User} from './user';

export interface Token {
  token;
  expiration_date;
  type: 'FORGOT_PASSWORD';
  user: User;
}
