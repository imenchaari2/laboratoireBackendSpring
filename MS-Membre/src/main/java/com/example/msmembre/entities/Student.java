package com.example.msmembre.entities;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("std")
@EqualsAndHashCode(callSuper = true)
public class Student extends Member implements Serializable {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date inscriptionDate;
    private String diploma;
    @ManyToOne
    private TeacherResearcher supervisor;
    public Student( String email, String password) {
        this.setEmail(email);
        this.setPassword(password);
    }
}
