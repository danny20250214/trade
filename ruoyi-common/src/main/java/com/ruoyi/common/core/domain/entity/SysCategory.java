package com.ruoyi.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntityNew;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 类别对象 sys_category
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_category")
public class SysCategory extends BaseEntityNew {
    private static final long serialVersionUID = 1L;

    /** 父类别id */
    private Long parentId;

    /** 类别名称 */
    private String name;

    /** 显示顺序 */
    private Integer sort;

    /** 祖级列表 */
    private String ancestors;

    /** 子类别 */
    @TableField(exist = false)
    private List<SysCategory> children;
} 