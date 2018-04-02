import { HttpClient, HttpClientModule, HttpHeaders, HttpResponse } from '@angular/common/http';
import { async, getTestBed, TestBed } from '@angular/core/testing';
import { of } from 'rxjs/observable/of';

import { CoreModule } from '@app/core/core.module';
import { User } from '@app/core/models/user';
import { AuthenticationService } from '@app/core/services/authentication.service';
import { StorageService } from '@app/core/services/storage.service';

describe('AuthenticationService', () => {
  let http: HttpClient;
  let storageService: StorageService;
  let service: AuthenticationService;

  const CURRENT_USER_LOCAL_STORAGE: string = 'kayako-user';
  const CURRENT_TOKEN_LOCAL_STORAGE: string = 'kayako-token';
  const CURRENT_LOGIN_EXPIRATION_LOCAL_STORAGE: string = 'kayako-login-expiration';
  const CURRENT_LOGIN_EXPIRATION_SESSION: string = 'session';
  const SAMPLE_AUTHORIZATION: string = 'Bearer sample';

  /* tslint:disable:no-any */
  const SUCCESS_RESPONSE: HttpResponse<any> = new HttpResponse({
    body: {
      username: 'sample',
      firstName: 'John',
      lastName: 'Doe'
    },
    headers: new HttpHeaders({ 'Authorization': SAMPLE_AUTHORIZATION })
  });

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        CoreModule,
        HttpClientModule
      ],
      providers: [
        AuthenticationService,
        { provide: StorageService, useClass: StorageService },
        HttpClient
      ]
    });
  }));

  beforeEach(() => {
    http = getTestBed().get(HttpClient);
    storageService = getTestBed().get(StorageService);
    service = getTestBed().get(AuthenticationService);
  });

  afterEach(() => {
    // The service manipulates the storage so it needs to be reset
    sessionStorage.removeItem(CURRENT_LOGIN_EXPIRATION_LOCAL_STORAGE);
    sessionStorage.removeItem(CURRENT_USER_LOCAL_STORAGE);
    sessionStorage.removeItem(CURRENT_TOKEN_LOCAL_STORAGE);

    localStorage.removeItem(CURRENT_LOGIN_EXPIRATION_LOCAL_STORAGE);
    localStorage.removeItem(CURRENT_USER_LOCAL_STORAGE);
    localStorage.removeItem(CURRENT_TOKEN_LOCAL_STORAGE);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('login', () => {
    it('should store data appropriately on a successful authentication / not remember option', () => {
      spyOn(http, 'post').and.returnValue(of(SUCCESS_RESPONSE));

      service.login('sample', 'secret', false).subscribe((res) => {
        expect(res).toBeDefined();
        expect(res).toBe(true);

        expect(localStorage.getItem(CURRENT_LOGIN_EXPIRATION_LOCAL_STORAGE))
          .toBe(CURRENT_LOGIN_EXPIRATION_SESSION);
        expect(sessionStorage.getItem(CURRENT_USER_LOCAL_STORAGE))
          .toBe('{"username":"sample","firstName":"John","lastName":"Doe"}');
        expect(sessionStorage.getItem(CURRENT_TOKEN_LOCAL_STORAGE))
          .toBe(SAMPLE_AUTHORIZATION);

        expect(localStorage.getItem(CURRENT_USER_LOCAL_STORAGE)).toBeNull();
        expect(localStorage.getItem(CURRENT_TOKEN_LOCAL_STORAGE)).toBeNull();
      });
    });

    it('should recover the authorization successfully / not remember option', () => {
      spyOn(http, 'post').and.returnValue(of(SUCCESS_RESPONSE));

      service.login('sample', 'secret', false).subscribe((res) => {
      });

      const token: string = service.getCurrentToken();
      expect(token).toBeDefined();
      expect(token).toBe(SAMPLE_AUTHORIZATION);
    });

    it('should recover the user data successfully / not remember option', () => {
      spyOn(http, 'post').and.returnValue(of(SUCCESS_RESPONSE));

      service.login('sample', 'secret', false).subscribe((res) => {
      });

      const user: User = service.getCurrentUser();
      expect(user).toBeDefined();
      expect(user.username).toBe('sample');
      expect(user.firstName).toBe('John');
    });

    it('should recover the user data successfully / remember option', () => {
      spyOn(http, 'post').and.returnValue(of(SUCCESS_RESPONSE));

      service.login('sample', 'secret', true).subscribe((res) => {
      });

      const user: User = service.getCurrentUser();
      expect(user).toBeDefined();
      expect(user.username).toBe('sample');
      expect(user.firstName).toBe('John');
    });

    it('should recover the authorization successfully / remember option', () => {
      spyOn(http, 'post').and.returnValue(of(SUCCESS_RESPONSE));

      service.login('sample', 'secret', true).subscribe((res) => {
      });

      const token: string = service.getCurrentToken();
      expect(token).toBeDefined();
      expect(token).toBe(SAMPLE_AUTHORIZATION);
    });
  });

  describe('getCurrentUser', () => {
    it('should not recover user when no login', () => {
      const user: User = service.getCurrentUser();
      expect(user).toBeUndefined();
    });

    it('should not have user after logout / no remember option', () => {
      spyOn(http, 'post').and.returnValue(of(SUCCESS_RESPONSE));

      service.login('sample', 'secret', false).subscribe((res) => {
      });
      service.logout();

      const user: User = service.getCurrentUser();
      expect(user).toBeUndefined();
    });

    it('should not have user after logout / remember option', () => {
      spyOn(http, 'post').and.returnValue(of(SUCCESS_RESPONSE));

      service.login('sample', 'secret', true).subscribe((res) => {
      });
      service.logout();

      const user: User = service.getCurrentUser();
      expect(user).toBeUndefined();
    });
  });

  describe('getCurrentToken', () => {
    it('should not recover token when no login', () => {
      const token: string = service.getCurrentToken();
      expect(token).toBeUndefined();
    });

    it('should not have token after logout / no remember option', () => {
      spyOn(http, 'post').and.returnValue(of(SUCCESS_RESPONSE));

      service.login('sample', 'secret', false).subscribe((res) => {
      });
      service.logout();

      const token: string = service.getCurrentToken();
      expect(token).toBeUndefined();
    });

    it('should not have token after logout / remember option', () => {
      spyOn(http, 'post').and.returnValue(of(SUCCESS_RESPONSE));

      service.login('sample', 'secret', true).subscribe((res) => {
      });
      service.logout();

      const token: string = service.getCurrentToken();
      expect(token).toBeUndefined();
    });
  });

  describe('logout', () => {
    it('should clear the data on logout / not remember option', () => {
      spyOn(http, 'post').and.returnValue(of(SUCCESS_RESPONSE));

      service.login('sample', 'secret', false).subscribe((res) => {
        expect(res).toBeDefined();
        expect(res).toBe(true);

        expect(localStorage.getItem(CURRENT_LOGIN_EXPIRATION_LOCAL_STORAGE)).toBeDefined();
        expect(sessionStorage.getItem(CURRENT_USER_LOCAL_STORAGE)).toBeDefined();
        expect(sessionStorage.getItem(CURRENT_TOKEN_LOCAL_STORAGE)).toBeDefined();

        expect(localStorage.getItem(CURRENT_USER_LOCAL_STORAGE)).toBeNull();
        expect(localStorage.getItem(CURRENT_TOKEN_LOCAL_STORAGE)).toBeNull();
      });

      service.logout();
      expect(localStorage.getItem(CURRENT_LOGIN_EXPIRATION_LOCAL_STORAGE)).toBeNull();
      expect(sessionStorage.getItem(CURRENT_USER_LOCAL_STORAGE)).toBeNull();
      expect(sessionStorage.getItem(CURRENT_TOKEN_LOCAL_STORAGE)).toBeNull();

      expect(localStorage.getItem(CURRENT_USER_LOCAL_STORAGE)).toBeNull();
      expect(localStorage.getItem(CURRENT_TOKEN_LOCAL_STORAGE)).toBeNull();
    });

    it('should clear the data on logout / remember option', () => {
      spyOn(http, 'post').and.returnValue(of(SUCCESS_RESPONSE));

      service.login('sample', 'secret', true).subscribe((res) => {
        expect(res).toBeDefined();
        expect(res).toBe(true);

        expect(localStorage.getItem(CURRENT_LOGIN_EXPIRATION_LOCAL_STORAGE)).toBeDefined();
        expect(localStorage.getItem(CURRENT_USER_LOCAL_STORAGE)).toBeDefined();
        expect(localStorage.getItem(CURRENT_TOKEN_LOCAL_STORAGE)).toBeDefined();

        expect(sessionStorage.getItem(CURRENT_USER_LOCAL_STORAGE)).toBeNull();
        expect(sessionStorage.getItem(CURRENT_TOKEN_LOCAL_STORAGE)).toBeNull();
      });

      service.logout();
      expect(localStorage.getItem(CURRENT_LOGIN_EXPIRATION_LOCAL_STORAGE)).toBeNull();
      expect(sessionStorage.getItem(CURRENT_USER_LOCAL_STORAGE)).toBeNull();
      expect(sessionStorage.getItem(CURRENT_TOKEN_LOCAL_STORAGE)).toBeNull();

      expect(localStorage.getItem(CURRENT_USER_LOCAL_STORAGE)).toBeNull();
      expect(localStorage.getItem(CURRENT_TOKEN_LOCAL_STORAGE)).toBeNull();
    });
  });

  describe('forgotPassword', () => {
    it('should delegate to the endpoint', () => {
      spyOn(http, 'post').and.returnValue(of({}));

      service.forgotPassword('sample@example.org').subscribe((res) => {
        expect(res).toBeTruthy();
      });
    });
  });

  describe('resetPassword', () => {
    it('should delegate to the endpoint', () => {
      spyOn(http, 'post').and.returnValue(of({}));

      service.resetPassword('key', 'password').subscribe((res) => {
        expect(res).toBeTruthy();
      });
    });
  });
});
