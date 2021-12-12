package com.codeman.mq;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author hdgaadd
 * Created on 2021/12/12
 */
@Component
@RocketMQMessageListener(topic = "testTopic", consumerGroup = "testGroup")
public class Listener implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt messageExt) {
        try {
            String message = new String(messageExt.getBody(), "UTF-8");
            System.out.println("接收到消息：" + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
