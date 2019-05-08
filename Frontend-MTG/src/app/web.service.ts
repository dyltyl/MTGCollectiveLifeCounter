import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
/**
 * A service used mainly to hold the value of the base site and the httpClient. Will eventually have GET/POST/PUT/DELETE methods (probably)
 */
export class WebService {
  /**
   * The base site of the REST API
   */
  public baseSite = 'https://magic-database.herokuapp.com/';
  constructor(public http: HttpClient) { }
  /**
   * Checks the status of the REST API
   */
  checkStatus(): Observable<any> {
    return this.http.get(this.baseSite + 'status');
  }
}
