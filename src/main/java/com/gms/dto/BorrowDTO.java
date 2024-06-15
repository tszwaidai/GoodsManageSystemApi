package com.gms.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author tszwaidai
 * @version 1.0
 * @description: TODO
 * @date 2024/6/13 15:34
 */
@Data
public class BorrowDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer borrowId;
    private Integer userId;
    private Integer goodsId;
    private LocalDateTime borrowTime;
    private LocalDateTime returnTime;
    private String goodsName;
    private String userName;
    private Integer status;

}
