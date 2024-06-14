package com.gms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
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
@TableName("goods_info")
@ApiModel(value = "GoodsInfo对象", description = "")
public class GoodsInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "goodsId", type = IdType.AUTO)
    private Integer goodsId;

    @TableField("goodsName")
    private String goodsName;

    @TableField("goodsTypeId")
    private Integer goodsTypeId;

    @TableField("goodsDesc")
    private String goodsDesc;

    @ApiModelProperty("")
    @TableField("status")
    private Integer status;

    @TableField("goodsPrice")
    private BigDecimal goodsPrice;


}
