package com.score.mapper;

import com.score.entity.Score;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScoreMapper {

    int insert(Score score);

    int update(Score score);

    Score selectById(Long id);

    Score selectByStudentAndCourse(@Param("studentId") Long studentId,
                                   @Param("courseId") Long courseId);

    List<Score> selectByStudentId(Long studentId);

    List<Score> selectByCourseId(Long courseId);

    int deleteById(Long id);
}
