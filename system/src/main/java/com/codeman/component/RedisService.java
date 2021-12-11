package com.codeman.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;

/**
 * @author hdgaadd
 * Created on 2021/12/10/00:34
 */
@Service
public class RedisService {
    @Autowired
    private JedisPool jedisPool;

    private static int accessCount = 0;

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
        System.out.println("--------------------redis脚本执行--------------------------");
        String key = "stockId:" + activityId;
        try (Jedis jedisClient = jedisPool.getResource()) {
            String script = "if redis.call('exists', KEYS[1]) == 1 then\n" +
                    "	local stock = tonumber(redis.call('get', KEYS[1]))\n" +
                    "	if (stock <= 0) then\n" +
                    "		return -1\n" +
                    "	end;\n" +
                    "	redis.call('decr', KEYS[1]);\n" +
                    "	return stock - 1;\n" +
                    "end;\n" +
                    "return -1;";
            Long stock = (Long) jedisClient.eval(script, Collections.singletonList(key), Collections.emptyList());
            if (stock < 0) {
                System.out.println("--------------------------------库存不足----------------------------");
                return false;
            }
            System.out.println("--------------------第"+ accessCount++ +"次：purchase succeed--------------------------");
            return true;
        } catch (Throwable throwable) {
            System.out.println("error" + throwable.toString());
            return false;
        }
    }
}
