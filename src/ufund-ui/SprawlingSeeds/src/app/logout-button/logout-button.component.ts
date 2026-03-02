import { Component } from '@angular/core';
import { UserService } from '../user.service';

@Component({
  selector: 'app-logout-button',
  standalone: false,
  templateUrl: './logout-button.component.html',
  styleUrl: './logout-button.component.css'
})
export class LogoutButtonComponent {
  service: UserService;
  constructor(service: UserService) {
    this.service = service;
  }

  logout(): void {
    this.service.logout();
  }
}
