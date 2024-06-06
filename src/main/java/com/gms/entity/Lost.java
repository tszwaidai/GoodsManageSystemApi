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
@TableName("lost")
@ApiModel(value = "Lost对象", description = "")
public class Lost implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "lostId", type = IdType.AUTO)
    private Integer lostId;

    @TableField("userId")
    private Integer userId;

    @TableField("goodsId")
    private Integer goodsId;

    @TableField("lostTime")
    private LocalDateTime lostTime;


}
