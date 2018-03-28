import { NO_ERRORS_SCHEMA, Component, ElementRef, TemplateRef } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs/observable/of';
import { _throw } from 'rxjs/observable/throw';
import { Observable } from 'rxjs/Observable';
import { mock } from 'ts-mockito/lib/ts-mockito';

import { ShellComponent, SidebarState, LogoState } from './shell.component';
import { AuthenticationService } from '@app/core/services/authentication.service';
import { Title } from '@angular/platform-browser';
import { InboxService } from '@app/core/services/inbox.service';
import { BreakpointObserver, BreakpointState } from '@angular/cdk/layout';
import { DfPortalService } from '@devfactory/ngx-df/portal';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { User } from '@app/core/models/user';

describe('ShellComponent', () => {
  const SAMPLE_INBOX_BADGE = 5;
  const SAMPLE_WELCOME_BADGE = 3;
  const SAMPLE_USER = <User>{ firstName: 'Sample', lastName: 'Last' };

  let component: ShellComponent;
  let fixture: ComponentFixture<ShellComponent>;
  let breakpointObserver: BreakpointObserver;
  let portal: DfPortalService;
  let authenticationService: AuthenticationService;
  let inboxService: InboxService;
  let router: Router;
  let title: Title;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
        schemas: [NO_ERRORS_SCHEMA],
        declarations: [ ShellComponent ],
        imports: [
          ReactiveFormsModule,
          BrowserAnimationsModule,
          RouterTestingModule
        ],
        providers: [
          { provide: BreakpointObserver, useFactory: () => mock(BreakpointObserver) },
          { provide: DfPortalService, useFactory: () => mock(DfPortalService) },
          { provide: AuthenticationService, useFactory: () => mock(AuthenticationService) },
          { provide: InboxService, useFactory: () => mock(InboxService) }
        ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShellComponent);
    component = fixture.componentInstance;

    router = TestBed.get(Router);
    breakpointObserver = TestBed.get(BreakpointObserver);
    portal = TestBed.get(DfPortalService);
    authenticationService = TestBed.get(AuthenticationService);
    inboxService = TestBed.get(InboxService);

    title = TestBed.get(Title);

    spyOn(breakpointObserver, 'observe').and.returnValue(of(<BreakpointState>{}));
    spyOn(authenticationService, 'getCurrentUser').and.returnValue(SAMPLE_USER);
    spyOn(inboxService, 'getBadgeCount').and.returnValue(of(SAMPLE_INBOX_BADGE));
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });

  describe('isLogoVisible', () => {
    it('should return true on logo visible', () => {
      component.logoState = LogoState.Visible;

      expect(component.isLogoVisible).toBeTruthy();
    });

    it('should return false on logo not hidden', () => {
      component.logoState = LogoState.Hidden;

      expect(component.isLogoVisible).toBeFalsy();
    });
  });

  describe('isSidebarOpen', () => {
    it('should return true on sidebar open', () => {
      component.sidebarState = SidebarState.Open;

      expect(component.isSidebarOpen).toBeTruthy();
    });

    it('should return false on sidebar closed', () => {
      component.sidebarState = SidebarState.Closed;

      expect(component.isSidebarOpen).toBeFalsy();
    });
  });

  describe('showOverlay', () => {
    it('should return true on innerWidth <= ShellComponent.MEDIA_LARGE and sidebarState open', () => {
      spyOnProperty(window, 'innerWidth', 'get').and.returnValue(500);
      component.sidebarState = SidebarState.Open;

      expect(component.showOverlay).toBeTruthy();
    });

    it('should return true on innerWidth <= ShellComponent.MEDIA_LARGE and sidebarState closed', () => {
      spyOnProperty(window, 'innerWidth', 'get').and.returnValue(500);
      component.sidebarState = SidebarState.Closed;

      expect(component.showOverlay).toBeFalsy();
    });

    it('should return true on innerWidth > ShellComponent.MEDIA_LARGE', () => {
      spyOnProperty(window, 'innerWidth', 'get').and.returnValue(1000);
      component.sidebarState = SidebarState.Open;

      expect(component.showOverlay).toBeFalsy();
    });
  });

  describe('ngOnInit', () => {
    it('should get the user from the AuthenticationService', () => {
      component.ngOnInit();

      expect(component.user).toBe(SAMPLE_USER);
    });

    it('should init the primary group with the badge counts', () => {
      component.ngOnInit();

      expect(component.primaryGroup).toBeDefined();
      expect(component.primaryGroup[0].count).toBe(SAMPLE_INBOX_BADGE);
      expect(component.primaryGroup[5].count).toBe(SAMPLE_WELCOME_BADGE);
    });

    it('should initialize the sidebarState/logoState', () => {
      component.ngOnInit();

      expect(component.sidebarState).toBe(SidebarState.Open);
      expect(component.logoState).toBe(LogoState.Visible);
    });

    it('should create the breakpointObserver', () => {
      component.ngOnInit();

      expect(component.breakpointObserver$).toBeDefined();
    });
  });

  describe('ngOnDestroy', () => {
    it('should destroy the observer on ngOnDestroy', () => {
      component.ngOnInit();
      spyOn(component.breakpointObserver$, 'unsubscribe');

      component.ngOnDestroy();

      expect(component.breakpointObserver$.unsubscribe).toHaveBeenCalled();
    });
  });

  describe('onSignOutClick', () => {
    it('should logout correctly on onSignOutClick', () => {
      spyOn(authenticationService, 'logout');
      spyOn(router, 'navigateByUrl');

      component.onSignOutClick();

      expect(authenticationService.logout).toHaveBeenCalled();
      expect(router.navigateByUrl).toHaveBeenCalledWith('/login');
    });
  });

  describe('onHamburgerClick', () => {
    it('should hide the logo and close the sidebar when open', () => {
      component.sidebarState = SidebarState.Open;

      component.onHamburgerClick();

      expect(component.sidebarState).toBe(SidebarState.Closed);
      expect(component.logoState).toBe(LogoState.Hidden);
    });

    it('should show the logo and the sidebar when closed', () => {
      component.sidebarState = SidebarState.Closed;

      component.onHamburgerClick();

      expect(component.sidebarState).toBe(SidebarState.Open);
      expect(component.logoState).toBe(LogoState.Visible);
    });
  });

  describe('onEllipsisClick', () => {
    it('should open the portal and set the closebar on search click', () => {
      let searchInput = mock(HTMLElement);
      spyOn(portal, 'open');
      spyOn(component, 'closeSidebar');

      component.onEllipsisClick(mock(TemplateRef));

      expect(portal.open).toHaveBeenCalled();
      expect(component.closeSidebar).toHaveBeenCalled();
    });
  });

  describe('closeSidebar', () => {
    it('should delagate to onHamburgerClick on innerWidth <= ShellComponent.MEDIA_LARGE and sidebarState open', () => {
      spyOnProperty(window, 'innerWidth', 'get').and.returnValue(500);
      component.sidebarState = SidebarState.Open;
      spyOn(component, 'onHamburgerClick');

      component.closeSidebar();

      expect(component.onHamburgerClick).toHaveBeenCalled();
    });

    it('should NOT delagate to onHamburgerClick on innerWidth > ShellComponent.MEDIA_LARGE', () => {
      spyOnProperty(window, 'innerWidth', 'get').and.returnValue(1000);
      component.sidebarState = SidebarState.Open;
      spyOn(component, 'onHamburgerClick');

      component.closeSidebar();

      expect(component.onHamburgerClick).not.toHaveBeenCalled();
    });

    it('should NOT delagate to onHamburgerClick on sidebarState closed', () => {
      spyOnProperty(window, 'innerWidth', 'get').and.returnValue(500);
      component.sidebarState = SidebarState.Closed;
      spyOn(component, 'onHamburgerClick');

      component.closeSidebar();

      expect(component.onHamburgerClick).not.toHaveBeenCalled();
    });
  });
});
