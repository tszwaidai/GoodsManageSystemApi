package com.gms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gms.common.Result;
import com.gms.dto.TypeCountDTO;
import com.gms.entity.GoodsInfo;
import com.gms.entity.GoodsType;
import com.gms.mapper.GoodsTypeMapper;
import com.gms.service.GoodsInfoService;
import com.gms.service.GoodsTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
public class GoodsTypeServiceImpl extends ServiceImpl<GoodsTypeMapper, GoodsType> implements GoodsTypeService {

    @Autowired
    private GoodsInfoService goodsInfoService;

    @Override
    public Result getTypeList(String typename, Long pageNo, Long pageSize) {
        LambdaQueryWrapper<GoodsType> wrapper = new LambdaQueryWrapper<>();
        //模糊查询
        wrapper.like(StringUtils.hasText(typename),GoodsType::getGoodsTypeName,typename);
        Page<GoodsType> page = new Page<>(pageNo,pageSize);
        page(page,wrapper);
        return Result.suc(page.getRecords(),page.getTotal());
    }

    @Override
    public Result getTypeCount() {
        // 获取分类数量
        QueryWrapper<GoodsInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("goodsTypeId", "COUNT(*) as count")
                .groupBy("goodsTypeId");

        List<Map<String, Object>> typeCounts = goodsInfoService.listMaps(queryWrapper);

        // 获取所有分类的名字
        Map<Integer, String> typeNames = list().stream()
                .collect(Collectors.toMap(GoodsType::getGoodsTypeId, GoodsType::getGoodsTypeName));

        // 将分类ID和数量转换为分类名称和数量
        List<TypeCountDTO> result = typeCounts.stream().map(tc -> {
            TypeCountDTO dto = new TypeCountDTO();
            Integer typeId = (Integer) tc.get("goodsTypeId");
            dto.setTypeName(typeNames.get(typeId));
            dto.setCount((Long) tc.get("count"));
            return dto;
        }).collect(Collectors.toList());

        return Result.suc(result);
    }

    @Override
    public Result addType(GoodsType goodsType) {
        save(goodsType);
        return Result.suc("新增物品分类成功");
    }

    @Override
    public Result updateType(GoodsType goodsType) {
        updateById(goodsType);
        return Result.suc("物品分类修改成功");
    }

    @Override
    public Result getTypeById(Integer id) {
        GoodsType goodsType = getById(id);
        return Result.suc(goodsType);
    }

    @Override
    public Result deleteTypeById(Integer id) {
        removeById(id);
        return Result.suc("删除物品分类信息成功");
    }


}
