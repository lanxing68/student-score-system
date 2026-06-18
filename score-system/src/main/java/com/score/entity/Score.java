package com.score.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Score {
    private Long id;
    private Long studentId;
    private Long courseId;
    private BigDecimal classPerformance;
    private BigDecimal experiment;
    private BigDecimal homework;
    private BigDecimal finalProject;
    private String scoreType;    // NORMAL / SPECIAL
    private BigDecimal total;
    private LocalDateTime createTime;

    // 联表查询用
    private String studentName;
    private String studentNo;
    private String courseName;
    private String term;
}
