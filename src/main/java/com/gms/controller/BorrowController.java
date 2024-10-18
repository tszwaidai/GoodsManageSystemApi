package com.gms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.gms.common.Result;
import com.gms.dto.BorrowDTO;
import com.gms.dto.GoodsInfoDTO;
import com.gms.entity.Borrow;
import com.gms.entity.GoodsInfo;
import com.gms.entity.Lost;
import com.gms.entity.User;
import com.gms.service.BorrowService;
import com.gms.service.GoodsInfoService;
import com.gms.service.LostService;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
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
@RequestMapping("/borrow")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    /**
     * 分页查询借阅信息
     * @param goodsname
     * @param username
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public Result getBorrowList(@RequestParam(value = "goodsname", required = false) String goodsname,
                                @RequestParam(value = "username",required = false) String username,
                                @RequestParam(value = "pageNo") Long pageNo,
                                @RequestParam(value = "pageSize") Long pageSize) {
        return borrowService.getBorrowList(goodsname,username,pageNo,pageSize);
    }

    /**
     * 添加借用记录
     * @param borrow
     * @return
     */
    @PostMapping("/add")
    public Result addBorrow(@RequestBody Borrow borrow){
        return borrowService.addBorrow(borrow);
    }

    /**
     * 修改借阅记录
     * @param borrow
     * @return
     */
    @PutMapping("/update")
    public Result updateBorrow(@RequestBody Borrow borrow){
        return borrowService.updateBorrow(borrow);
    }

    /**
     * 根据id获取借阅信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getBorrowById(@PathVariable("id") Integer id) {
        return borrowService.getBorrowById(id);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable("id") Integer id){
        return borrowService.deleteById(id);
    }


    /**
     * 管理员通过申请
     * @param id
     * @return
     */
    @PutMapping("/approve/{id}")
    public Result approveBorrow(@PathVariable("id") Integer id) {
        return borrowService.approveBorrow(id);
    }

    /**
     * 不同意申请
     * @param id
     * @return
     */
    @PutMapping("/reject/{id}")
    public Result rejectBorrow(@PathVariable("id") Integer id){
        return borrowService.rejectBorrow(id);
    }

    /**
     * 学生点击“领用”后，将物品状态设置为“使用中”。
     * @param id
     * @return
     */
    @PostMapping("/completeBorrow/{id}")
    public Result completeBorrow(@PathVariable("id") Integer id) {
        return borrowService.completeBorrow(id);
    }

    /**
     * 学生归还物品
     * @param id
     * @return
     */
    @PutMapping("/return/{id}")
    public Result returnGoods(@PathVariable("id") Integer id) {
        return borrowService.returnGoods(id);
    }

    /**
     * 物品丢失上报
     * @param
     * @return
     */
    @PostMapping("/lost")
    public  Result lostGoods(@RequestBody Map<String, Integer> request){
        return borrowService.lostGoods(request);
    }

    /**
     *审核情况获取
     * @return
     */
    @GetMapping("/status-count")
    public Result getStatusCount() {
        return borrowService.getStatusCount();
    }


}

