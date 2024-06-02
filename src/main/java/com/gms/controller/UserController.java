package com.gms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gms.entity.User;
import com.gms.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Jovie
 * @since 2024-06-02
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

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

