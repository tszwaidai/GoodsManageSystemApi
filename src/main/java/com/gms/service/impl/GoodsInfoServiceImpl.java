package com.gms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.gms.common.Result;
import com.gms.dto.GoodsInfoDTO;
import com.gms.entity.Borrow;
import com.gms.entity.GoodsInfo;
import com.gms.entity.GoodsType;
import com.gms.mapper.GoodsInfoMapper;
import com.gms.service.BorrowService;
import com.gms.service.GoodsInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tszwaidai
 * @since 2024-06-07
 */
@Service
public class GoodsInfoServiceImpl extends ServiceImpl<GoodsInfoMapper, GoodsInfo> implements GoodsInfoService {

    @Autowired
    @Lazy
    private BorrowService borrowService;

    @Override
    public Result getInfoList(String goodsname, String typename, Long pageNo, Long pageSize) {
        Page<Map<String, Object>> page = new Page<>(pageNo, pageSize);
        // 创建联表查询包装器
        MPJLambdaWrapper<GoodsInfo> wrapper = new MPJLambdaWrapper<>();
        // 添加联表查询
        wrapper.selectAll(GoodsInfo.class)
                .select(GoodsType::getGoodsTypeName) // 选择类型名称
                .leftJoin(GoodsType.class, GoodsType::getGoodsTypeId, GoodsInfo::getGoodsTypeId);
        // 根据物品名称的搜索条件
        if (StringUtils.hasText(goodsname)) {
            wrapper.like(GoodsInfo::getGoodsName, goodsname);
        }
        // 添加根据分类名称的搜索条件
        if (StringUtils.hasText(typename)) {
            wrapper.like(GoodsType::getGoodsTypeName, typename);
        }

        IPage<Map<String, Object>> rawPage = pageMaps(page,wrapper);

        List<GoodsInfoDTO> goodsInfoDTOList = rawPage.getRecords().stream().map(record -> {
            GoodsInfoDTO dto = new GoodsInfoDTO();
            dto.setGoodsId((Integer) record.get("goodsId"));
            dto.setGoodsName((String) record.get("goodsName"));
            dto.setGoodsTypeId((Integer) record.get("goodsTypeId"));
            dto.setGoodsTypeName((String) record.get("goodsTypeName")); // 手动设置类型名称
            dto.setGoodsDesc((String) record.get("goodsDesc"));
            dto.setStatus((Integer) record.get("status"));
            dto.setGoodsPrice((BigDecimal) record.get("goodsPrice"));
            return dto;
        }).collect(Collectors.toList());

        // 创建DTO分页对象
        Page<GoodsInfoDTO> dtoPage = new Page<>(pageNo, pageSize, rawPage.getTotal());
        dtoPage.setRecords(goodsInfoDTOList);
        return Result.suc(dtoPage.getRecords(), dtoPage.getTotal());
    }

    @Override
    public Result addInfo(GoodsInfo goodsInfo) {
        save(goodsInfo);
        return Result.suc("新增物品信息成功");
    }

    @Override
    public Result updateInfo(GoodsInfo goodsInfo) {
        updateById(goodsInfo);
        return Result.suc("修改物品信息成功");
    }

    @Override
    public Result getInfoById(Integer id) {
        GoodsInfo goodsInfo = getById(id);
        return Result.suc(goodsInfo);
    }

    @Override
    public Result deleteById(Integer id) {
        removeById(id);
        return Result.suc("删除物品信息成功");
    }

    @Override
    public Result updateStatus(GoodsInfo goodsInfo) {
        boolean success = updateById(goodsInfo);
        if (success){
            return Result.suc("状态更新成功");
        }else{
            return Result.fail("状态更新失败");
        }
    }

    @Override
    public Result applyGoods(Map<String, Integer> request) {
        Integer goodsId = request.get("goodsId");
        Integer userId = request.get("userId");

        // 更新物品状态为“申请中”
        GoodsInfo goodsInfo = getById(goodsId);
        goodsInfo.setStatus(1); // 假设状态1表示“申请中”
        updateById(goodsInfo);

        // 在Borrow表中插入新记录
        Borrow borrow = new Borrow();
        borrow.setUserId(userId);
        borrow.setGoodsId(goodsId);
        borrow.setBorrowTime(LocalDateTime.now());
        borrow.setStatus(0);
        borrowService.save(borrow); // 保存到Borrow表

        return Result.suc("申请物品成功");
    }


}
