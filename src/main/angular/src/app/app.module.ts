import { CanDeactivateGuard } from './services/can-DeactivateGuard-service';
import { AuthGuardGuard } from './services/auth-guard.guard';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { AngularFontAwesomeModule } from 'angular-font-awesome';
import { RoleService } from './services/api/role.service';
@NgModule({
  declarations: [
    AppComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule,
    AngularFontAwesomeModule
  ],
  providers:  [RoleService,AuthGuardGuard,CanDeactivateGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }
