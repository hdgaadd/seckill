package com.codeman.mq;

import com.alibaba.fastjson.JSON;
import com.codeman.component.RedisService;
import com.codeman.domain.SeckillActivity;
import com.codeman.domain.SeckillOrder;
import com.codeman.mapper.SeckillActivityMapper;
import com.codeman.service.RocketmqService;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.data.mapping.model.PreferredConstructorDiscoverer;
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
@RocketMQMessageListener(topic = "pay_check", consumerGroup = "payCheckGroup")
public class PayCheckConsumeListener implements RocketMQListener<MessageExt> {
    @Resource
    private RocketmqService rocketmqService;
    @Resource
    private RedisService redisService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void onMessage(MessageExt messageExt) {
        try {
            String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
            SeckillOrder order = JSON.parseObject(message, SeckillOrder.class);
            if (order.getOrderStatus() != 2) {
                order.setOrderStatus(3);
                // 订单超时，更新数据库库存
                rocketmqService.revertDataBase(order);
                // 订单超时，更新Redis库存
                redisService.revertStock(order);
                // 将用户从锁定状态解除
                redisService.removeLimitMember(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
