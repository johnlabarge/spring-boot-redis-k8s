/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { EventerDataService } from './eventer-data.service';

describe('EventerDataService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [EventerDataService]
    });
  });

  it('should ...', inject([EventerDataService], (service: EventerDataService) => {
    expect(service).toBeTruthy();
  }));
});
