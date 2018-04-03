import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'environments/environment';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs/Observable';

import { User } from '@app/core/models/user';
import { StorageService } from '@app/core/services/storage.service';
import {Token} from "@app/core/models/token";

// tslint:disable-next-line
const URLs = {
  login: `${environment.apiPath}/auth/login`,
  forgotPassword: `${environment.apiPath}/auth/forgot-password`,
  resetPassword: `${environment.apiPath}/auth/reset-password`,
  token: `${environment.apiPath}/auth/check-token`
};

@Injectable()
export class AuthenticationService {

  private static AUTHORIZATION_HEADER: string = 'Authorization';

  private static CURRENT_USER_STORAGE_KEY: string = 'kayako-user';
  private static CURRENT_TOKEN_STORAGE_KEY: string = 'kayako-token';
  private static CURRENT_LOGIN_EXPIRATION_STORAGE_KEY: string = 'kayako-login-expiration';
  private static CURRENT_LOGIN_EXPIRATION_DAYS: number = 30;
  private static CURRENT_LOGIN_EXPIRATION_SESSION: string = 'session';

  public constructor(private http: HttpClient,
    private storageService: StorageService) {
  }

  /**
   * Perform a user authentication and store logged in user info in the local storage.
   *
   * @param {string} username   User's username
   * @param {string} password   User's password
   * @param {boolean} remember  Indicate if the login has to be remembered or not
   * @return {boolean}          True if login was successful
   */
  public login(username: string, password: string, remember: boolean): Observable<boolean> {
    return this.http.post<User>(URLs.login, JSON.stringify({ username, password }), { observe: 'response' })
      .pipe(
        map(res => {
          const user: User = res.body;

          if (remember) {
            const expiration: Date = new Date();
            expiration.setDate(expiration.getDate() + AuthenticationService.CURRENT_LOGIN_EXPIRATION_DAYS);
            this.storageService.setItem(AuthenticationService.CURRENT_LOGIN_EXPIRATION_STORAGE_KEY,
              expiration.toString(), true);
          } else {
            this.storageService.setItem(AuthenticationService.CURRENT_LOGIN_EXPIRATION_STORAGE_KEY,
              AuthenticationService.CURRENT_LOGIN_EXPIRATION_SESSION, true);
          }
          this.storageService.setItem(AuthenticationService.CURRENT_TOKEN_STORAGE_KEY,
            res.headers.get(AuthenticationService.AUTHORIZATION_HEADER), remember);
          this.storageService.setItem(AuthenticationService.CURRENT_USER_STORAGE_KEY, JSON.stringify(user), remember);

          return true;
        })
      );
  }

  /**
   * Destroy current session and remove authentication local storage.
   */
  public logout(): void {
    // Remove user from storage to log user out
    this.storageService.removeItem(AuthenticationService.CURRENT_LOGIN_EXPIRATION_STORAGE_KEY, true);

    this.storageService.removeItem(AuthenticationService.CURRENT_TOKEN_STORAGE_KEY, false);
    this.storageService.removeItem(AuthenticationService.CURRENT_USER_STORAGE_KEY, false);

    this.storageService.removeItem(AuthenticationService.CURRENT_TOKEN_STORAGE_KEY, true);
    this.storageService.removeItem(AuthenticationService.CURRENT_USER_STORAGE_KEY, true);
  }

  public forgotPassword(email: string): Observable<boolean> {
    return this.http.post(`${URLs.forgotPassword}?email=${email}`, null)
      .pipe(
        map(() => true) // There is no response from the backend apart from OK (which is implicit)
      );
  }

  /**
   * @param {string} token generated reset token
   * @param {string} password New password
   */
  /* tslint:disable:no-any */
  public resetPassword(token: string, password: string): Observable<HttpResponse<any>> {
    return this.http.post(URLs.resetPassword, JSON.stringify({ token, password }), { observe: 'response' });
  }

  public isUserLogged(): boolean {
    return !!this.getCurrentToken();
  }

  public getCurrentToken(): string {
    const storageType: boolean = this.getStorageType();
    if (storageType !== undefined) {
      return this.storageService.getItem(AuthenticationService.CURRENT_TOKEN_STORAGE_KEY, storageType);
    } else {
      return undefined; // The user is not logged or login expired
    }
  }

  /**
   * Get current logged in user or undefined if nobody logged or login expired.
   *
   * @return {User} Current logged in user
   */
  public getCurrentUser(): User {
    const storageType: boolean = this.getStorageType();
    if (storageType !== undefined) {
      const data: string = this.storageService.getItem(AuthenticationService.CURRENT_USER_STORAGE_KEY, storageType);
      return JSON.parse(data);
    } else {
      return undefined; // The user is not logged or login expired
    }
  }

  /**
   * Get the storage type to be used to get the data from (according to the login) or undefined if expired/not logged.
   *
   * @return {boolean} True if persistent storage has to be used, false if session storage or undefined if the login
   *  has expired or not user logged
   */
  private getStorageType(): boolean {
    const loginExpiration: string = this.storageService.getItem(AuthenticationService.CURRENT_LOGIN_EXPIRATION_STORAGE_KEY, true);
    if (loginExpiration) {
      if (loginExpiration === AuthenticationService.CURRENT_LOGIN_EXPIRATION_SESSION) {
        return false;
      } else {
        const currentTime: Date = new Date();
        const expiration: Date = new Date(loginExpiration);
        if (currentTime < expiration) {
          return true;
        }   // The login expired
      }
    }   // The user is not logged

    return undefined;
  }

  /**
   * Get the token from the backend if not expired.
   *
   * @return (Token) the token registered
   */
  public checkForValidToken(token: string): Observable<any> {
    return this.http.get<any>(`${URLs.token}/${token}`);
  }

}
