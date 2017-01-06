package com.google.cloudjlb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Created by johnlabarge on 12/21/16.
 */


@Service
public class EventService {

  ArrayList<Event> eventData = new ArrayList<Event>();

  Logger logger = LoggerFactory.getLogger(EventService.class);

  public EventService() {
    eventData = new ArrayList<Event>();
    for (int i = 0; i < 200; i++) {
      eventData.add(
          Event.builder()
              .description(String.format("This is event %d", i))
              .name(String.format("Event %d", i))
              .owner(String.format("Owner %d", (int) (Math.random() * 50)))
              .date(EventService.randomDate())
              .externalLink(String.format("http://notimplemented/events/%d",i))
              .build()
      );
    }
  }
  @Cacheable("getAllEvents")
  public List<Event> getAllEvents() {

    logger.info("###\n\n retreiving events from service\n\n");
    return randomSubsetOfEvents();
  }

  @CacheEvict(cacheNames="getAllEvents", allEntries=true)
  public void evictEventsCache() {

  }
  private List<Event> randomSubsetOfEvents() {
    long seed = System.nanoTime();
    List<Event> shuffledEvents = new ArrayList<Event>();
    shuffledEvents.addAll(eventData);
    Collections.shuffle(shuffledEvents, new Random(seed));
    return new ArrayList<Event>(shuffledEvents.subList(0,4));
  }
  private static Date randomDate() {
     long maxDiff = 1000*60*60*24*365; // 1 year away
     long diff =  (long) (Math.random()*maxDiff);
     long now = System.currentTimeMillis();
     long laterDate = now+diff;
     return new Date(laterDate);
  }


}
