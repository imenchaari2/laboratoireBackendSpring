package com.example.msmembre.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name= "type_mbr", discriminatorType = DiscriminatorType.STRING,length = 3)
public abstract class Member implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @Transient
    private String fullName;

    @NonNull
    private String cin;
    @NonNull
    private String email;
    @NonNull
    private String password;
    @OneToOne
    private File cv;
    @NonNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date birthDate;
    @Temporal(TemporalType.DATE)
    private Date createdDate;
    @OneToOne
    private File photo;
    private String role;



//    @Transient
//    List<ArticleBean> articles;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
