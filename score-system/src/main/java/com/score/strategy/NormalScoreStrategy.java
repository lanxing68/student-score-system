package com.score.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 普通加分策略 — 四项成绩按固定权重加权求和
 * 权重：课堂表现 20% + 实验 20% + 课后作业 30% + 大作业 30%
 */
@Component("normalScoreStrategy")
public class NormalScoreStrategy implements ScoreStrategy {

    private static final BigDecimal CP_WEIGHT = new BigDecimal("0.2");
    private static final BigDecimal EXP_WEIGHT = new BigDecimal("0.2");
    private static final BigDecimal HW_WEIGHT = new BigDecimal("0.3");
    private static final BigDecimal FP_WEIGHT = new BigDecimal("0.3");

    @Override
    public BigDecimal calculate(BigDecimal classPerformance, BigDecimal experiment,
                                BigDecimal homework, BigDecimal finalProject) {
        return classPerformance.multiply(CP_WEIGHT)
                .add(experiment.multiply(EXP_WEIGHT))
                .add(homework.multiply(HW_WEIGHT))
                .add(finalProject.multiply(FP_WEIGHT))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
