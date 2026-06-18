package com.score.service.impl;

import com.score.entity.Score;
import com.score.mapper.ScoreMapper;
import com.score.service.ScoreService;
import com.score.strategy.ScoreStrategy;
import com.score.strategy.ScoreStrategyFactory;
import com.score.utils.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ScoreStrategyFactory strategyFactory;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String RANK_KEY_PREFIX = "rank:course:";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enterScore(Score score) {
        Score exist = scoreMapper.selectByStudentAndCourse(score.getStudentId(), score.getCourseId());
        if (exist != null) {
            score.setId(exist.getId());
            updateScore(score);
            return;
        }
        ScoreStrategy strategy = strategyFactory.getStrategy(score.getScoreType());
        score.setTotal(strategy.calculate(
                score.getClassPerformance(), score.getExperiment(),
                score.getHomework(), score.getFinalProject()));
        scoreMapper.insert(score);
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
    public Map<String, Object> batchImport(InputStream inputStream, Long courseId) {
        Map<String, List<Object>> parsed = ExcelUtils.readScores(inputStream);
        List<Score> successList = (List) parsed.get("success");
        List<Map<String, Object>> failList = (List) parsed.get("fail");

        int successCount = 0;
        for (Score s : successList) {
            try {
                // 根据学号查学生ID
                Long studentId = jdbcTemplate.queryForObject(
                        "SELECT id FROM student WHERE student_no = ?", Long.class, s.getStudentNo());
                s.setStudentId(studentId);
                s.setCourseId(courseId);
                enterScore(s);
                successCount++;
            } catch (Exception e) {
                Map<String, Object> fail = new HashMap<>();
                fail.put("studentNo", s.getStudentNo());
                fail.put("reason", "学号不存在或导入失败: " + e.getMessage());
                failList.add(fail);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("successCount", successCount);
        result.put("failCount", failList.size());
        result.put("failList", failList);
        return result;
    }

    @Override
    public void exportScores(Long courseId, HttpServletResponse response) {
        try {
            List<Score> scores = scoreMapper.selectByCourseId(courseId);
            List<Map<String, Object>> dataList = new ArrayList<>();
            int rank = 1;
            for (Score s : scores) {
                Map<String, Object> row = new HashMap<>();
                row.put("rank", rank++);
                row.put("studentNo", s.getStudentNo());
                row.put("studentName", s.getStudentName());
                row.put("classPerformance", s.getClassPerformance());
                row.put("experiment", s.getExperiment());
                row.put("homework", s.getHomework());
                row.put("finalProject", s.getFinalProject());
                row.put("scoreType", s.getScoreType());
                row.put("total", s.getTotal());
                dataList.add(row);
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("成绩单", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            OutputStream out = response.getOutputStream();
            ExcelUtils.writeScores(dataList, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }
}
