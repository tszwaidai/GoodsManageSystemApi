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

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 用户登录
     * @param user
     * @return
     */
    @PostMapping(value = "/login")
    public Result login(@RequestBody User user) {
        log.info("Login attempt with user: {}", user);
        List list = userService.lambdaQuery()
                .eq(User::getUserName,user.getUserName())
                .eq(User::getUserPassword,user.getUserPassword()).list();
        if (list.size() > 0) {
            // 用户名和密码正确，生成Token
            String token = TokenProcessor.getInstance().makeToken();
            // 将Token存储到Redis，设置过期时间
            redisTemplate.opsForValue().set(token, list.get(0), 1, TimeUnit.HOURS);
            return Result.suc(token);
        } else {
            return Result.fail("用户名或密码错误");
        }
    }

    /**
     * 获取用户信息
     * @param token
     * @return
     */
    @GetMapping(value = "/info")
    public Result getInfo(@RequestParam("token") String token) {
        System.out.println("Received token: " + token); // 打印接收到的Token
        // 从Redis中获取用户信息
        User user = (User) redisTemplate.opsForValue().get(token);

        if (user != null) {
            return Result.suc(user);
        } else {
            return Result.fail("无效的Token或Token已过期");
        }
    }


    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping(value = "/register")
    public Result register(@RequestBody User user) {
        //查询当前用户是否存在
        List<User> existingUsers = userService.lambdaQuery()
                .eq(User::getUserName,user.getUserName()).list();
        if (existingUsers.size() > 0){
            return Result.fail("用户已存在");
        }
        boolean isSaved = userService.save(user);
        return isSaved ? Result.suc() : Result.fail();
    }

    /**
     * 退出登录
     * @param token
     * @return
     */
    @PostMapping(value = "/logout")
    public Result logout(@RequestHeader("X-Token") String token) {
        Boolean isDeleted = redisTemplate.delete(token);
        return isDeleted ? Result.suc("成功退出登录") : Result.fail("退出登录失败");
    }



    /**
     * 查询所有用户/基于mp的分页查询
     * @return
     */
    @GetMapping("/list")
    public Result getUserList(@RequestParam(value = "username",required = false) String username,
                       @RequestParam(value = "pageNo") Long pageNo,
                       @RequestParam(value = "pageSize") Long pageSize) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(username), User::getUserName,username);
        Page<User> page = new Page<>(pageNo,pageSize);
        userService.page(page,wrapper);
        return Result.suc(page.getRecords(),page.getTotal());

    }

    /**
     * 新增
     * @param user
     * @return
     */
    @PostMapping("/add")
    public Result addUser(@RequestBody User user) {
        userService.save(user);
        return Result.suc("新增用户成功");
    }

    /**
     * 修改
     * @param user
     * @return
     */
    @PutMapping("/update")
    public Result updateUser(@RequestBody User user) {
        user.setUserPassword(null);
        userService.updateById(user);
        return Result.suc("修改用户成功");
    }

    /**
     * 通过ID获取用户信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getUserById(@PathVariable("id") Integer id){
        User user = userService.getById(id);
        return Result.suc(user);
    }


    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteUserById(@PathVariable("id") Integer id) {
        userService.removeById(id);
        return Result.suc("删除用户成功");
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

    /**
     * Token 验证
     * @param token
     * @return
     */
    @GetMapping(value = "/verify")
    public Result verifyToken(@RequestParam String token) {
        Object user = redisTemplate.opsForValue().get(token);
        if (user != null){
            return Result.suc(user);
        } else {
            return Result.fail("Token无效或已过期");
        }
    }

}


