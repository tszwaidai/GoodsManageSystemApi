package com.gms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gms.dto.LostDTO;
import com.gms.entity.Lost;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tszwaidai
 * @since 2024-06-07
 */
public interface LostService extends IService<Lost> {

    IPage<LostDTO> getLostDTOPage(Page<LostDTO> page, String goodsname, String username);
}
