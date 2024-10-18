package com.gms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gms.common.Result;
import com.gms.dto.BorrowDTO;
import com.gms.entity.Borrow;
import com.gms.entity.GoodsInfo;
import com.gms.entity.Lost;
import com.gms.mapper.BorrowMapper;
import com.gms.service.BorrowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gms.service.GoodsInfoService;
import com.gms.service.LostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private GoodsInfoService goodsInfoService;

    @Autowired
    private LostService lostService;

    @Override
    public IPage<BorrowDTO> getBorrowDTOPage(Page<?> page, String goodsname, String username) {
        return borrowMapper.selectBorrowDTOPage(page,goodsname,username);
    }

    @Override
    public Borrow findByGoodsIdAndUserId(Integer goodsId, Integer userId) {
        QueryWrapper<Borrow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goodsId", goodsId).eq("userId", userId);
        return borrowMapper.selectOne(queryWrapper);
    }

    @Override
    public Result getBorrowList(String goodsname, String username, Long pageNo, Long pageSize) {
        // 创建分页对象
        Page<BorrowDTO> page = new Page<>(pageNo, pageSize);
        //分页查询
        IPage<BorrowDTO> borrowDTOPage = getBorrowDTOPage(page, goodsname, username);
        return Result.suc(borrowDTOPage.getRecords(), borrowDTOPage.getTotal());
    }

    @Override
    public Result addBorrow(Borrow borrow) {
        save(borrow);
        return Result.suc("新增借用记录成功");
    }

    @Override
    public Result updateBorrow(Borrow borrow) {
        updateById(borrow);
        return Result.suc("修改借阅记录成功");
    }

    @Override
    public Result getBorrowById(Integer id) {
        Borrow borrow = getById(id);
        return Result.suc(borrow);
    }

    @Override
    public Result deleteById(Integer id) {
        removeById(id);
        return Result.suc("删除借用记录成功");
    }

    @Override
    public Result approveBorrow(Integer id) {
        Borrow borrow = getById(id);
        borrow.setStatus(1); //假设状态1表示“已通过”
        updateById(borrow);

        // 同时更新GoodsInfo表中对应物品的状态为“已通过”
        GoodsInfo goodsInfo = goodsInfoService.getById(borrow.getGoodsId());
        goodsInfo.setStatus(2); // 假设状态0表示“未申请”
        goodsInfoService.updateById(goodsInfo);

        return Result.suc("物品申请已通过");
    }

    @Override
    public Result rejectBorrow(Integer id) {
        Borrow borrow = getById(id);
        borrow.setStatus(2); //假设状态2表示“未通过”
        updateById(borrow);

        // 同时更新GoodsInfo表中对应物品的状态为“未通过”
        GoodsInfo goodsInfo = goodsInfoService.getById(borrow.getGoodsId());
        goodsInfo.setStatus(3); // 假设状态0表示“未通过”
        goodsInfoService.updateById(goodsInfo);

        return Result.suc("物品申请未通过");
    }

    @Override
    public Result completeBorrow(Integer id) {
        Borrow borrow = getById(id);
        // 更新物品状态为“使用中”
        borrow.setStatus(3); //使用中
        updateById(borrow);
        return Result.suc("物品领用成功");
    }

    @Override
    public Result returnGoods(Integer id) {
        Borrow borrow = getById(id);
        borrow.setReturnTime(LocalDateTime.now());
        borrow.setStatus(4); // 假设状态4表示“已归还”
        updateById(borrow);

        // 同时更新GoodsInfo表中对应物品的状态为“未申请”
        GoodsInfo goodsInfo = goodsInfoService.getById(borrow.getGoodsId());
        goodsInfo.setStatus(0); // 假设状态0表示“未申请”
        goodsInfoService.updateById(goodsInfo);

        return  Result.suc("物品已归还");
    }

    @Override
    public Result lostGoods(Map<String, Integer> request) {
        Integer goodsId = request.get("goodsId");
        Integer userId = request.get("userId");

        // 通过 goodsId 和 userId 查询 Borrow 对象
        Borrow borrow = findByGoodsIdAndUserId(goodsId, userId);
        borrow.setStatus(5); //表示物品丢失
        updateById(borrow);

        // 同时更新GoodsInfo表中对应物品的状态为“未申请”
        GoodsInfo goodsInfo = goodsInfoService.getById(borrow.getGoodsId());
        goodsInfo.setStatus(0); // 假设状态0表示“未申请”
        goodsInfoService.updateById(goodsInfo);

        // 向lost插入一条数据
        Lost lost = new Lost();
        lost.setUserId(userId);
        lost.setGoodsId(goodsId);
        lost.setLostTime(LocalDateTime.now());
        lostService.save(lost);

        return Result.suc("物品丢失上报成功");

    }

    @Override
    public Result getStatusCount() {
        // 获取各状态的数量
        Map<String, Long> countMap = new HashMap<>();
        countMap.put("pending", count(new QueryWrapper<Borrow>().eq("status", 0))); // 待审核
        countMap.put("approved", count(new QueryWrapper<Borrow>().eq("status", 1))); // 已通过
        countMap.put("rejected", count(new QueryWrapper<Borrow>().eq("status", 2))); // 未通过
        countMap.put("using", count(new QueryWrapper<Borrow>().eq("status", 3))); // 使用中
        countMap.put("returned", count(new QueryWrapper<Borrow>().eq("status", 4))); // 已归还

        return Result.suc(countMap);
    }
}
