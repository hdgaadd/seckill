package com.codeman.mq;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.codeman.component.RedisService;
import com.codeman.domain.SeckillActivity;
import com.codeman.domain.SeckillOrder;
import com.codeman.mapper.SeckillActivityMapper;
import com.codeman.mapper.SeckillOrderMapper;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author hdgaadd
 * Created on 2021/12/12
 */
@Component
@RocketMQMessageListener(topic = "createOrder", consumerGroup = "testGroup")
public class OrderCustomListener implements RocketMQListener<MessageExt> {
    @Resource
    private SeckillOrderMapper seckillOrderMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private SeckillActivityMapper seckillActivityMapper;

    @Override
    public void onMessage(MessageExt messageExt) {
        try {
            String message = new String(messageExt.getBody(), "UTF-8");
            System.out.println("-------------------------接收到消息----------------------------：" + message);
            SeckillOrder order = JSON.parseObject(message, SeckillOrder.class);
            // 限定库存-1
            int result =  updateOrder(order.getSeckillActivityId());
            if (result > 0) {
                System.out.println("-------------------------限定库存-1成功----------------------------");
                order.setOrderStatus(1);
                // 把该活动id+用户id作为key，加入到Jedis连接池里，作为限选用户
                Boolean ret =  redisService.addLimitUser(order.getSeckillActivityId(), order.getUserId());
                if (ret) {
                    System.out.println("-------------------------添加限选用户成功----------------------------");
                    seckillOrderMapper.insert(order);
                    System.out.println("-------------------------创建订单成功----------------------------");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int updateOrder(Long seckillActivityId) {
        QueryWrapper<SeckillActivity> seckillOrderQueryWrapper = new QueryWrapper<>();
        seckillOrderQueryWrapper.eq("id", seckillActivityId);
        SeckillActivity activity = seckillActivityMapper.selectById(seckillActivityId);
        activity.setLockStock(activity.getLockStock() + 1);
        int result = seckillActivityMapper.update(activity, seckillOrderQueryWrapper);
        return result;
    }
}
