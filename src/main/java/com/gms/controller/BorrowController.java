package com.gms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.gms.common.Result;
import com.gms.dto.BorrowDTO;
import com.gms.dto.GoodsInfoDTO;
import com.gms.entity.Borrow;
import com.gms.entity.GoodsInfo;
import com.gms.entity.User;
import com.gms.service.BorrowService;
import com.gms.service.GoodsInfoService;
import io.swagger.models.auth.In;
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
@RestController
@RequestMapping("/borrow")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;
    @Autowired
    private GoodsInfoService goodsInfoService;

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


}

