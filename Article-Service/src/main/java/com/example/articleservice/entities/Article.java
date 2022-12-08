package com.example.articleservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)

public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;
    private Long creatorId;
    @ElementCollection
    private List<Long> membersIds = new ArrayList<>();
    @ElementCollection
    private List<String> membersNames = new ArrayList<>();
    @NonNull
    private String title;
    @NonNull
    private String type; //newspaper article/ event/book chapter/book/poster
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date createdDate;
    @NonNull
    private String url;
    @OneToOne
    private File pdfSource;
//    @Transient
//    private MemberBean author;
}
