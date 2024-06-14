package com.gms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gms.dto.BorrowDTO;
import com.gms.dto.LostDTO;
import com.gms.entity.Lost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tszwaidai
 * @since 2024-06-07
 */
@Mapper
public interface LostMapper extends BaseMapper<Lost> {
    /**
     * mp不支持多个实体类查询 手动添加sql
     * @param page
     * @param goodsname
     * @param username
     * @return
     */
    @Select("SELECT l.lostId, l.userId, l.goodsId, l.lostTime, g.goodsName, u.username " +
            "FROM lost l " +
            "LEFT JOIN goods_info g ON l.goodsId = g.goodsId " +
            "LEFT JOIN user u ON l.userId = u.userId " +
            "WHERE " +
            "(#{goodsname} IS NULL OR g.goodsName LIKE CONCAT('%', #{goodsname}, '%')) " +
            "AND (#{username} IS NULL OR u.username LIKE CONCAT('%', #{username}, '%'))")
    IPage<LostDTO> selectLostDTOPage(Page<?> page, @Param("goodsname") String goodsname, @Param("username") String username);

}
