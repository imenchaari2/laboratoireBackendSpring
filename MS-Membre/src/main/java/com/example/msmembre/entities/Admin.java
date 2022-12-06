package com.example.msmembre.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("adm")
@EqualsAndHashCode(callSuper = true)
public class Admin extends Member implements Serializable {
    public Admin(String email, String encode) {
        this.setEmail(email);
        this.setPassword(encode);
    }
}
