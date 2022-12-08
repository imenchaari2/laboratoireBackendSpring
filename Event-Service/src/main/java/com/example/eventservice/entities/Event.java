package com.example.eventservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long _id;
    private Long creatorId;
    @ElementCollection
    private List<Long> membersIds = new ArrayList<>();
    @ElementCollection
    private List<String> membersNames = new ArrayList<>();

    @NonNull
    private String title;
    @Temporal(TemporalType.DATE)
    @NonNull
    private Date start;
    @Temporal(TemporalType.DATE)
    @NonNull
    private Date end;
    @NonNull
    private String location;
}
