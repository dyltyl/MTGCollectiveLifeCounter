import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WebService {
  public baseSite = 'https://magic-database.herokuapp.com/';
  constructor(public http: HttpClient) { }
  checkStatus(): Observable<any> {
    return this.http.get(this.baseSite + 'status');
  }
}
