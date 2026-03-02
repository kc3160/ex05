import { Component } from '@angular/core';
import { UserService } from '../user.service';
import { Location } from '@angular/common';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-user-form-manager',
  standalone: false,
  templateUrl: './user-form-manager.component.html',
  styleUrl: './user-form-manager.component.css'
})
export class UserFormManagerComponent {
  private failedSub!: Subscription;
  constructor(private userService: UserService, private location: Location) { }

  ngOnInit(): void {
    this.failedSub = this.userService.getFailed().subscribe(hasFailed => {
      if (hasFailed == undefined) {
        return;
      }
      if (!hasFailed) {
        this.location.back();
      }
    });
  }

  ngOnDestroy(): void {
    this.failedSub.unsubscribe();
  }

  current: number = 0;

  changeTo(current: number): void {
     this.current = current;
  }
}
