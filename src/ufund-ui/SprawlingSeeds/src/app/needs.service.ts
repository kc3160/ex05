import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders , HttpParams} from '@angular/common/http';

import {Observable, of} from 'rxjs';
import {catchError, map, tap} from 'rxjs/operators';

import {Need, ToolNeed, FertilizerNeed, SeedNeed, BundleNeed} from './need';
import { MessageService } from './message.service';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class NeedsService {

  private needsUrl = 'http://localhost:8080/needs';
  httpOp = {
    headers: new HttpHeaders({ 'Content-Type' : 'application/json'
    })
  };


  constructor(private http : HttpClient,
             private messageService: MessageService,
             private userService: UserService) { }

  /** GET whole cupboard*/
  getNeeds(): Observable<Need[]> {
    return this.http.get<Need[]>(this.needsUrl)
      .pipe(
        map(needs => needs.map(need => this.parseNeed(need))), 
        tap(_ => this.log('fetched needs')),
        catchError(this.handleError<Need[]>('getNeeds', []))
      );
  }

  /** GET need by id */
  getNeed(id: number): Observable<Need> {
    return this.http.get<Need>(`${this.needsUrl}/${id}`)
      .pipe(
        map(need => this.parseNeed(need)), 
        tap(n => {
          this.log(`${n ? 'fetched' : 'not found'}`);
        }),
        catchError(this.handleError<Need>(`getNeed ${id}`))
      );
  }

    /** GET multiple needs by their IDs */
  getNeedsByIds(ids: number[]): Observable<Need[]> {
    // Create HTTP params and append all IDs
    let params = new HttpParams();
    ids.forEach(id => {
      params = params.append('ids', id.toString());
    });

    return this.http.get<Need[]>(`${this.needsUrl}/batch`, { params })
      .pipe(
        map(needs => needs.map(need => this.parseNeed(need))),
        tap(_ => this.log(`fetched needs with ids: ${ids.join(',')}`)),
        catchError(this.handleError<Need[]>('getNeedsByIds', []))
      );
  }

  /** GET needs which matches the filters */
  searchNeeds(search: string, tagIds: number[], lBound: number, uBound: number): Observable<Need[]> {
    let params = new HttpParams()
      .set('search', search)
      .set('lBound', lBound)
      .set('uBound', uBound);

    tagIds.forEach(id => {
      params = params.append('tagIds', id.toString());
    });

    return this.http.get<Need[]>(`${this.needsUrl}/`, { params })
      .pipe(
        map(needs => needs.map(need => this.parseNeed(need))), 
        tap(a => a.length ?
          this.log(`found matching needs for '${search}', cost ${lBound} to ${uBound}, and matching tags of id ${tagIds}`) :
          this.log(`no matching needs found for '${search}', cost ${lBound} to ${uBound}, and matching tags of id ${tagIds}`)),
        catchError(this.handleError<Need[]>('searchNeeds', []))
      );
  }


///////////////////// NON-SAFE!!!!!!!!!!!!!!!! ///////////////////////

  /** POST*/
  addNeed(need: Need): Observable<Need>{
    return this.http.post<Need>(`${this.needsUrl}?id=${this.userService.getSessionId()}`, need, this.httpOp)
    .pipe(
      tap((tNeed: Need) => this.log(`posted need at id ${tNeed.id}`)),
        catchError(this.handleError<Need>('addNeed'))
    );
  }

  /** delete */
  deleteNeed(id: number): Observable<Need>{
    return this.http.delete<Need>(`${this.needsUrl}/${id}?id=${this.userService.getSessionId()}`,this.httpOp)
    .pipe(
      tap(_ => this.log(`deleted need with id ${id}`)),
        catchError(this.handleError<Need>('deleteNeed'))
    );
  }

  /** PUT */
  updateNeed(need: Need) : Observable<any>{
    return this.http.put(`${this.needsUrl}?id=${this.userService.getSessionId()}`, need, this.httpOp)
    .pipe(
      tap(_ => this.log(`put need on id ${need.id}`)),
        catchError(this.handleError<any>('updateNeed'))
    );
  }

  ////// Bundle RESTFUL ////// 

  addNeedToBundle(bunId: number, need: Need) : Observable<any>{
    return this.http.put(`${this.needsUrl}/${bunId}/add?id=${this.userService.getSessionId()}`, need,this.httpOp)
    .pipe(
      tap(_ => this.log(`put need ${need.id} on bundle ${bunId}`)),
        catchError(this.handleError<any>('addNeedToBundle'))
    );
  }

  removeNeedFromBundle(bunId: number, need: Need) : Observable<any>{
    return this.http.put(`${this.needsUrl}/${bunId}/remove?id=${this.userService.getSessionId()}`, need, this.httpOp)
    .pipe(
    tap(_ => this.log(`removed need ${need.id} from bundle ${bunId}`)),
      catchError(this.handleError<any>('removeNeedFromBundle'))
    );
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

  /** Log a Service message with the MessageService */
  private log(message: string) {
    this.messageService.add(`NeedsService: ${message}`);
  }

    private parseNeed(need: Need): Need {
    switch (need.type) {
      case 'tool':
        return { ...need, used: (need as ToolNeed).used } as ToolNeed;
      case 'fertilizer':
        return { ...need, organic: (need as FertilizerNeed).organic } as FertilizerNeed;
      case 'seed':
        return { ...need } as SeedNeed;
      case 'bundle':
        return {...need, discount: (need as BundleNeed).discount, needs: (need as BundleNeed).needs } as BundleNeed;
      default:
        return need;
    }
  }

}
