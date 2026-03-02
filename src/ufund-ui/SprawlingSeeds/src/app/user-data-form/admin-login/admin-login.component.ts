import { Component } from '@angular/core';
import { UserDataFormComponent } from '../user-data-form.component';
import { UserService } from '../../user.service';
import { MessageService } from '../../message.service';
import { Admin } from '../../user';
import { first } from 'rxjs';

@Component({
  selector: 'app-admin-login',
  standalone: false,
  templateUrl: '../user-data-form.component.html',
  styleUrl: './admin-login.component.css'
})
export class AdminLoginComponent extends UserDataFormComponent{

  constructor(private userService: UserService, messageService: MessageService){
    super(messageService);
    this.setTitle("Admin Login")
  }

  /**
   * This method gathers admin login info from the form
   * the input information are validated
   * if the info put into the form is valid the admin is logged in through the userService
   * 
   * if the information input into the form is invalid a "Invalid Login" error is thrown
   * 
   * if there is an issue with the userService a "Failed to Login" error is thrown
   * 
   * @returns {void} This method does not return any value
   */

  override sendData(): void {
    //logs the information put into the user-data-form
    this.log("logging in { " + this.userDataForm.value.username + "," + this.userDataForm.value.email + "," + this.userDataForm.value.password + "}")
    if(this.userDataForm.valid){
      this.userService.login(new Admin(this.userDataForm.value.username!, this.userDataForm.value.email!, this.userDataForm.value.password!));
    } else {
      this.showError("Invalid Login") //error in case information input is invalid
      return
    }
    this.userService.getFailed().pipe(first()).subscribe(hasFailed => {
      if(hasFailed) {
        //throws error if userService fails
        this.showError("Failed to login")
      } else {
        this.clearError()
      }
    });
  }
}
