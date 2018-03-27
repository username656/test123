import { Component, HostBinding, OnInit, TemplateRef } from '@angular/core';

import { User } from '@app/core/models/user';
import { UserService } from '@app/core/services/user.service';
import { DfModalService } from '@devfactory/ngx-df/modal';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss']
})
export class WelcomeComponent implements OnInit {
  public user: User;
  public loading: boolean = false;

  @HostBinding('class') public classes: string = 'd-flex flex-column col p-0';

  public constructor(private service: UserService, private modal: DfModalService) { }

  public ngOnInit(): void {
    this.user = this.service.user;
  }

  public onCardClick(card: string, active: TemplateRef<null>, inactive: TemplateRef<null>): void {
    if (this.user[card]) {
      this.modal.open(active, {customClass: `modal-welcome--${card}`});
    } else {
      this.modal.open(inactive, {customClass: `modal-welcome--no-${card}`});
    }
  }

  public onLinkedCardNextStepClick(
    close: Function,
    next: string,
    active: TemplateRef<null>,
    inactive: TemplateRef<null>
  ): void {
    close();
    this.onCardClick(next, active, inactive);
  }

  public onCardConfirmationClick(
    close: Function,
    card: string,
    active: TemplateRef<null>,
    inactive: TemplateRef<null>
  ): void {
    this.loading = true;
    this.service.updateIntegration(card).subscribe(response => {
      console.log(response);
      close();
      this.onCardClick(card, active, inactive);
      this.loading = false;
    }, err => {
      console.log(err);
      this.loading = false;
    });
  }
}
