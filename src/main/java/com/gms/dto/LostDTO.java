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
 * @date 2024/6/14 18:12
 */
@Data
public class LostDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "lostId", type = IdType.AUTO)
    private Integer lostId;

    @TableField("userId")
    private Integer userId;

    @TableField("goodsId")
    private Integer goodsId;

    @TableField("lostTime")
    private LocalDateTime lostTime;
    private String goodsName;
    private String userName;

}
