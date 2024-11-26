package com.sky.controller.admin;

import com.sky.dto.DataOverViewQueryDTO;
import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.SalesTop10ReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 程浩然
 * @Create: 2024/11/26 - 14:43
 * @Description: 统计报表相关接口
 */
@RestController
@RequestMapping("/admin/report")
@Slf4j
@Api(tags = "统计报表相关接口")
public class ReportController {
    @Autowired
    private ReportService reportService;


    @ApiOperation("查询销量排名top10接口")
    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> topTen(DataOverViewQueryDTO dataOverViewQueryDTO) {
        SalesTop10ReportVO salesTop10ReportVO = reportService.topTen(dataOverViewQueryDTO);
        return Result.success(salesTop10ReportVO);
    }
}