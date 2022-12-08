package com.example.eventservice.beans;

import lombok.Data;
@Data
public class MemberBean {
    private Long id;
    private String firstName;
    private String lastName;
    private String type;
    private String cin;
    private String email;
    private String role;
}
