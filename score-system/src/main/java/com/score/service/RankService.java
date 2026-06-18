package com.score.service;

import java.util.Map;

public interface RankService {

    /** 获取学生在某课程的排名（第几名） */
    Long getStudentRank(Long courseId, Long studentId);

    /** 获取某课程全班排名列表 */
    Map<String, Object> getCourseRankList(Long courseId);
}
