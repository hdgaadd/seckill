package com.codeman.mq;

import com.alibaba.fastjson.JSON;
import com.codeman.domain.SeckillActivity;
import com.codeman.domain.SeckillOrder;
import com.codeman.mapper.SeckillActivityMapper;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import util.LOG;

import javax.annotation.Resource;

/**
 * @author hdgaadd
 * Created on 2021/12/12
 */
@Component
@RocketMQMessageListener(topic = "payDone", consumerGroup = "payDoneGroup")
public class PayCustomListener implements RocketMQListener<MessageExt> {
    @Resource
    private SeckillActivityMapper seckillActivityMapper;

    @Override
    public void onMessage(MessageExt messageExt) {
        try {
            String message = new String(messageExt.getBody(), "UTF-8");
            SeckillOrder order = JSON.parseObject(message, SeckillOrder.class);
            Boolean result = updateActivity(order.getSeckillActivityId());
            if (result) {
                System.out.println("更新库存成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean updateActivity(Long seckillActivityId) {
        SeckillActivity activity = seckillActivityMapper.selectById(seckillActivityId);
        activity.setLockStock(activity.getLockStock() - 1);
        activity.setTotalStock(activity.getTotalStock() - 1);
        seckillActivityMapper.update(activity, null);
        LOG.log("更新活动表成功");
        return true;
    }
}
