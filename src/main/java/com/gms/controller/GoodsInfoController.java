package com.gms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.gms.common.Result;
import com.gms.dto.GoodsInfoDTO;
import com.gms.entity.Borrow;
import com.gms.entity.GoodsInfo;
import com.gms.entity.GoodsType;
import com.gms.entity.User;
import com.gms.service.BorrowService;
import com.gms.service.GoodsInfoService;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@RestController
@RequestMapping("/goodsInfo")
@SessionAttributes("currentUser")
public class GoodsInfoController {
    @Autowired
    private GoodsInfoService goodsInfoService;

    /**
     * 分页查询所有物品信息 模糊查询与多表查询
     * @return
     */
    @GetMapping("/list")
    public Result getInfoList(@RequestParam(value = "goodsname", required = false) String goodsname,
                              @RequestParam(value = "typename", required = false) String typename,
                              @RequestParam(value = "pageNo") Long pageNo,
                              @RequestParam(value = "pageSize") Long pageSize) {
        return goodsInfoService.getInfoList(goodsname,typename,pageNo,pageSize);
    }


    /**
     * 新增物品信息
     * @param goodsInfo
     * @return
     */
    @PostMapping(value = "/add")
    public Result addInfo(@RequestBody GoodsInfo goodsInfo) {
        return goodsInfoService.addInfo(goodsInfo);
    }

    /**
     * 修改物品信息
     * @param goodsInfo
     * @return
     */
    @PutMapping(value = "/update")
    public Result updateInfo(@RequestBody GoodsInfo goodsInfo) {
        return goodsInfoService.updateInfo(goodsInfo);
    }

    /**
     * 根据ID获得物品信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getInfoById(@PathVariable("id") Integer id){
        return goodsInfoService.getInfoById(id);
    }

    /**
     * 删除物品信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable("id") Integer id){
        return goodsInfoService.deleteById(id);
    }

    /**
     * 更新状态
     * @param goodsInfo
     * @return
     */
    @PostMapping("/updateStatus")
    public Result updateStatus(@RequestBody GoodsInfo goodsInfo) {
        return goodsInfoService.updateStatus(goodsInfo);
    }

    /**
     * 学生点击“申请”，将物品状态设置为“申请中”，同时在Borrow表插入一条记录。
     * @param request
     * @return
     */
    @PostMapping("/apply")
    public Result applyGoods(@RequestBody Map<String, Integer> request) {
        return goodsInfoService.applyGoods(request);
    }


}

