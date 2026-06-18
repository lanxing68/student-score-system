package com.score.strategy;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 评分策略工厂 — 根据 scoreType 返回对应的策略实现
 * 新增评分规则：只需新增策略类 + 在这里加一行，不改原有代码
 */
@Component
public class ScoreStrategyFactory {

    private final Map<String, ScoreStrategy> strategyMap;

    /**
     * Spring 自动将所有 ScoreStrategy 的实现注入 Map
     * key = Bean 名称, value = 策略实例
     */
    public ScoreStrategyFactory(Map<String, ScoreStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    /**
     * 获取评分策略
     * @param scoreType "NORMAL" 或 "SPECIAL"
     */
    public ScoreStrategy getStrategy(String scoreType) {
        ScoreStrategy strategy = strategyMap.get(scoreType.toLowerCase() + "ScoreStrategy");
        if (strategy == null) {
            throw new IllegalArgumentException("未知的评分类型: " + scoreType);
        }
        return strategy;
    }
}
