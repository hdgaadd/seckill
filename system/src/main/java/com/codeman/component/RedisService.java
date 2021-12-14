package com.codeman.component;

import com.codeman.constant.RedisKey;
import com.codeman.domain.SeckillOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import util.LOG;

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

    /**
     * 通过Jedis连接池锁定库存
     * @param activityId
     * @return
     */
    public Boolean stockDeductVaildator(Long activityId) { // [dɪˈdʌkt]['vali,deitə]减去 验证器
        System.out.println("--------------------redis脚本执行--------------------------");
        String key = "stock:" + activityId;
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
            System.out.println("--------------------秒杀请求为第"+ accessCount++ +"次：purchase succeed--------------------------");
            return true;
        } catch (Throwable throwable) {
            System.out.println("error" + throwable.toString());
            return false;
        }
    }

    public Boolean addLimitUser(Long seckillActivityId, Long userId) {
        Jedis resource = jedisPool.getResource();
        resource.sadd("limitUser:" + seckillActivityId,  String.valueOf(userId));
        resource.close();
        return true;
    }

    /**
     * 恢复Redis库存
     * @param order
     */
    public void revertStock(SeckillOrder order) {
        Jedis resource = jedisPool.getResource();
        resource.incr("stock:"  + order.getSeckillActivityId());
        LOG.log("恢复Redis库存成功");
        resource.close();
    }

    /**
     * 将用户从锁定状态解除
     * @param order
     */
    public void removeLimitMember(SeckillOrder order) {
        Jedis resource = jedisPool.getResource();
        resource.srem("limitUser:" + order.getSeckillActivityId(), String.valueOf(order.getUserId()));
        resource.close();
    }
}
