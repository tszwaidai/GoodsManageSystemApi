package com.gms.service;

import com.gms.common.Result;
import com.gms.entity.GoodsType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tszwaidai
 * @since 2024-06-07
 */
public interface GoodsTypeService extends IService<GoodsType> {

    Result getTypeList(String typename, Long pageNo, Long pageSize);

    Result getTypeCount();

    Result addType(GoodsType goodsType);

    Result updateType(GoodsType goodsType);

    Result getTypeById(Integer id);

    Result deleteTypeById(Integer id);
}
