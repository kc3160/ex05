import { Component } from '@angular/core';
import { MessageService } from '../message.service';
import { FormControl, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-user-data-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf,
  ],
  templateUrl: './user-data-form.component.html',
  styleUrl: './user-data-form.component.css'
})
export class UserDataFormComponent {
  userDataForm = new FormGroup({
    username: new FormControl('', Validators.required),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', Validators.required),
  });
  title!: string;
  errorMessage?: string | undefined;

  constructor(private messageService: MessageService) { 
    this.setTitle("User Form");
  }

  sendData(): void {
    this.log("Data submitted { " + this.userDataForm.value.username + ", " + this.userDataForm.value.email + ", " + this.userDataForm.value.password + " }");
  }

  showError(message: string): void {
    this.errorMessage = message;
  }

  clearError(): void {
    this.errorMessage = undefined;
  }

  log(message: string): void {
    this.messageService.add(message);
  }

  setTitle(title: string): void {
    this.log("Setting " + title + " from " + this.title);
    this.title = title;
  }
}
