package com.ruoyi.system.domain;


import com.baomidou.mybatisplus.annotation.*;
import com.ruoyi.common.utils.bean.Query;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 产品信息表 (sys_product) 实体类
 */
@Data
@TableName("sys_product")
public class SysProduct {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private Query query;


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;

    /**
     * 创建人名称
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorUsername;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long modifierId;

    /**
     * 修改人名称
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String modifierUsername;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifyTime;

    /**
     * 版本号
     */
    @Version
    private Integer version;

    /**
     * 是否删除： true 删除 false 未删除
     */
    @TableLogic
    private Boolean deleted;


    private String code;

    private String name;

    private Integer sort;

//    @JsonDeserialize(using = HtmlDeserializer.class)
    private String context;

    private String remark;

}
