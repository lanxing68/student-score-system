package com.score.controller;

import com.score.annotation.RequireRole;
import com.score.common.Result;
import com.score.entity.Score;
import com.score.service.ScoreService;
import com.score.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/score")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    /**
     * 教师单条录入成绩
     */
    @RequireRole("TEACHER")
    @PostMapping("/enter")
    public Result enterScore(@RequestBody Score score) {
        scoreService.enterScore(score);
        return Result.ok("录入成功");
    }

    /**
     * 教师Excel批量导入成绩
     */
    @RequireRole("TEACHER")
    @PostMapping("/batch")
    public Result batchImport(@RequestParam("file") MultipartFile file,
                              @RequestParam Long courseId) {
        try {
            return Result.ok("导入完成", scoreService.batchImport(file.getInputStream(), courseId));
        } catch (IOException e) {
            return Result.error("文件读取失败: " + e.getMessage());
        }
    }

    /**
     * 教师修改成绩
     */
    @RequireRole("TEACHER")
    @PutMapping("/{id}")
    public Result updateScore(@PathVariable Long id, @RequestBody Score score) {
        score.setId(id);
        scoreService.updateScore(score);
        return Result.ok("修改成功");
    }

    /**
     * 学生查看自己的所有成绩
     */
    @RequireRole("STUDENT")
    @GetMapping("/my")
    public Result getMyScores() {
        Long studentId = SecurityUtils.getCurrentUserId();
        return Result.ok("获取成功", scoreService.getMyScores(studentId));
    }

    /**
     * 查看某课程全班成绩（教师或学生均可）
     */
    @GetMapping("/course/{courseId}")
    public Result getCourseScores(@PathVariable Long courseId) {
        return Result.ok("获取成功", scoreService.getCourseScores(courseId));
    }

    /**
     * 导出某课程成绩单Excel
     */
    @RequireRole("TEACHER")
    @GetMapping("/export/{courseId}")
    public void exportScores(@PathVariable Long courseId, HttpServletResponse response){
        scoreService.exportScores(courseId, response);
    }
}
