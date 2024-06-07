package com.gms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gms.common.QueryPageParam;
import com.gms.common.Result;
import com.gms.entity.User;
import com.gms.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.ls.LSException;

import java.util.ArrayList;
import java.util.List;

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
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private UserService userService;

//    @GetMapping(value = "/getCount")
//    public Integer getCount() {
//        return ;
//    }

    /**
     * 分页查询
     * @param query
     * @return
     */
    @PostMapping(value = "queryPage")
    public Result queryPage(@RequestBody QueryPageParam query) {
        PageHelper.startPage(query.getPageNum(),query.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (query.getQueryParam() != null && !query.getQueryParam().isEmpty()) {
            lambdaQueryWrapper.like(User::getUserName, query.getQueryParam());
        }

        List<User> userList = userService.list(lambdaQueryWrapper); //查询用户
        PageInfo<User> pageInfo = new PageInfo<>(userList); //查询到的用户userList封装到pageInfo

        return Result.suc(pageInfo.getList(), pageInfo.getTotal());

    }



    /**
     * 查询所有用户
     * @return
     */
    @GetMapping("/list")
    public List<User> list() {
        return  userService.list();
    }

    /**
     * 新增
     * @param user
     * @return
     */
    @PostMapping("/save")
    public boolean save(@RequestBody User user) {
        return userService.save(user);
    }

    /**
     * 修改
     * @param user
     * @return
     */
    @PostMapping("/mod")
    public boolean mod(@RequestBody User user) {
        return userService.updateById(user);
    }

    /**
     * 新增或修改
     * @param user
     * @return
     */
    @PostMapping("/saveOrMod")
    public boolean saveOrMod(@RequestBody User user){
        return userService.saveOrUpdate(user);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public boolean delete(Integer id) {
        return userService.removeById(id);
    }

    /**
     * 模糊 / 匹配 查询
     * @param user
     * @return
     */
    @GetMapping("/query")
    public List<User> query(@RequestBody User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(User::getUserName,user.getUserName());
        return userService.list(lambdaQueryWrapper);
    }




}


