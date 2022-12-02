package com.example.eventservice.services;

import com.example.eventservice.entities.Event;
import com.example.eventservice.repositories.EventRepository;
import com.google.common.collect.Lists;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ImpEventService implements IEventService{
    @Autowired
    EventRepository eventRepository;
    @Override
    public Event addEvent(Event event) {
        return this.eventRepository.save(event);
    }

    @Override
    public void deleteEvent(Long id) {
        this.eventRepository.deleteById(id);
    }

    @Override
    public Event updateEvent(Event event) {
        return this.eventRepository.saveAndFlush(event);
    }

    @Override
    public Event findEventById(long id) {
        return this.eventRepository.findById(id).get();
    }

    @Override
    public List<Event> findAllEvents() {
        return this.eventRepository.findAll();
    }

    @Override
    public Event findByStartBetween(@NonNull Date startDate, @NonNull Date startDate2) {
        return eventRepository.findByStartBetween(startDate,startDate2);
    }

    @Override
    public Event findByTitleLikeIgnoreCase(@NonNull String title) {
        return eventRepository.findByTitleLikeIgnoreCase(title);
    }

    @Override
    public Event findByLocationLikeIgnoreCase(@NonNull String location) {
        return eventRepository.findByLocationLikeIgnoreCase(location);
    }
    @Override
    public List<Event> getAllEventsByMember(Long idMember) {
        List<Event> eventsByMember = Lists.newArrayList();
        List<Event> events = eventRepository.findAll();
        var eventList = events.stream().filter(event -> event.getMemberId().equals(idMember)).toList();
        eventsByMember.addAll(eventList);
        return eventsByMember;
    }
}
