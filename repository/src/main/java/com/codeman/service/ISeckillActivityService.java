package com.codeman.service;

import com.codeman.domain.SeckillActivity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hdgaadd
 * @since 2021-12-09
 */
public interface ISeckillActivityService extends IService<SeckillActivity> {

    String purchase(Integer activityId);
}