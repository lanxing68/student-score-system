package com.score.strategy;

import java.math.BigDecimal;

/**
 * 评分策略接口 — 所有评分规则实现此接口
 */
public interface ScoreStrategy {

    /**
     * 计算加权总分
     * @param classPerformance 课堂表现 0-100
     * @param experiment       实验 0-100
     * @param homework         课后作业 0-100
     * @param finalProject     大作业 0-100
     * @return 加权总分
     */
    BigDecimal calculate(BigDecimal classPerformance, BigDecimal experiment,
                         BigDecimal homework, BigDecimal finalProject);
}
