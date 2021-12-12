package com.codeman;

import com.codeman.mq.RocketMQService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RelaxApplicationTests {

	@Resource
	private RocketMQService rocketMQService;

	@Test
	public void test() throws Exception {
		rocketMQService.sendMessage("testTopic", "测试消息");
	}
}