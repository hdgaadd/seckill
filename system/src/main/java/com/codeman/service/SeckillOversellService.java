package com.codeman.service;

/**
 * @author hdgaadd
 * Created on 2021/12/10 00:00:11
*/
public interface SeckillOversellService {
    /**
     * 根据活动Id锁定库存，秒杀成功
     * @param activityId
     * @return
     */
    Boolean stockDeductVaildator(Long activityId);

    /**
     * 秒杀成功后，创建订单
     * @param activityId
     * @param userId
     * @return
     */
    String createOrder(Long activityId, Long userId) throws Exception;
}