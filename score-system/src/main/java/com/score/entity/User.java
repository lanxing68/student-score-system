package com.score.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String role;        // STUDENT / TEACHER
    private LocalDateTime createTime;
}
