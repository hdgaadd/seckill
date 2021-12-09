package com.codeman.controller;


import com.codeman.service.ISeckillActivityService;
import com.codeman.service.impl.SeckillActivityServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hdgaadd
 * @since 2021-12-09
 */
//codeman/seckill-activity/activityId
@RestController
@RequestMapping("/codeman/seckill-activity")
public class SeckillActivityController {
    @Resource
    private ISeckillActivityService seckillActivityService;
    @GetMapping("/activityId/{activityId}")
    public String purchase(@PathVariable Integer activityId) {
        // [ˈpɜːtʃəs]
        String result = seckillActivityService.purchase(activityId);
        return result;
    }
}

