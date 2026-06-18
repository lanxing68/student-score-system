package com.score.utils;

import com.score.entity.Score;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

public class ExcelUtils {

    /**
     * 读取成绩导入Excel：学号|课堂表现|实验|课后作业|大作业|评分类型
     * @return Map: "successList" → 成功列表, "failList" → 失败列表
     */
    public static Map<String, List<Object>> readScores(InputStream inputStream) {
        List<Score> successList = new ArrayList<>();
        List<Map<String, Object>> failList = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {  // 跳过表头
                Row row = sheet.getRow(i);
                if (row == null) continue;
                try {
                    String studentNo = getCellString(row, 0);
                    BigDecimal cp = new BigDecimal(getCellString(row, 1));
                    BigDecimal exp = new BigDecimal(getCellString(row, 2));
                    BigDecimal hw = new BigDecimal(getCellString(row, 3));
                    BigDecimal fp = new BigDecimal(getCellString(row, 4));
                    String scoreType = getCellString(row, 5).toUpperCase();

                    if (!"NORMAL".equals(scoreType) && !"SPECIAL".equals(scoreType)) {
                        throw new IllegalArgumentException("评分类型必须填 NORMAL 或 SPECIAL");
                    }

                    Score s = new Score();
                    s.setStudentNo(studentNo);
                    s.setClassPerformance(cp);
                    s.setExperiment(exp);
                    s.setHomework(hw);
                    s.setFinalProject(fp);
                    s.setScoreType(scoreType);
                    successList.add(s);
                } catch (Exception e) {
                    Map<String, Object> fail = new HashMap<>();
                    fail.put("row", i + 1);
                    fail.put("reason", e.getMessage());
                    failList.add(fail);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Excel 读取失败: " + e.getMessage());
        }

        Map<String, List<Object>> result = new HashMap<>();
        result.put("success", (List) successList);
        result.put("fail", (List) failList);
        return result;
    }

    /**
     * 导出成绩单Excel：学号|姓名|课堂表现|实验|课后作业|大作业|评分类型|总分|排名
     */
    public static void writeScores(List<Map<String, Object>> dataList, OutputStream outputStream) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("成绩单");
            String[] headers = {"排名", "学号", "姓名", "课堂表现", "实验", "课后作业", "大作业", "评分类型", "总分"};

            // 表头样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            // 写表头
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 写数据行
            int rowNum = 1;
            for (Map<String, Object> data : dataList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(String.valueOf(data.get("rank")));
                row.createCell(1).setCellValue(String.valueOf(data.get("studentNo")));
                row.createCell(2).setCellValue(String.valueOf(data.get("studentName")));
                row.createCell(3).setCellValue(String.valueOf(data.get("classPerformance")));
                row.createCell(4).setCellValue(String.valueOf(data.get("experiment")));
                row.createCell(5).setCellValue(String.valueOf(data.get("homework")));
                row.createCell(6).setCellValue(String.valueOf(data.get("finalProject")));
                row.createCell(7).setCellValue(String.valueOf(data.get("scoreType")));
                row.createCell(8).setCellValue(String.valueOf(data.get("total")));
            }

            workbook.write(outputStream);
        } catch (Exception e) {
            throw new RuntimeException("Excel 导出失败: " + e.getMessage());
        }
    }

    private static String getCellString(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) throw new IllegalArgumentException("第" + col + "列为空");
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }
}
