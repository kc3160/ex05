import { Component } from '@angular/core';
import { UserDataFormComponent } from '../user-data-form.component';
import { UserService } from '../../user.service';
import { MessageService } from '../../message.service';
import { Helper } from '../../user';
import { first } from 'rxjs';

@Component({
  selector: 'app-registration-form',
  standalone: false,
  templateUrl: '../user-data-form.component.html',
  styleUrl: './registration-form.component.css',
})
export class RegistrationFormComponent extends UserDataFormComponent {
  constructor(private userService: UserService, messageService: MessageService) { 
    super(messageService);
    this.setTitle("Registration");
  }

  override sendData(): void {
    this.log("Registering { " + this.userDataForm.value.username + ", " + this.userDataForm.value.email + ", " + this.userDataForm.value.password + " }");
    if(this.userDataForm.valid) {
      this.userService.registerHelper(new Helper(this.userDataForm.value.username!, this.userDataForm.value.email!, this.userDataForm.value.password!));
    }
    else {
      this.showError("Invalid Registration Form");
      return;
    }
    this.userService.getFailed().pipe(first()).subscribe(hasFailed => {
      if (hasFailed) {
        this.showError("Failed to register");
      } else {
        this.clearError();
      }
    })
  }
}
