package com.example.eventservice.repositories;

import com.example.eventservice.entities.Event;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Event findByStartBetween(@NonNull Date startDate, @NonNull Date startDate2);
    Event findByTitleLikeIgnoreCase(@NonNull String title);
    Event findByLocationLikeIgnoreCase(@NonNull String title);
}
