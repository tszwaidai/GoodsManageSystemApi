package com.gms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gms.common.Result;
import com.gms.entity.GoodsType;
import com.gms.entity.User;
import com.gms.service.GoodsTypeService;
import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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

