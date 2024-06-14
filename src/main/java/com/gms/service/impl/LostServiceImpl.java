package com.gms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gms.dto.LostDTO;
import com.gms.entity.Lost;
import com.gms.mapper.LostMapper;
import com.gms.service.LostService;
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
public class LostServiceImpl extends ServiceImpl<LostMapper, Lost> implements LostService {

    @Autowired
    private  LostMapper lostMapper;

    @Override
    public IPage<LostDTO> getLostDTOPage(Page<LostDTO> page, String goodsname, String username) {
        return lostMapper.selectLostDTOPage(page,goodsname,username);
    }
}
