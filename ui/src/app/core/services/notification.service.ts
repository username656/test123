import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { NotificationServiceMock } from '@app/core/services/notification.service.mock';

@Injectable()
export class NotificationService extends NotificationServiceMock {

  public constructor(http: HttpClient) {
    super(http);
  }

}
