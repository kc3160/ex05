import { Component } from '@angular/core';
import { UserService } from '../../user.service';
import { MessageService } from '../../message.service';
import { Helper } from '../../user';
import { UserDataFormComponent } from '../user-data-form.component';
import { first } from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: '../user-data-form.component.html',
  styleUrl: './login.component.css'
})

export class LoginComponent extends UserDataFormComponent {
  constructor(private userService: UserService, messageService: MessageService) { 
    super(messageService);
    this.setTitle("Login");
  }
  
  /**
   * Handles the process of logging in Helpers
   * This function validates the inputs
   * login helper is then called from the userService to then login the helper
   * 
   * if any of the inputs from the form are invalid an invalid login error will be thrown
   * 
   * if the userService fails then a Failed to login error is thrown
   * 
   * @returns {void} This method does not return anything.
   */

  override sendData(): void {
    //logs the info put into the form
    this.log("Logging in { " + this.userDataForm.value.username + ", " + this.userDataForm.value.email + ", " + this.userDataForm.value.password + " }");
    //checks to see if input information is valid
    if(this.userDataForm.valid){
      //logs in helper through the userService if input info is valid
      this.userService.login(new Helper(this.userDataForm.value.username!, this.userDataForm.value.email!, this.userDataForm.value.password!));
    }
    else {
      //throws error if input info is not valid
      this.showError("Invalid Login");
      return
    }
    this.userService.getFailed().pipe(first()).subscribe(hasFailed => {
      if (hasFailed == true) {
        //throws error if userService fails at login
        this.showError("Failed to login");
      } else if (hasFailed == false) {
        this.clearError();
      }
    });
  }
}
