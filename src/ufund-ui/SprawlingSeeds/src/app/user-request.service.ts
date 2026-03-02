import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Helper, User } from './user';
import { Admin } from './user';
import { catchError, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserRequestService {
  //! Add this common stuff to a single static class like the base url and httpOptions
  private loginUrl = 'http://localhost:8080/account';
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }

  /**
   * POST: Create a Helper account, returns the made 
   * @param helper 
   * @returns 
   */
  registerHelper(helper: Helper): Observable<Helper> {
    const url = `${this.loginUrl}/register`;
    console.log(JSON.stringify(helper));
    return this.http.post<Helper>(url, helper, this.httpOptions).pipe(
      //tap((newHelper: Helper) => newHelper),
      catchError(this.handleError<Helper>("register", undefined))
    );
  }

  login(user: User): Observable<LoginData> {
    const url = `${this.loginUrl}/login`;

    return this.http.post<LoginData>(url, user, this.httpOptions).pipe(
      catchError(this.handleError<LoginData>("login", undefined))
    );
  }

  logout(id: number, isAdmin: boolean): Observable<void> {
    if (isAdmin) {
      return this.logoutAdmin(id);
    }
    else {
      return this.logoutHelper(id);
    }
  }

  logoutAdmin(id: number): Observable<void> {
    const url = `${this.loginUrl}/logout-admin/${id}`;

    return this.http.put<void>(url, null);
  }

  logoutHelper(id: number): Observable<void> {
    const url = `${this.loginUrl}/logout/${id}`;

    return this.http.put<void>(url, null);
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
    private handleError<T>(operation = 'operation', result?: T) {
      return (error: any): Observable<T> => {
        // Console any specific errors by branching based on operation and error type

        //console.error(error); // log to console instead
  
        // Let the app keep running by returning an empty result.
        return of(result as T);
      };
    }
}

export class LoginData {
  id: number;
  admin: boolean;

  constructor(id: number, admin: boolean) {
    this.id = id;
    this.admin = admin;
  }
}
