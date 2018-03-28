import { BreakpointObserver } from '@angular/cdk/layout';
import { Component, HostBinding, OnDestroy, OnInit, Renderer2, TemplateRef } from '@angular/core';
import { NavigationStart, Router } from '@angular/router';
import { User } from '@app/core/models/user';
import { NotificationService } from '@app/core/services/notification.service';
import { UserService } from '@app/core/services/user.service';
import { DfPortalOptions, DfPortalOrientation, DfPortalService } from '@devfactory/ngx-df/portal';
import { filter } from 'rxjs/operators';
import { Subscription } from 'rxjs/Subscription';

import { ShellNotification, ShellNotificationByDate } from '../../models/shell-notification';

import { logoState, overlayState, sidebarState, topbarState } from './shell.animation';
import { AuthenticationService } from '@app/core/services/authentication.service';


enum SidebarState {
  Open = 'open',
  Closed = 'closed'
}

enum LogoState {
  Visible = 'visible',
  Hidden = 'hidden'
}

interface IconGroup {
  label: string;
  link?: string;
  icon: string;
  count?: number;
}

@Component({
  selector: 'app-shell',
  templateUrl: './shell.component.html',
  styleUrls: ['shell.component.scss'],
  animations: [logoState, overlayState, sidebarState, topbarState]
})
export class ShellComponent implements OnInit, OnDestroy {
  private static readonly MEDIA_MEDIUM: number = 767;
  private static readonly MEDIA_LARGE: number = 991;

  public user: User;
  public sidebarState: SidebarState = SidebarState.Open;
  public logoState: LogoState = LogoState.Visible;
  public breakpointObserver$: Subscription;
  public primaryGroup: IconGroup[];
  public loading: boolean = false;
  public dates: ShellNotificationByDate[];

  public get isLogoVisible(): boolean {
    return this.logoState === LogoState.Visible;
  }

  public get isSidebarOpen(): boolean {
    return this.sidebarState === SidebarState.Open;
  }

  public get showOverlay(): boolean {
    if (window.innerWidth <= ShellComponent.MEDIA_LARGE) {
      return this.sidebarState === SidebarState.Open;
    } else {
      return false;
    }
  }

  @HostBinding('class') public classes: string = 'd-flex flex-column col p-0';

  public constructor(
    private router: Router,
    private breakpointObserver: BreakpointObserver,
    private renderer: Renderer2,
    private portal: DfPortalService,
    private authenticationService: AuthenticationService,
    private userService: UserService,
    private notificationService: NotificationService
  ) {}

  public ngOnInit(): void {
    this.user = this.userService.user;
    this.initPrimaryGroup();
    this.sidebarState = window.innerWidth > ShellComponent.MEDIA_MEDIUM ? SidebarState.Open : SidebarState.Closed;
    this.logoState = window.innerWidth > ShellComponent.MEDIA_MEDIUM ? LogoState.Visible : LogoState.Hidden;
    this.listenToWindowResize();
    this.router.events
      .pipe(filter(event => event instanceof NavigationStart))
      .subscribe((event: NavigationStart) => {
        this.closeSidebar();
      });
  }

  public ngOnDestroy(): void {
    this.breakpointObserver$.unsubscribe();
  }

  public onSignOutClick(): void {
    this.authenticationService.logout();
    this.router.navigateByUrl('/login');
  }

  public onHamburgerClick(): void {
    if (this.sidebarState === SidebarState.Open) {
      this.logoState = LogoState.Hidden;
      this.sidebarState = SidebarState.Closed;
    } else {
      this.sidebarState = SidebarState.Open;
      setTimeout(() => {
        this.logoState = LogoState.Visible;
      }, 300);
    }
  }

  public onTopbarAnimationStart(topbar: HTMLElement): void {
    this.renderer.removeClass(topbar, 'justify-content-between');
    this.renderer.removeClass(topbar, 'justify-content-center');
    this.renderer.addClass(topbar, 'justify-content-end');
  }

  public onTopbarAnimationDone(topbar: HTMLElement): void {
    this.renderer.removeClass(topbar, 'justify-content-end');
    if (this.sidebarState === SidebarState.Open) {
      this.renderer.addClass(topbar, 'justify-content-between');
    } else {
      this.renderer.addClass(topbar, 'justify-content-center');
    }
  }

  public onSearchClick(template: TemplateRef<null>, searchInput: HTMLElement): void {
    searchInput.blur();
    const options: DfPortalOptions = new DfPortalOptions();
    options.orientation = DfPortalOrientation.Top;
    this.portal.open(template, options);
    this.closeSidebar();
  }

  public onEllipsisClick(template: TemplateRef<null>): void {
    const options: DfPortalOptions = new DfPortalOptions();
    options.orientation = DfPortalOrientation.Right;
    this.portal.open(template, options);
    this.closeSidebar();
  }

  public onNotificationsIconClick(evt: boolean): void {
    if (evt) {
      this.loadNotifications();
    }
  }

  public onMobileNotificationsIconClick(template: TemplateRef<null>): void {
    const options: DfPortalOptions = new DfPortalOptions();
    options.orientation = DfPortalOrientation.Top;
    options.height = 450;
    this.portal.open(template, options);
    this.loadNotifications();
  }

  public onNotificationMouseEnter(notification: ShellNotification): void {
    if (!notification.read) {
      notification.showLink = true;
    }
  }

  public onNotificationMouseLeave(notification: ShellNotification): void {
    notification.showLink = false;
  }

  public onMarkAsReadClick(evt: Event, notification: ShellNotification): void {
    evt.preventDefault();
    notification.read = true;
    this.notificationService.updateNotificationReadState(notification, true);
  }

  public closeSidebar(): void {
    if (window.innerWidth <= ShellComponent.MEDIA_LARGE && this.sidebarState === SidebarState.Open) {
      this.onHamburgerClick();
    }
  }

  public listenToWindowResize(): void {
    this.breakpointObserver$ = this.breakpointObserver.observe([
      '(max-width: 991px)'
    ]).subscribe(result => {
      if (result.matches) {
        if (this.sidebarState === SidebarState.Open) {
          this.sidebarState = SidebarState.Closed;
          this.logoState = LogoState.Hidden;
        }
      } else {
        if (this.sidebarState === SidebarState.Closed) {
          this.sidebarState = SidebarState.Open;
          setTimeout(() => {
            this.logoState = LogoState.Visible;
          }, 300);
        }
      }
    });
  }

  public initPrimaryGroup(): void {
    this.primaryGroup = [
      {label: 'Inbox', link: '/', icon: 'fa-inbox'},
      {label: 'Users', icon: 'fa fa-address-card'},
      {label: 'Organizations', icon: 'fa fa-building'},
      {label: 'Insights', icon: 'fa fa-bar-chart'},
      {label: 'Helpcenter', icon: 'fa fa-book'}
    ];
  }

  public loadNotifications(): void {
    this.loading = true;
      this.notificationService.getNotificationsByDay().subscribe(response => {
        if (response.status === 'success') {
          this.dates = response.dates.map(date => {
            return Object.assign(date, {notifications: date.notifications.map(notification => {
              return Object.assign(notification, {showLink: false});
            })});
          });
        }
        this.loading = false;
      }, err => {
        console.log(err);
        this.loading = false;
      });
  }
}
