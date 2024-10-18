package com.gms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gms.common.Result;
import com.gms.dto.LostDTO;
import com.gms.entity.Borrow;
import com.gms.entity.Lost;
import com.gms.mapper.LostMapper;
import com.gms.service.BorrowService;
import com.gms.service.LostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Autowired
    @Lazy
    private BorrowService borrowService;

    @Override
    public IPage<LostDTO> getLostDTOPage(Page<LostDTO> page, String goodsname, String username) {
        return lostMapper.selectLostDTOPage(page,goodsname,username);
    }

    @Override
    public Result getLostList(String goodsname, String username, Long pageNo, Long pageSize) {
        // 创建分页对象
        Page<LostDTO> page = new Page<>(pageNo, pageSize);
        //分页查询
        IPage<LostDTO> lostDTOPage = getLostDTOPage(page, goodsname, username);
        return Result.suc(lostDTOPage.getRecords(), lostDTOPage.getTotal());
    }

    @Override
    public Result solveLost(Integer id) {
        Lost lost = getById(id);

        // 通过 goodsId 和 userId 查询 Borrow 记录
        Borrow borrow = borrowService.findByGoodsIdAndUserId(lost.getGoodsId(), lost.getUserId());
        borrow.setStatus(6); //丢失已处理 恢复初始状态
        borrow.setReturnTime(LocalDateTime.now());
        borrowService.updateById(borrow);

        return Result.suc("丢失物品已解决");
    }

    @Override
    public Result deleteById(Integer id) {
        removeById(id);
        return Result.suc("删除记录成功");
    }
}
