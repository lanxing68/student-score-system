package com.score.service;

import com.score.entity.Score;

import jakarta.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface ScoreService {

    void enterScore(Score score);

    void updateScore(Score score);

    Score getById(Long id);

    List<Score> getMyScores(Long studentId);

    List<Score> getCourseScores(Long courseId);

    Map<String, Object> batchImport(InputStream inputStream, Long courseId);

    void exportScores(Long courseId, HttpServletResponse response);
}
