package com.gms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gms.common.Result;
import com.gms.dto.BorrowDTO;
import com.gms.dto.LostDTO;
import com.gms.entity.Borrow;
import com.gms.entity.Lost;
import com.gms.service.BorrowService;
import com.gms.service.LostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tszwaidai
 * @since 2024-06-07
 */
@RestController
@RequestMapping("/lost")
public class LostController {

    @Autowired
    private LostService lostService;

    @Autowired
    private BorrowService borrowService;

    /**
     * 分页查询
     * @param goodsname
     * @param username
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public Result getLostList(@RequestParam(value = "goodsname", required = false) String goodsname,
                                @RequestParam(value = "username",required = false) String username,
                                @RequestParam(value = "pageNo") Long pageNo,
                                @RequestParam(value = "pageSize") Long pageSize) {
        return lostService.getLostList(goodsname,username,pageNo,pageSize);
    }

    /**
     * 解决丢失问题
     * @param id
     * @return
     */
    @PutMapping("/solve/{id}")
    public Result solveLost(@PathVariable("id") Integer id) {
        return lostService.solveLost(id);
    }

    /**
     * 删除记录
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable("id") Integer id){
        return lostService.deleteById(id);
    }


}

