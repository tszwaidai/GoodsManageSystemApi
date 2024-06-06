package com.gms.mapper;

import com.gms.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tszwaidai
 * @since 2024-06-07
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
