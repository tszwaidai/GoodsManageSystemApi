package com.gms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author tszwaidai
 * @since 2024-06-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("borrow")
@ApiModel(value = "Borrow对象", description = "")
public class Borrow implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "borrowId", type = IdType.AUTO)
    private Integer borrowId;

    @TableField("userId")
    private Integer userId;

    @TableField("goodsId")
    private Integer goodsId;

    @TableField("borrowTime")
    private LocalDateTime borrowTime;

    @TableField("returnTime")
    private LocalDateTime returnTime;


}
