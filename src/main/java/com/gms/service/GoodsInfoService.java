package com.gms.service;

import com.gms.common.Result;
import com.gms.entity.GoodsInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tszwaidai
 * @since 2024-06-07
 */
public interface GoodsInfoService extends IService<GoodsInfo> {

    Result getInfoList(String goodsname, String typename, Long pageNo, Long pageSize);

    Result addInfo(GoodsInfo goodsInfo);

    Result updateInfo(GoodsInfo goodsInfo);

    Result getInfoById(Integer id);

    Result deleteById(Integer id);

    Result updateStatus(GoodsInfo goodsInfo);

    Result applyGoods(Map<String, Integer> request);
}
