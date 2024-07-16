package com.gms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gms.common.Result;
import com.gms.dto.TypeCountDTO;
import com.gms.entity.GoodsInfo;
import com.gms.entity.GoodsType;
import com.gms.entity.User;
import com.gms.service.GoodsInfoService;
import com.gms.service.GoodsTypeService;
import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tszwaidai
 * @since 2024-06-07
 */
@Slf4j
@RestController
@RequestMapping("/goodsType")
public class GoodsTypeController {

    @Autowired
    private GoodsTypeService goodsTypeService;
    @Autowired
    private GoodsInfoService goodsInfoService;


    /**
     * 分页查询类型
     * @param typename
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public Result getTypeList(@RequestParam(value = "typename",required = false) String typename,
                              @RequestParam(value = "pageNo") Long pageNo,
                              @RequestParam(value = "pageSize") Long pageSize) {
        LambdaQueryWrapper<GoodsType> wrapper = new LambdaQueryWrapper<>();
        //模糊查询
        wrapper.like(StringUtils.hasText(typename),GoodsType::getGoodsTypeName,typename);
        Page<GoodsType> page = new Page<>(pageNo,pageSize);
        goodsTypeService.page(page,wrapper);
        return Result.suc(page.getRecords(),page.getTotal());
    }

    /**
     * 获取类别数量
     * @return
     */
    @GetMapping("/type-count")
    public Result getTypeCount(){
        // 获取分类数量
        QueryWrapper<GoodsInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("goodsTypeId", "COUNT(*) as count")
                .groupBy("goodsTypeId");

        List<Map<String, Object>> typeCounts = goodsInfoService.listMaps(queryWrapper);

        // 获取所有分类的名字
        Map<Integer, String> typeNames = goodsTypeService.list().stream()
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

    /**
     * 新增物品分类
     * @param goodsType
     * @return
     */
    @PostMapping(value = "/add")
    public Result addType(@RequestBody GoodsType goodsType) {
        goodsTypeService.save(goodsType);
        return Result.suc("新增物品分类成功");
    }


    /**
     * 修改分类
     * @param goodsType
     * @return
     */
    @PutMapping(value = "/update")
    public Result updateType(@RequestBody GoodsType goodsType){
        goodsTypeService.updateById(goodsType);
        return Result.suc("物品分类修改成功");
    }

    /**
     * 根据ID获取分类信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getTypeById(@PathVariable("id") Integer id) {
        GoodsType goodsType = goodsTypeService.getById(id);
        return Result.suc(goodsType);
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteTypeById(@PathVariable("id") Integer id){
        goodsTypeService.removeById(id);
        return Result.suc("删除物品分类信息成功");
    }


}

