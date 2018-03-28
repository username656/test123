import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { UserServiceMock } from '@app/core/services/user.service.mock';

@Injectable()
export class UserService extends UserServiceMock {

  public constructor(http: HttpClient) {
    super(http);
  }

}
