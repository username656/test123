import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '@app/shared/guards/auth.guard';

import { ShellComponent } from './pages/shell/shell.component';

const routes: Routes = [
  {path: '', component: ShellComponent, canActivate: [AuthGuard], children: [
    {path: 'welcome', loadChildren: 'app/modules/welcome/welcome.module#WelcomeModule'}
  ]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ShellRoutingModule { }
