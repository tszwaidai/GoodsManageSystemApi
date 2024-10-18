package com.gms.controller;



import com.gms.common.Result;
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
        return goodsTypeService.getTypeList(typename, pageNo, pageSize);
    }

    /**
     * 获取类别数量
     * @return
     */
    @GetMapping("/type-count")
    public Result getTypeCount(){
        return goodsTypeService.getTypeCount();
    }

    /**
     * 新增物品分类
     * @param goodsType
     * @return
     */
    @PostMapping(value = "/add")
    public Result addType(@RequestBody GoodsType goodsType) {
        return goodsTypeService.addType(goodsType);
    }


    /**
     * 修改分类
     * @param goodsType
     * @return
     */
    @PutMapping(value = "/update")
    public Result updateType(@RequestBody GoodsType goodsType){
        return goodsTypeService.updateType(goodsType);
    }

    /**
     * 根据ID获取分类信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getTypeById(@PathVariable("id") Integer id) {
        return goodsTypeService.getTypeById(id);
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteTypeById(@PathVariable("id") Integer id){
        return goodsTypeService.deleteTypeById(id);
    }


}

