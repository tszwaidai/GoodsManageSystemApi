package com.gms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gms.dto.BorrowDTO;
import com.gms.entity.Borrow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tszwaidai
 * @since 2024-06-07
 */
public interface BorrowService extends IService<Borrow> {
    IPage<BorrowDTO> getBorrowDTOPage(Page<?> page,String goodsname,String username);

    Borrow findByGoodsIdAndUserId(Integer goodsId, Integer userId);
}
