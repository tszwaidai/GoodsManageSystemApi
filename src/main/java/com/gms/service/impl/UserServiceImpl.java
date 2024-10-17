package com.gms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gms.common.Result;
import com.gms.common.TokenProcessor;
import com.gms.entity.User;
import com.gms.mapper.UserMapper;
import com.gms.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tszwaidai
 * @since 2024-06-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 用户登录
     * @param user
     * @return
     */
    @Override
    public Result login(User user) {
        List<User> list = lambdaQuery()
                .eq(User::getUserName,user.getUserName())
                .eq(User::getUserPassword,user.getUserPassword())
                .list();
        if (list().size() > 0) {
            String token = TokenProcessor.getInstance().makeToken();
            redisTemplate.opsForValue().set(token, list.get(0), 3, TimeUnit.HOURS);
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
    @Override
    public Result getInfo(String token) {
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
    @Override
    public Result register(User user) {
        //查询当前用户是否存在
        List<User> existingUsers = lambdaQuery()
                .eq(User::getUserName,user.getUserName()).list();
        if (existingUsers.size() > 0){
            return Result.fail("用户已存在");
        }
        boolean isSaved = save(user);
        return isSaved ? Result.suc() : Result.fail();
    }

    /**
     * 退出登录
     * @param token
     * @return
     */
    @Override
    public Result logout(String token) {
        Boolean isDeleted = redisTemplate.delete(token);
        return isDeleted ? Result.suc("成功退出登录") : Result.fail("退出登录失败");
    }

    /**
     * 查询所有用户/基于mp的分页查询
     * @param username
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Result getUserList(String username, Long pageNo, Long pageSize) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(username), User::getUserName,username);
        Page<User> page = new Page<>(pageNo,pageSize);
        page(page,wrapper);
        return Result.suc(page.getRecords(),page.getTotal());
    }

    @Override
    public Result addUser(User user) {
        save(user);
        return Result.suc("新增用户成功");
    }

    @Override
    public Result updateUser(User user) {
        user.setUserPassword(null);
        updateById(user);
        return Result.suc("修改用户成功");
    }

    @Override
    public Result getUserById(Integer id) {
        User user = getById(id);
        return Result.suc(user);
    }

    @Override
    public Result deleteUserById(Integer id) {
        removeById(id);
        return Result.suc("删除用户成功");
    }

    @Override
    public Result query(User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(User::getUserName,user.getUserName());
        return Result.suc(lambdaQueryWrapper);
    }

    @Override
    public Result verifyToken(String token) {
        Object user = redisTemplate.opsForValue().get(token);
        if (user != null){
            return Result.suc(user);
        } else {
            return Result.fail("Token无效或已过期");
        }
    }


}
