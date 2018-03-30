import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {AuthenticationService} from '@app/core/services/authentication.service';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  public constructor(public auth: AuthenticationService) {
  }

  /* tslint:disable:no-any */
  public intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token: string = this.auth.getCurrentToken();
    if (!!token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        }
      });
    }
    request = request.clone({
      setHeaders: {
        Accept: 'application/json',
        'Content-Type': 'application/json'
      }
    });

    return next.handle(request);
  }
}
