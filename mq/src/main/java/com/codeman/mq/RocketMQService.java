package com.codeman.mq;

import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
/**
 * @author hdgaadd
 * Created on 2021/12/12
 */
@Service
public class RocketMQService {
    @Resource
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
