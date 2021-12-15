package com.codeman.service.impl;

import com.codeman.domain.ProductSkuStock;
import com.codeman.mapper.CommodityMapper;
import com.codeman.service.ICommodityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hdgaadd
 * @since 2021-12-09
 */
@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, ProductSkuStock> implements ICommodityService {

}
