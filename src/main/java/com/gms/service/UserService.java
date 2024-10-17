package com.gms.service;

import com.gms.common.Result;
import com.gms.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tszwaidai
 * @since 2024-06-07
 */
public interface UserService extends IService<User> {
    Result login(User user);


    Result getInfo(String token);

    Result register(User user);

    Result logout(String token);

    Result getUserList(String username, Long pageNo, Long pageSize);

    Result addUser(User user);

    Result updateUser(User user);

    Result getUserById(Integer id);

    Result deleteUserById(Integer id);

    Result query(User user);

    Result verifyToken(String token);
}
