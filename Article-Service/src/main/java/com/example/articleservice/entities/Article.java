package com.example.articleservice.entities;

import com.example.articleservice.beans.MemberBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)

public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;
    private Long authorId;
    private String authorName;
    @NonNull
    private String title;
    @NonNull
    private String type; //newspaper article/ event/book chapter/book/poster
    @Temporal(TemporalType.DATE)
    @NonNull
    private Date createdDate;
    @NonNull
    private String url;
    @NonNull
    private String pdfSource;
//    @Transient
//    private MemberBean author;
}
