package com.gms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gms.common.Result;
import com.gms.entity.GoodsType;
import com.gms.entity.User;
import com.gms.service.GoodsTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
//    @GetMapping("/list")
//    public Result getTypeList(@RequestParam(value = "typename",required = false) String typename,
//                              @RequestParam(value = "pageNo") Long pageNo,
//                              @RequestParam(value = "pageSize") Long pageSize) {
//        LambdaQueryWrapper<GoodsType> wrapper = new LambdaQueryWrapper<>();
//        //模糊查询
//        wrapper.like(StringUtils.hasText(typename),GoodsType::getGoodsTypeName,typename);
//        Page<User> page = new Page<>(pageNo,pageSize);
//        goodsTypeService.page(page,wrapper);
//        return Result.suc(page.getRecords(),page.getTotal());
//    }

}

