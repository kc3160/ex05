import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NeedsComponent } from './needs/needs.component';
import { NeedDetailComponent } from './need-detail/need-detail.component';
import { NeedSearchComponent } from './need-search/need-search.component';
import { RegistrationFormComponent } from './user-data-form/registration-form/registration-form.component';
import { LoginComponent } from './user-data-form/login/login.component';
import { UserFormManagerComponent } from './user-form-manager/user-form-manager.component';
import { BasketComponent } from './basket/basket.component';
import { AppComponent } from './app.component';
import { HomepageComponent } from './homepage/homepage.component';

const routes: Routes = [
  { path: '', redirectTo: '/homepage', pathMatch: 'full' },
  {path:'detail/:id', component: NeedDetailComponent},
  {path: 'home', component: HomepageComponent},
  {path: 'homepage', component: HomepageComponent},
  {path: 'needs', component: NeedsComponent},
  {path: 'search', component: NeedSearchComponent},
  {path: 'login-manager', component: UserFormManagerComponent},
  {path: 'basket', component: BasketComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {onSameUrlNavigation: 'reload'})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
