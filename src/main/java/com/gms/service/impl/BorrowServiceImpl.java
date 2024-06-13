package com.gms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gms.dto.BorrowDTO;
import com.gms.entity.Borrow;
import com.gms.mapper.BorrowMapper;
import com.gms.service.BorrowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tszwaidai
 * @since 2024-06-07
 */
@Service
public class BorrowServiceImpl extends ServiceImpl<BorrowMapper, Borrow> implements BorrowService {

    @Autowired
    private BorrowMapper borrowMapper;
    @Override
    public IPage<BorrowDTO> getBorrowDTOPage(Page<?> page, String goodsname, String username) {
        return borrowMapper.selectBorrowDTOPage(page,goodsname,username);
    }
}
