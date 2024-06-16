package com.gms.controller;


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
    @Autowired
    private GoodsInfoService goodsInfoService;

    @Autowired
    private LostService lostService;

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

        // 创建分页对象
        Page<BorrowDTO> page = new Page<>(pageNo, pageSize);
        //分页查询
        IPage<BorrowDTO> borrowDTOPage = borrowService.getBorrowDTOPage(page, goodsname, username);
        return Result.suc(borrowDTOPage.getRecords(), borrowDTOPage.getTotal());
    }

    /**
     * 添加借用记录
     * @param borrow
     * @return
     */
    @PostMapping("/add")
    public Result addBorrow(@RequestBody Borrow borrow){
        borrowService.save(borrow);
        return Result.suc("新增借用记录成功");
    }

    /**
     * 修改借阅记录
     * @param borrow
     * @return
     */
    @PutMapping("/update")
    public Result updateBorrow(@RequestBody Borrow borrow){
        borrowService.updateById(borrow);
        return Result.suc("修改借阅记录成功");
    }

    /**
     * 根据id获取借阅信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getBorrowById(@PathVariable("id") Integer id) {
        Borrow borrow = borrowService.getById(id);
        return Result.suc(borrow);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable("id") Integer id){
        borrowService.removeById(id);
        return Result.suc("删除借用记录成功");
    }


    /**
     * 管理员通过申请
     * @param id
     * @return
     */
    @PutMapping("/approve/{id}")
    public Result approveBorrow(@PathVariable("id") Integer id) {
        Borrow borrow = borrowService.getById(id);
        borrow.setStatus(1); //假设状态1表示“已通过”
        borrowService.updateById(borrow);

        // 同时更新GoodsInfo表中对应物品的状态为“已通过”
        GoodsInfo goodsInfo = goodsInfoService.getById(borrow.getGoodsId());
        goodsInfo.setStatus(2); // 假设状态0表示“未申请”
        goodsInfoService.updateById(goodsInfo);

        return Result.suc("物品申请已通过");

    }

    /**
     * 不同意申请
     * @param id
     * @return
     */
    @PutMapping("/reject/{id}")
    public Result rejectBorrow(@PathVariable("id") Integer id){
        Borrow borrow = borrowService.getById(id);
        borrow.setStatus(2); //假设状态2表示“未通过”
        borrowService.updateById(borrow);

        // 同时更新GoodsInfo表中对应物品的状态为“未通过”
        GoodsInfo goodsInfo = goodsInfoService.getById(borrow.getGoodsId());
        goodsInfo.setStatus(3); // 假设状态0表示“未通过”
        goodsInfoService.updateById(goodsInfo);

        return Result.suc("物品申请未通过");
    }

    /**
     * 学生归还物品
     * @param id
     * @return
     */
    @PutMapping("/return/{id}")
    public Result returnGoods(@PathVariable("id") Integer id) {
        Borrow borrow = borrowService.getById(id);
        borrow.setReturnTime(LocalDateTime.now());
        borrow.setStatus(3); // 假设状态3表示“已归还”
        borrowService.updateById(borrow);

        // 同时更新GoodsInfo表中对应物品的状态为“未申请”
        GoodsInfo goodsInfo = goodsInfoService.getById(borrow.getGoodsId());
        goodsInfo.setStatus(0); // 假设状态0表示“未申请”
        goodsInfoService.updateById(goodsInfo);

        return  Result.suc("物品已归还");

    }

    /**
     * 物品丢失上报
     * @param
     * @return
     */
    @PostMapping("/lost")
    public  Result lostGoods(@RequestBody Map<String, Integer> request){
        Integer goodsId = request.get("goodsId");
        Integer userId = request.get("userId");

        // 通过 goodsId 和 userId 查询 Borrow 对象
        Borrow borrow = borrowService.findByGoodsIdAndUserId(goodsId, userId);
        borrow.setStatus(4); //表示物品丢失
        borrowService.updateById(borrow);

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




}

