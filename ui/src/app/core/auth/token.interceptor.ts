import {HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest} from '@angular/common/http';
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
    const headers: HttpHeaders = new HttpHeaders()
      .append('Accept', 'application/json')
      .append('Content-Type', 'application/json');
    if (!!token) {
      headers.append('Authorization', token);
    }
    request = request.clone({
      headers: headers
    });
    return next.handle(request);
  }
}
