package com.example.eventservice.services;

import com.example.eventservice.entities.Event;
import lombok.NonNull;

import java.util.Date;
import java.util.List;

public interface IEventService {
    Event addEvent(Event event,Long idCreator);
    void deleteEvent(Long id);
    Event updateEvent(Event event);
    Event findEventById(Long id);
    Event affectMembersToEvent(List<Long> ids , Long idEvent);
    List<Event> findAllEvents();
    Event findByStartBetween(@NonNull Date startDate, @NonNull Date startDate2);
    Event findByTitleLikeIgnoreCase(@NonNull String title);
    Event findByLocationLikeIgnoreCase(@NonNull String location);
    List<Event> getAllEventsByMember(Long idMember);
}
