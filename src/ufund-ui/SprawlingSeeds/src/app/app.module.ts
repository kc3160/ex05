import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration, withEventReplay } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NeedDetailComponent } from './need-detail/need-detail.component';
import { NeedsComponent } from './needs/needs.component';
import { MessagesComponent } from './messages/messages.component';
import { NeedSearchComponent } from './need-search/need-search.component';
import { UserDataFormComponent } from './user-data-form/user-data-form.component';
import { BasketComponent } from './basket/basket.component';
import { RegistrationFormComponent } from './user-data-form/registration-form/registration-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './user-data-form/login/login.component';
import { AdminLoginComponent } from './user-data-form/admin-login/admin-login.component';
import { UserFormManagerComponent } from './user-form-manager/user-form-manager.component';
import { LogoutButtonComponent } from './logout-button/logout-button.component';
import { TagComponent } from './tag/tag.component';
import { TagMenuComponent } from './tag-menu/tag-menu.component';
import { TagDropdownComponent } from './tag-dropdown/tag-dropdown.component';
import { HomepageComponent } from './homepage/homepage.component';

@NgModule({
  declarations: [
    AppComponent,
    NeedDetailComponent,
    NeedsComponent,
    MessagesComponent,
    BasketComponent,
    RegistrationFormComponent,
    LoginComponent,
    AdminLoginComponent,
    UserFormManagerComponent,
    LogoutButtonComponent,
    TagMenuComponent,
    NeedSearchComponent,
    TagDropdownComponent,
    HomepageComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    TagComponent,
    ReactiveFormsModule,
    FormsModule,
    TagComponent
],
  providers: [
    provideClientHydration(withEventReplay())
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
