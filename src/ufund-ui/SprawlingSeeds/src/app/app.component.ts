import { Component } from '@angular/core';
import { UserService } from './user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'SprawlingSeeds';
  userService:UserService;

  constructor(userService : UserService){ 
    this.userService = userService;
  }

  isDivVisible = true;

  DivOff(){
    this.isDivVisible = false;
  }

  DivOn(){
    this.isDivVisible = true;
  }

  toggleBasket(): boolean{
    return !this.userService.isLoggedIn();
  }

  isLoggedIn(): boolean{
   return  this.userService.isLoggedIn();
  }

  isNotLoggedIn(): boolean{
    return !this.userService.isLoggedIn();
  }

  toggle_logged_in(): void{
    this.userService.logout();
  }
}
