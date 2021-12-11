package com.codeman.component;

import com.codeman.domain.SeckillActivity;
import com.codeman.mapper.SeckillActivityMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hdgaadd
 * Created on 2021/12/10/00:43
 */
@Component
public class RedisStartApplication implements ApplicationRunner {
    @Resource
    private RedisService redisService;
    @Resource
    private SeckillActivityMapper seckillActivityMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<SeckillActivity> seckillActivities = seckillActivityMapper.selectList(null);
        for (SeckillActivity activity : seckillActivities) {
            redisService.setKey("stockId:" + activity.getId(), activity.getTotalStock());
        }
        System.out.println("--------------------redis初始化成功--------------------------");
    }
}
