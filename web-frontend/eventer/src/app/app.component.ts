import { Component, OnInit } from '@angular/core';
import { EventerDataService } from './eventer-data.service';
import {Eventer} from "./eventer";

@Component({
  providers: [EventerDataService],
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  public eventers: Eventer[];

  constructor(private _dataService: EventerDataService) { }

  ngOnInit() {
    this.getAllItems();
    console.log(this.eventers);
  }


  private getAllItems(): void {
    this._dataService
      .GetAll()
      .subscribe( (data:Eventer[]) => { this.eventers = data;
        console.log(data)},
        error => console.log(error),
        () => console.log('Get all Items complete'));
  }
}
