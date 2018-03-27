import { NO_ERRORS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthGuard } from './auth.guard';
import { AuthenticationService } from '@app/core/services/authentication.service';
import { mock } from 'ts-mockito/lib/ts-mockito';
import { Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

describe('AuthGuard', () => {
  const authenticationService = mock(AuthenticationService);
  const router = mock(Router);
  const guard: AuthGuard = new AuthGuard(authenticationService, router);

  it('should navigate to login when not logged', () => {
    spyOn(router, 'navigateByUrl');
    spyOn(authenticationService, 'isUserLogged').and.returnValue(false);

    guard.canActivate(mock(ActivatedRouteSnapshot), mock(RouterStateSnapshot));

    expect(router.navigateByUrl).toHaveBeenCalledWith('/login');
  });

  it('should return true when logged', () => {
    spyOn(authenticationService, 'isUserLogged').and.returnValue(true);

    const result = guard.canActivate(mock(ActivatedRouteSnapshot), mock(RouterStateSnapshot));

    expect(result).toBeTruthy();
  });
});
