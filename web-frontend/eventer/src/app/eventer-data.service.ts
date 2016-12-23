import { Injectable } from '@angular/core';
import 'rxjs/add/operator/map'
import { Observable } from 'rxjs/Observable';
//import { Configuration } from '../app.constants';
import { Eventer } from './eventer'
import {Http, Response, Headers} from "@angular/http";

@Injectable()
export class EventerDataService {

  private actionUrl: string;
  private headers: Headers;

  constructor(private _http: Http) {
    var apiHostUrl = this.getApiHostUrl();
    this.actionUrl = apiHostUrl+'/events';
    console.log("actionUrl:"+this.actionUrl);

    this.headers = new Headers();
    this.headers.append('Content-Type', 'application/json');
    this.headers.append('Accept', 'application/json');
  }
  // Simulate GET /eventers
  public GetAll = (): Observable<Eventer[]> => {
    return this._http.get(this.actionUrl)
      .map((response: Response) => <Eventer[]>response.json())

  }
  private getApiHostUrl() {
     var protocol = window.location.protocol;
     var host = window.location.hostname;
     var port = window.location.port;
     console.log("protocol:"+protocol);
     if (host == 'localhost') {
       return `${protocol}//${host}:8080`
     } else {
       return `${protocol}//${host}:${port}/api`
     }
  }
}
