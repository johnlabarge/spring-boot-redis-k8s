package com.google.cloudjlb;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by johnlabarge on 12/20/16.
 */
public class Event implements Serializable {

  private String externalLink;
  private String description;
  private String name;
  private String owner;

  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  private Date date;



  public Event() {

  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }



  public static EventBuilder builder() {
    return new EventBuilder();
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getExternalLink() {
    return externalLink;
  }

  public void setExternalLink(String externalLink) {
    this.externalLink = externalLink;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public static class EventBuilder {
    private String externalLink;
    private String description;
    private String name;
    private String owner;
    private Date date;

    public EventBuilder() {

    }

    public EventBuilder externalLink(String newExternalLink) {
      externalLink = newExternalLink;
      return this;
    }

    public EventBuilder description(String newDescription) {
      description = newDescription;
      return this;
    }

    public EventBuilder name(String newName) {
      name = newName;
      return this;
    }

    public EventBuilder owner(String newOwner) {
      owner = newOwner;
      return this;
    }

    public EventBuilder date(Date newDate) {
      date = newDate;
      return this;
    }


    public Event build() {
      Event event = new Event();
      event.setExternalLink(externalLink);
      event.setDescription(description);
      event.setName(name);
      event.setOwner(owner);
      event.setDate(date);
      return event;
    }

  }


}
