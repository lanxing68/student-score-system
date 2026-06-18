package com.score.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 特别加分策略 — 在普通加分基础上乘以 1.2 特别系数
 * 用于教师对难度高、回答好的作业给予额外加权
 */
@Component("specialScoreStrategy")
public class SpecialScoreStrategy implements ScoreStrategy {

    private static final BigDecimal BONUS_FACTOR = new BigDecimal("1.2");

    @Override
    public BigDecimal calculate(BigDecimal classPerformance, BigDecimal experiment,
                                BigDecimal homework, BigDecimal finalProject) {
        // 先按普通权重算分，再乘特别系数
        NormalScoreStrategy normal = new NormalScoreStrategy();
        BigDecimal normalScore = normal.calculate(classPerformance, experiment, homework, finalProject);
        return normalScore.multiply(BONUS_FACTOR).min(new BigDecimal("100"))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
