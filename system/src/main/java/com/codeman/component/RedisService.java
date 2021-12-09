package com.codeman.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author hdgaadd
 * Created on 2021/12/10/00:34
 */
@Service
public class RedisService {
    @Autowired
    private JedisPool jedisPool;

    public void setKey(String key, Integer totalStock) {
        Jedis resource = jedisPool.getResource();
        resource.set(key, totalStock.toString());
        resource.close();
    }

    public String getKey(String key) {
        Jedis resource = jedisPool.getResource();
        String result = resource.get(key);
        resource.close();
        return result;
    }

    public Boolean stockDeductVaildator(Long activityId) { // [dɪˈdʌkt]['vali,deitə]减去 验证器
        return false;
    }
}
