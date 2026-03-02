import { Injectable } from '@angular/core';
import { Tag } from './tag';
import { catchError, Observable, of, tap } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { UserService } from './user.service';
import { MessageService } from './message.service';

@Injectable({
  providedIn: 'root'
})
export class TagService {
  private loginUrl = 'http://localhost:8080/tags';
    httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

  constructor(private http: HttpClient, private userService: UserService, private messageService: MessageService) { }

    /** GET all tags */
   getTags(): Observable<Tag[]> {
     return this.http.get<Tag[]>(this.loginUrl)
       .pipe(
         tap(_ => this.log('fetched tags')),
         catchError(this.handleError<Tag[]>('getTags', []))
       );
   }

   //assuming the methods
  getNeedTags(id: number): Observable<Tag[]>{
    const url = `${this.loginUrl}/${id}`;
    return this.http.get<Tag[]>(url)
       .pipe(
         tap(_ => this.log(`fetched tags for need id=${id}`)),
         catchError(this.handleError<Tag[]>(`getNeedTags id=${id}`))
       ); 
  }

  /**
   //GET tag by id 
   getTag(id: number): Observable<Tag> {
     const url = `${this.loginUrl}/${id}`;
     return this.http.get<Tag>(url)
       .pipe(
         tap(_ => this.log(`fetched tag id=${id}`)),
         catchError(this.handleError<Tag>(`getTag id=${id}`))
       );
   }*/

  createTag(tag: Tag): Observable<Tag | undefined> {
    if (this.userService.getSessionId() == undefined) {
      return of(undefined);
    }
    const url = `${this.loginUrl}?id=${this.userService.getSessionId()}`;

    return this.http.post<Tag>(url, tag, this.httpOptions);
  }

  updateTag(tag: Tag): Observable<Tag | undefined> {
    if (this.userService.getSessionId() == undefined) {
      return of(undefined);
    }
    const url = `${this.loginUrl}?id=${this.userService.getSessionId()}`;

    return this.http.put<Tag>(url, tag, this.httpOptions);
  }

  updateNeedTag(needId: number, oldTagId: number, tagId: number) : Observable<void> {
    return this.http.put<void>(`${this.loginUrl}/${needId}/${oldTagId}/${tagId}?id=${this.userService.getSessionId()}`, this.httpOptions)
    .pipe(
      tap(_ => this.log(`change need ${this.loginUrl} & ${oldTagId} with ${tagId}`)),
        catchError(this.handleError<void>('updateNeedTag'))
    );
  }

  deleteTag(tag: Tag): Observable<void> {
    if (this.userService.getSessionId() == undefined) {
      return of();
    }
    const url = `${this.loginUrl}/${tag.id}?id=${this.userService.getSessionId()}`;

    return this.http.delete<void>(url);
  }

  private log(message: string) {
    this.messageService.add(`NeedsService: ${message}`);
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

      //TODO: log to a file instead of console, just TEMP
      console.error(error);

      //can be something else, such as a popup, or another component (ErrorComponent?)
      this.log(`${operation} failed: ${error.message}`);

      return of(result as T);
    };
  }
}
