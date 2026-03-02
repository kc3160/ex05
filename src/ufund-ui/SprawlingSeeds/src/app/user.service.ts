import { Injectable } from '@angular/core';
import { Helper, User } from './user';
import { Admin } from './user';
import { UserRequestService, LoginData } from './user-request.service';
import { BehaviorSubject, Observable, of, tap } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  // Undefined when not logged in, a number when logged in
  sessionId?: number = undefined;
  isAdmin: boolean = false;
  private hasFailed: BehaviorSubject<boolean|undefined>;

  constructor(private service: UserRequestService) { 
    this.hasFailed = new BehaviorSubject<boolean|undefined>(true);
  }

  /**
   * This method checks the input on the user-data-form to make sure that something was put into it
   * 
   * info that is input into the login form is then sent into the user-request-service
   * 
   * @param helper 
   * @return {void} This method does not return anything
   */
  registerHelper(helper: Helper): void {
    this.service.registerHelper(helper).subscribe((h: Helper) => {
      if (h == undefined) {
        this.hasFailed.next(true);
      } else {
        this.hasFailed.next(false);
      }
      this.hasFailed.next(undefined);
    });
  }

  /**
   * This method checks that Helper info was put into the login form exists within the user-request-service
   * 
   * info that is put into the login form is sent to the user-request-service
   * 
   * @param helper 
   * @return {void} This method returns nothing
   */
  login(user: User): void {
    this.service.login(user).subscribe((data: LoginData) => {
      if (data.id != null) {
        this.sessionId = data.id;
        this.isAdmin = data.admin;
        this.hasFailed.next(false);
      } else {
        this.hasFailed.next(true);
      }
      this.hasFailed.next(undefined);
    });
  }

  logout(): void {
    if (this.sessionId == undefined) {
      return;
    }
    this.service.logout(this.sessionId, this.isAdmin).subscribe(
    (response) => {
      this.sessionId = undefined;
      this.isAdmin = false;
    },
    (error: HttpErrorResponse) => {
      console.error(error);
    }
    );
  }

  setSessionId(sessionId: number): void {
    this.sessionId = sessionId;
  }

  getSessionId(): number | undefined {
    return this.sessionId;
  }

  getFailed(): Observable<boolean|undefined> {
    return this.hasFailed.asObservable();
  }

  isLoggedIn(): boolean {
    if (this.sessionId == undefined) {
      return true;
    }
    return false;
  }
}
