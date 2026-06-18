package com.score.controller;

import com.score.annotation.RequireRole;
import com.score.common.Result;
import com.score.service.RankService;
import com.score.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rank")
public class RankController {

    @Autowired
    private RankService rankService;

    /** 学生在某课程中的排名 */
    @RequireRole("STUDENT")
    @GetMapping("/my")
    public Result getMyRank(@RequestParam Long courseId) {
        Long studentId = SecurityUtils.getCurrentUserId();
        Long rank = rankService.getStudentRank(courseId, studentId);
        return Result.ok("获取成功", rank);
    }

    /** 某课程全班排名 */
    @GetMapping("/course/{courseId}")
    public Result getCourseRank(@PathVariable Long courseId) {
        return Result.ok("获取成功", rankService.getCourseRankList(courseId));
    }
}
