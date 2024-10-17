package com.gms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gms.common.QueryPageParam;
import com.gms.common.Result;
import com.gms.common.TokenProcessor;
import com.gms.entity.User;
import com.gms.service.UserService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.ls.LSException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
//@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param user
     * @return
     */
    @PostMapping(value = "/login")
    public Result login(@RequestBody User user) {
        log.info("Login attempt with user: {}", user);
        return userService.login(user);
    }

    /**
     * 获取用户信息
     * @param token
     * @return
     */
    @GetMapping(value = "/info")
    public Result getInfo(@RequestParam("token") String token) {
        return userService.getInfo(token);
    }


    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping(value = "/register")
    public Result register(@RequestBody User user) {
        return userService.register(user);
    }


    /**
     * 退出登录
     * @param token
     * @return
     */
    @PostMapping(value = "/logout")
    public Result logout(@RequestHeader("X-Token") String token) {
        return userService.logout(token);
    }


    /**
     * 查询所有用户/基于mp的分页查询
     * @return
     */
    @GetMapping("/list")
    public Result getUserList(@RequestParam(value = "username",required = false) String username,
                       @RequestParam(value = "pageNo") Long pageNo,
                       @RequestParam(value = "pageSize") Long pageSize) {
        return userService.getUserList(username,pageNo,pageSize);
    }

    /**
     * 新增
     * @param user
     * @return
     */
    @PostMapping("/add")
    public Result addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    /**
     * 修改
     * @param user
     * @return
     */
    @PutMapping("/update")
    public Result updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    /**
     * 通过ID获取用户信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getUserById(@PathVariable("id") Integer id){
        return userService.getUserById(id);
    }


    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteUserById(@PathVariable("id") Integer id) {
        return userService.deleteUserById(id);
    }


    /**
     * 模糊 / 匹配 查询
     * @param user
     * @return
     */
    @GetMapping("/query")
    public Result query(@RequestBody User user) {
        return userService.query(user);
    }

    /**
     * Token 验证
     * @param token
     * @return
     */
    @GetMapping(value = "/verify")
    public Result verifyToken(@RequestParam String token) {
        return userService.verifyToken(token);
    }

}


