CREATE TABLE IF NOT EXISTS user (
                                    7     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    8     username VARCHAR(50) NOT NULL UNIQUE,
    7     id BIGINT AUTO_INCREMENT PRIMARY KEY,
    8     username VARCHAR(50) NOT NULL UNIQUE,
    9     password VARCHAR(255) NOT NULL,
    10     role VARCHAR(20) NOT NULL COMMENT 'STUDENT 或 TEACHER',
    11     create_time DATETIME DEFAULT CURRENT_TIMESTAMP
    12 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
13
  14 CREATE TABLE IF NOT EXISTS student (
                                            15     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            16     user_id BIGINT COMMENT '关联user表，可为空',
                                            17     student_no VARCHAR(20) NOT NULL UNIQUE,
    18     name VARCHAR(50) NOT NULL,
    19     class_name VARCHAR(50),
    20     create_time DATETIME DEFAULT CURRENT_TIMESTAMP
    21 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
22
  23 CREATE TABLE IF NOT EXISTS course (
                                           24     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           25     course_name VARCHAR(100) NOT NULL,
    26     term VARCHAR(20) NOT NULL COMMENT '学期，如 2024-2025-1',
    27     teacher_id BIGINT NOT NULL COMMENT '授课教师 user_id',
    28     create_time DATETIME DEFAULT CURRENT_TIMESTAMP
    29 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
30
  31 CREATE TABLE IF NOT EXISTS score (
                                          32     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          33     student_id BIGINT NOT NULL,
                                          34     course_id BIGINT NOT NULL,
                                          35     class_performance DECIMAL(5,2) COMMENT '课堂表现 0-100',
    36     experiment DECIMAL(5,2) COMMENT '实验 0-100',
    37     homework DECIMAL(5,2) COMMENT '课后作业 0-100',
    38     final_project DECIMAL(5,2) COMMENT '大作业 0-100',
    39     score_type VARCHAR(10) COMMENT 'NORMAL 或 SPECIAL',
    40     total DECIMAL(6,2) COMMENT '加权总分',
    41     create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    42     UNIQUE KEY uk_student_course (student_id, course_id)
    43 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;