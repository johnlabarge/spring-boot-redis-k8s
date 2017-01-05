package com.google.cloudjlb;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * Created by johnlabarge on 12/20/16.
 */
@RestController
public class EventController {

  @Autowired
  EventService eventService;
  @CrossOrigin
  @RequestMapping(value ="/events",
                  method = RequestMethod.GET,
                  produces = "application/json")
  public List<Event> getEvents() throws Exception {
       return eventService.getAllEvents();
  }

  @CrossOrigin
  @RequestMapping(value="/evict-event-cache",
                  method=RequestMethod.GET,
                  produces="application/json")
  public String evictEventCache() throws Exception {
       eventService.evictEventsCache();
       return "OK";
  }

}
