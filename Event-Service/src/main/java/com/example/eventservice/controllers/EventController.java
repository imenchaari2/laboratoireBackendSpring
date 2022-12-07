package com.example.eventservice.controllers;

import com.example.eventservice.entities.Event;
import com.example.eventservice.services.ImpEventService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/api/event")
public class EventController {
    @Autowired
    ImpEventService eventService;
    @PostMapping(value = "/addEvent")
    public Event addEvent(@RequestBody Event event){
        return eventService.addEvent(event);
    }
    @PutMapping(value="/updateEvent/{id}")
    public Event updateEvent(@PathVariable Long id, @RequestBody Event event)
    {
        event.set_id(id);
        return eventService.updateEvent(event);
    }
    @GetMapping(value = "/findEvent/{id}")
    public Event findOneEvent(@PathVariable Long id)
    {
        return eventService.findEventById(id);
    }
    @GetMapping(value = "/events")
    public List<Event> findAll()
    {
        return eventService.findAllEvents();
    }
    @GetMapping(value = "/eventByTitle")
    public Event indByTitleLikeIgnoreCase(@RequestParam String title)
    {
        return eventService.findByTitleLikeIgnoreCase(title);
    }
    @GetMapping(value = "/eventByLocation")
    public Event findByLocationLikeIgnoreCase(@RequestParam String location)
    {
        return eventService.findByLocationLikeIgnoreCase(location);
    }
    @GetMapping(value = "/eventByStartDateInterval")
    public Event findByStartDateBetween(@RequestParam Date startDate, @RequestParam Date startDate2)
    {
        return eventService.findByStartBetween(startDate,startDate2);
    }
    @DeleteMapping(value = "/deleteEvent/{id}")
    public void deleteEvent(@PathVariable Long id)
    {
        eventService.deleteEvent(id);
    }
    @GetMapping("/eventsByMember/{idMember}")
    public List<Event> getAllEventsByMember(@PathVariable Long idMember)
    {
        return eventService.getAllEventsByMember(idMember);
    }
}
