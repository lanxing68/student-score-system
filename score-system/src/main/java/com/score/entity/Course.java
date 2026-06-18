package com.score.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Course {
    private Long id;
    private String courseName;
    private String term;         // 如 "2024-2025-1"
    private Long teacherId;
    private String teacherName;  // 联表查询用，不是数据库字段
    private LocalDateTime createTime;
}
