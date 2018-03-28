import { AfterViewInit, Component, ElementRef, HostBinding, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { AuthenticationService } from '@app/core/services/authentication.service';
import { EmailUtilities } from '@app/shared/utilities/email-utilities';
import { DfValidationMessagesMap } from '@devfactory/ngx-df/validation-messages';

import { PasswordUtilities } from '@app/shared/utilities/password-utilities.ts';


@Component({
  selector: 'app-create-password-result',
  templateUrl: './create-password-result-page.component.html',
  styleUrls: ['./create-password-result-page.component.scss']
})
export class CreatePasswordResultPageComponent implements OnInit {
  public form: FormGroup;
  public result: string;

  @HostBinding('class') public class: string = 'col p-0 d-flex justify-content-center align-items-center';

  public constructor(
    private title: Title,
    private route: ActivatedRoute
  ) {}

  public ngOnInit(): void {
    this.title.setTitle('Versata');
    this.route.data.subscribe(data => this.result = data.result);
  }
}
