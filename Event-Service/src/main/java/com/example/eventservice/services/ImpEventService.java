package com.example.eventservice.services;

import com.example.eventservice.beans.MemberBean;
import com.example.eventservice.entities.Event;
import com.example.eventservice.proxies.MemberProxy;
import com.example.eventservice.repositories.EventRepository;
import com.google.common.collect.Lists;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ImpEventService implements IEventService {
    @Autowired
    EventRepository eventRepository;
    @Autowired
    MemberProxy memberProxy;

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
        Event event1 = eventRepository.findById(event.get_id()).get();
        event.setMemberName(event1.getMemberName());
        event.setMemberId(event1.getMemberId());
        return this.eventRepository.saveAndFlush(event);
    }

    @Override
    public Event findEventById(long id) {
        return this.eventRepository.findById(id).get();
    }

    @Override
    public List<Event> findAllEvents() {
        List<Event> events = eventRepository.findAll();
        List<MemberBean> members = memberProxy.getAllMembers();
        List<Long> presentMembersIds = new ArrayList<>();
        members.forEach(member -> {
            presentMembersIds.add(member.getId());
        });
        events.forEach(event -> {
                if (!presentMembersIds.contains(event.getMemberId())) {
//                    event.setMemberId(null);
//                    event.setMemberName(null);
//                    eventRepository.saveAndFlush(event);
                    deleteEvent(event.get_id());
                } else {
                    MemberBean memberBean1 = memberProxy.getMemberById(event.getMemberId()).get();
                    if (!Objects.equals(memberBean1.getRole(), "ROLE_ADMIN")) {
                        event.setMemberName(memberBean1.getFirstName() + " " + memberBean1.getLastName());
                        eventRepository.saveAndFlush(event);
                    } else {
                        event.setMemberName("ADMIN");
                        eventRepository.saveAndFlush(event);
                    }
                }
        });
        return events;
    }

    @Override
    public Event findByStartBetween(@NonNull Date startDate, @NonNull Date startDate2) {
        return eventRepository.findByStartBetween(startDate, startDate2);
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
