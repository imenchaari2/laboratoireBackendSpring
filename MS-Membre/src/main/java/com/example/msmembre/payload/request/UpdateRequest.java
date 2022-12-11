package com.example.msmembre.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequest {
    private Long id;
    private String firstName;
    private String lastName;
    private String cin;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date birthDate;
    private Date inscriptionDate;
    private String diploma;
    private String type;
    private String grade;
    private String etablishment;

}
