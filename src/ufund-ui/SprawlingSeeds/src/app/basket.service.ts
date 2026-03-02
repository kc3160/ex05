import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import {Observable, of, throwError} from 'rxjs';
import {catchError, map, tap} from 'rxjs/operators';

import {Basket} from './basket';
import { Need } from './need';
import {MessageService} from './message.service';
import {UserService} from  './user.service';

@Injectable({
  providedIn: 'root'
})
export class BasketService {

  private basketsUrl = 'http://localhost:8080/baskets';
  httpOp = {
    headers: new HttpHeaders({ 'Content-Type' : 'application/json'
    })
  };

  constructor(private http: HttpClient,
             private messageService: MessageService,
             private userService: UserService){}

  /** Get Basket */
  getBasket() : Observable<Basket | undefined> {
    if (this.userService.getSessionId() == undefined ) {
      return of(undefined);
    }
    return this.http
    .get<Basket>(`${this.basketsUrl}?id=${this.userService.getSessionId()}`)
    .pipe(
        tap(_ => this.log(`fetched basket`)),
          catchError(this.handleError<Basket>(`getBasket failed`))
    );
  }

  /** POST basket*/
  addNeedToBasket(need: Need): Observable<any> {
    if (this.userService.getSessionId() == undefined ) {
      return of(undefined);
    }
    return this.http.post(`${this.basketsUrl}/needs?id=${this.userService.getSessionId()}`, need, this.httpOp)
    .pipe(
    tap( _ => this.log(`added need with id ${need.id}`) ),
      catchError(this.handleError<any>('addNeedToBasket'))
    );
  }

  /** PUT basket*/
  updateBasket(basket : Basket) : Observable<any> {
    if (this.userService.getSessionId() == undefined ) {
      return of(undefined);
    }
    return this.http.put(`${this.basketsUrl}?id=${this.userService.getSessionId()}`, basket, this.httpOp)
    .pipe(
      tap(_ => this.log(`updated basket`)),
        catchError(this.handleError<any>('updateBasket'))
    );
  }

  /** PoST checkout*/
  checkoutBasket() : Observable<any> { 
    if (this.userService.getSessionId() == undefined ) {
      return of(undefined);
    }
    return this.http.post(`${this.basketsUrl}/checkout?id=${this.userService.getSessionId()}`,null)
    .pipe(
      tap( _ => this.log(`checking out basket`)),
      catchError(this.handleError<any>('checkoutBasket'))
    );
  }

  /**so I dont have to inject userService when basketService already has one*/
  isAdmin() : boolean {
    return this.userService.isAdmin;
  }

  isLoggedIn() : boolean{
    return !!this.userService.sessionId; 
  }
  /**or i could just inject lol*/
  

   /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      //TODO: log to a file instead of console, just TEMP
      console.error(error);

      //can be something else, such as a popup, or another component (ErrorComponent?)
      this.log(`${operation} failed: ${error.message}`);

      return throwError(() => result as T);
    };
  }

  /** Log a Service message with the MessageService */
  private log(message: string) {
    this.messageService.add(`BasketService: ${message}`);
  }
}
