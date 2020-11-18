import { ChangePasswordComponent } from '../pages/home/change-password/change-password.component';
import { AddRoleComponent } from '../pages/home/add-role/add-role.component';
import { AddUserComponent } from '../pages/home/add-user/add-user.component';
import { Injectable } from '@angular/core';
import { CanDeactivate } from '@angular/router';
import { Observable } from 'rxjs';

export interface CanComponentDeactivate {
  canDeactivate: () => Observable<boolean> | Promise<boolean> | boolean;
}

@Injectable()
export class CanDeactivateGuard implements CanDeactivate<AddUserComponent> {
  canDeactivate(component: AddUserComponent) {
  return component.canDeactivate ? component.canDeactivate() : true;
  }
}
