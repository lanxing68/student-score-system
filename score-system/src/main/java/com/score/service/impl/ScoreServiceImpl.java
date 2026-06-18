package com.score.service.impl;

import com.score.entity.Score;
import com.score.mapper.ScoreMapper;
import com.score.service.ScoreService;
import com.score.strategy.ScoreStrategy;
import com.score.strategy.ScoreStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private ScoreStrategyFactory strategyFactory;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String RANK_KEY_PREFIX = "rank:course:";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enterScore(Score score) {
        // 1. 校验是否存在已有成绩（同一学生同课程只存一条）
        Score exist = scoreMapper.selectByStudentAndCourse(score.getStudentId(), score.getCourseId());
        if (exist != null) {
            score.setId(exist.getId());
            updateScore(score);
            return;
        }
        // 2. 策略计算总分
        ScoreStrategy strategy = strategyFactory.getStrategy(score.getScoreType());
        score.setTotal(strategy.calculate(
                score.getClassPerformance(), score.getExperiment(),
                score.getHomework(), score.getFinalProject()));
        // 3. 写入数据库
        scoreMapper.insert(score);
        // 4. 更新 Redis ZSet 排名
        redisTemplate.opsForZSet().add(RANK_KEY_PREFIX + score.getCourseId(),
                score.getStudentId(), score.getTotal().doubleValue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateScore(Score score) {
        ScoreStrategy strategy = strategyFactory.getStrategy(score.getScoreType());
        score.setTotal(strategy.calculate(
                score.getClassPerformance(), score.getExperiment(),
                score.getHomework(), score.getFinalProject()));
        scoreMapper.update(score);
        redisTemplate.opsForZSet().add(RANK_KEY_PREFIX + score.getCourseId(),
                score.getStudentId(), score.getTotal().doubleValue());
    }

    @Override
    public Score getById(Long id) {
        return scoreMapper.selectById(id);
    }

    @Override
    public List<Score> getMyScores(Long studentId) {
        return scoreMapper.selectByStudentId(studentId);
    }

    @Override
    public List<Score> getCourseScores(Long courseId) {
        return scoreMapper.selectByCourseId(courseId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchImport(List<Score> scores) {
        for (Score score : scores) {
            enterScore(score);
        }
    }
}
