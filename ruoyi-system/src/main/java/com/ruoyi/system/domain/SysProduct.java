package com.ruoyi.system.domain;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntityNew;
import com.ruoyi.common.utils.bean.Query;
import lombok.Data;

/**
 * 产品信息表 (sys_product) 实体类
 */
@Data
@TableName("sys_product")
public class SysProduct extends BaseEntityNew {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private Query query;

    @Excel(name = "产品编号")
    private String code;

    @Excel(name = "产品名称")
    private String name;

    @Excel(name = "产品标题")
    private String title;

    @Excel(name = "产品排序")
    private Integer sort;

    @Excel(name = "产品内容")
    private String context;

    @Excel(name = "产品备注")
    private String remark;

}
