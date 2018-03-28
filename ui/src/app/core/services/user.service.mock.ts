import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs/Observable';

import { User } from '../models/user';

const API_URL: string = 'http://mockbin.com/request';

@Injectable()
export class UserServiceMock {
  public user: User;

  public constructor(private http: HttpClient) {
    // Generating mock user. This should be replaced by back-end information.
    this.generateUser();
  }

  public updateIntegration(field: string): Observable<{status: string, user?: User}> {
    return this.http.get(API_URL).pipe(map(() => {
      this.user[field] = true;
      return {status: 'success', user: this.user};
    }));
  }

  /**
   * Just in case there no login when the client reviews, generate
   * a mock user for them anyways.
   */
  public generateUser(): void {
    this.user = {
      username: 'ricardo.maturi@devfactory.com',
      firstName: 'Ricardo',
      lastName: 'Maturi',
      image: '/assets/img/profile-image.jpg',
      state: 'online',
      badgeInbox: 12
    };
  }
}
