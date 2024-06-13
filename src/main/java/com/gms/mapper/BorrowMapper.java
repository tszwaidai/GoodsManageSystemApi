package com.gms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gms.dto.BorrowDTO;
import com.gms.entity.Borrow;
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
public interface BorrowMapper extends BaseMapper<Borrow> {
    /**
     * mp不支持多个实体类查询 手动添加sql
     * @param page
     * @param goodsname
     * @param username
     * @return
     */
    @Select("SELECT b.borrowId, b.userId, b.goodsId, b.borrowTime, b.returnTime, g.goodsName, u.username " +
            "FROM borrow b " +
            "LEFT JOIN goods_info g ON b.goodsId = g.goodsId " +
            "LEFT JOIN user u ON b.userId = u.userId " +
            "WHERE (#{goodsname} IS NULL OR g.goodsName LIKE CONCAT('%', #{goodsname}, '%')) " +
            "AND (#{username} IS NULL OR u.username LIKE CONCAT('%', #{username}, '%'))")
    IPage<BorrowDTO> selectBorrowDTOPage(Page<?> page, @Param("goodsname") String goodsname, @Param("username") String username);
}
