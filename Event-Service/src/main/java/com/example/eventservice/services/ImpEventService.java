package com.example.eventservice.services;

import com.example.eventservice.beans.MemberBean;
import com.example.eventservice.entities.Event;
import com.example.eventservice.proxies.MemberProxy;
import com.example.eventservice.repositories.EventRepository;
import com.google.common.collect.Lists;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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
    public Event addEvent(Event event, Long idCreator) {
        event.setCreatorId(idCreator);
        event.getMembersIds().add(idCreator);
        return this.eventRepository.saveAndFlush(event);
    }

    @Override
    public void deleteEvent(Long id) {
        this.eventRepository.deleteById(id);
    }

    @Override
    public Event affectMembersToEvent(List<Long> ids, Long idEvent) {
        Event event1 = eventRepository.findById(idEvent).get();
        List<String> names = Lists.newArrayList();
        List<Long> membersIds = Lists.newArrayList();
        event1.getMembersIds().clear();
        membersIds.add(event1.getCreatorId());
        ids.forEach(id -> {
            if (!membersIds.contains(id)) {
                membersIds.add(id);
            }
        });
        event1.setMembersIds(membersIds);
        return getEvent(event1, names);
    }

    private Event getEvent(Event event1, List<String> names) {
        event1.getMembersIds().forEach(id -> {
            MemberBean memberBean1 = memberProxy.getMemberById(id).get();
            if (!Objects.equals(memberBean1.getRole(), "ROLE_ADMIN")) {
                names.add(memberBean1.getFirstName() + " " + memberBean1.getLastName());
            } else {
                names.add("ADMIN");
            }
        });
        event1.getMembersNames().clear();
        event1.setMembersNames(names);
        return this.eventRepository.saveAndFlush(event1);
    }

    @Override
    public List<Event> findAllEvents() {
        List<Event> events = eventRepository.findAll();
        List<MemberBean> members = memberProxy.getAllMembers();
        List<Long> presentMembersIds = new ArrayList<>();
        List<String> presentMembersNames = new ArrayList<>();
        members.forEach(member -> {
            presentMembersIds.add(member.getId());
            presentMembersNames.add("ADMIN");
            presentMembersNames.add(member.getFirstName() + " " + member.getLastName());
        });
        events.forEach(event -> {
                    event.getMembersIds().forEach(id -> {
                        if (!presentMembersIds.contains(id)) {
                            event.getMembersIds().remove(id);
                        }
                    });
                    event.getMembersNames().forEach(name -> {
                        System.out.println(presentMembersNames.contains(name));
                        if (!presentMembersNames.contains(name)) {
                            event.getMembersNames().remove(name);
                        }
                    });
                    eventRepository.saveAndFlush(event);
                }
        );
        return events;
    }

    @Override
    public Event updateEvent(Event event) {
        Event event1 = eventRepository.findById(event.get_id()).get();
        event.setMembersNames(event1.getMembersNames());
        event.setMembersIds(event1.getMembersIds());
        event.setCreatorId(event1.getCreatorId());
        return this.eventRepository.saveAndFlush(event);
    }

    @Override
    public Event findEventById(Long id) {
        return this.eventRepository.findById(id).get();
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
        events.forEach(event -> {
            if (event.getMembersIds().contains(idMember)) {
                eventsByMember.add(event);
            }
        });
        return eventsByMember;
    }
}
