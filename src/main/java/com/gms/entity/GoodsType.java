package com.gms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
 * @author Jovie
 * @since 2024-06-02
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("goods_type")
@ApiModel(value = "GoodsType对象", description = "")
public class GoodsType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "goodsTypeId", type = IdType.AUTO)
    private Integer goodsTypeId;

    @TableField("goodsTypeName")
    private String goodsTypeName;

    @TableField("goodsTypeDesc")
    private String goodsTypeDesc;


}
