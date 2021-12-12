package com.codeman.mq;

import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hdgaadd
 * Created on 2021/12/12
 */
@Service
public class RocketMQService {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendMessage(String topic, String messageBody) throws Exception {
        Message message = new Message();
        message.setBody(messageBody.getBytes());
        rocketMQTemplate.getProducer().send(message);
    }

    public void sendMessage(String topic, String messageBody, int format) throws Exception {
        Message message = new Message();
        message.setDelayTimeLevel(format);
        message.setBody(messageBody.getBytes());
        rocketMQTemplate.getProducer().send(message);
    }
}
