package com.score.controller;

import com.score.annotation.RequireRole;
import com.score.common.Result;
import com.score.entity.Score;
import com.score.service.ScoreService;
import com.score.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/score")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    /** 教师单条录入成绩 */
    @RequireRole("TEACHER")
    @PostMapping("/enter")
    public Result enterScore(@RequestBody Score score) {
        scoreService.enterScore(score);
        return Result.ok("录入成功");
    }

    /** 教师修改成绩 */
    @RequireRole("TEACHER")
    @PutMapping("/{id}")
    public Result updateScore(@PathVariable Long id, @RequestBody Score score) {
        score.setId(id);
        scoreService.updateScore(score);
        return Result.ok("修改成功");
    }

    /** 学生查看自己的所有成绩 */
    @RequireRole("STUDENT")
    @GetMapping("/my")
    public Result getMyScores() {
        Long studentId = SecurityUtils.getCurrentUserId();
        return Result.ok("获取成功", scoreService.getMyScores(studentId));
    }

    /** 查看某课程全班成绩（教师或学生均可） */
    @GetMapping("/course/{courseId}")
    public Result getCourseScores(@PathVariable Long courseId) {
        return Result.ok("获取成功", scoreService.getCourseScores(courseId));
    }
}
