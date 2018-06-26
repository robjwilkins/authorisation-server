package com.wilkins.authorisation.server.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private List<String> roles;
}
