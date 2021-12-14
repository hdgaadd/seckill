package com.codeman.service.impl;

import com.alibaba.fastjson.JSON;
import com.codeman.component.RedisService;
import com.codeman.domain.SeckillActivity;
import com.codeman.domain.SeckillOrder;
import com.codeman.mapper.SeckillActivityMapper;
import com.codeman.component.RocketMQService;
import com.codeman.service.SeckillOversellService;
import com.codeman.util.SnowFlake;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hdgaadd
 * Created on 2021/12/10 00:00:11
 */
@Service
public class SeckillOversellServiceImpl implements SeckillOversellService {
    @Resource
    private RedisService redisService;
    @Resource
    private SeckillActivityMapper seckillActivityMapper;
    @Resource
    private RocketMQService rocketMQService;

    private final SnowFlake snowFlake = new SnowFlake(1, 1);

    @Override
    public Boolean stockDeductVaildator(Long activityId) {
        // 通过Jedis连接池锁定库存
        Boolean result = redisService.stockDeductVaildator(activityId);
        return result;
    }

    @Override
    public String createOrder(Long activityId, Long userId) throws Exception {
        SeckillActivity activity = seckillActivityMapper.selectById(activityId);
        SeckillOrder seckillOrder = new SeckillOrder();
        // id使用雪花id
        seckillOrder.setCode(String.valueOf(snowFlake.nextId()));
        seckillOrder.setUserId(userId);
        seckillOrder.setSeckillActivityId(activityId);
        seckillOrder.setCommodityId(activity.getCommodityId());
        seckillOrder.setAmount(activity.getSeckillPrice());
        seckillOrder.setOrderStatus(1);
        // 把订单传递给消息队列，去创建订单
        Boolean result = sentRocketMQ(seckillOrder);
        return result ? seckillOrder.getCode() : "订单创建失败";
    }

    private Boolean sentRocketMQ(SeckillOrder seckillOrder) throws Exception {
        rocketMQService.sendMessage("createOrder", JSON.toJSONString(seckillOrder));
        rocketMQService.sendMessage("pay_check", JSON.toJSONString(seckillOrder), 5);
        return true;
    }
}
