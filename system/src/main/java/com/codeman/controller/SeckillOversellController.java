package com.codeman.controller;

import com.codeman.service.SeckillOversellService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hdgaadd
 * Created on 2021/12/10 00:00:11
*/
@Api(tags = "秒杀")
@RestController
@RequestMapping("/")
// oversale售空[ˌəʊvəˈsel]
public class SeckillOversellController {
    @Resource
    private SeckillOversellService seckillOversellService;

    @GetMapping("/stockDeductVaildator/{activityId}")
    @ApiOperation("秒杀，创建订单，返回订单编号")
    public String stockDeductVaildator(@PathVariable Long activityId, Long userId) throws Exception {
        Boolean result = seckillOversellService.stockDeductVaildator(activityId);
        String orderCode = null;
        if (result) {
            orderCode = seckillOversellService.createOrder(activityId, userId);
        }
        return orderCode != null ? "秒杀成功，订单编号为：" + orderCode : "秒杀失败，库存不足";
    }

}
