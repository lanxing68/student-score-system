-- ==========================================
--  学生学习情况登记系统 - 数据库初始化脚本
--  使用：Navicat 中选中 score_system 库后执行
-- ==========================================

CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL COMMENT 'STUDENT 或 TEACHER',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS student (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT COMMENT '关联user表，可为空',
    student_no VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL,
    class_name VARCHAR(50),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL,
    term VARCHAR(20) NOT NULL COMMENT '学期，如 2024-2025-1',
    teacher_id BIGINT NOT NULL COMMENT '授课教师 user_id',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS score (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    class_performance DECIMAL(5,2) COMMENT '课堂表现 0-100',
    experiment DECIMAL(5,2) COMMENT '实验 0-100',
    homework DECIMAL(5,2) COMMENT '课后作业 0-100',
    final_project DECIMAL(5,2) COMMENT '大作业 0-100',
    score_type VARCHAR(10) COMMENT 'NORMAL 或 SPECIAL',
    total DECIMAL(6,2) COMMENT '加权总分',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_student_course (student_id, course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
