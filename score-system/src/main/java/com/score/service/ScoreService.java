package com.score.service;

import com.score.entity.Score;

import java.util.List;

public interface ScoreService {

    void enterScore(Score score);

    void updateScore(Score score);

    Score getById(Long id);

    List<Score> getMyScores(Long studentId);

    List<Score> getCourseScores(Long courseId);

    void batchImport(List<Score> scores);
}
