package com.codeman.mq;

import com.alibaba.fastjson.JSON;
import com.codeman.domain.SeckillActivity;
import com.codeman.domain.SeckillOrder;
import com.codeman.mapper.SeckillActivityMapper;
import com.codeman.service.RocketmqService;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import util.LOG;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author hdgaadd
 * Created on 2021/12/12
 */
@Component
@RocketMQMessageListener(topic = "payDone", consumerGroup = "payDoneGroup")
public class PayConsumeListener implements RocketMQListener<MessageExt> {
    @Resource
    private SeckillActivityMapper seckillActivityMapper;
    @Resource
    private RocketmqService rocketmqService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void onMessage(MessageExt messageExt) {
        try {
            String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
            SeckillOrder order = JSON.parseObject(message, SeckillOrder.class);
            // 支付成功，更新数据库库存
            Boolean result = rocketmqService.updateActivity(order.getSeckillActivityId());
            if (result) {
                System.out.println("更新库存成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}