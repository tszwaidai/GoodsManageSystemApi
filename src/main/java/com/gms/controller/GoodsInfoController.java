package com.gms.controller;


import com.gms.entity.GoodsInfo;
import com.gms.service.GoodsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tszwaidai
 * @since 2024-06-07
 */
@RestController
@RequestMapping("/goodsInfo")
public class GoodsInfoController {
    @Autowired
    private GoodsInfoService goodsInfoService;

    /**
     * 查询所有物品信息
     * @return
     */
    @GetMapping("/list")
    public List<GoodsInfo> list() {
        return goodsInfoService.list();
    }




}

