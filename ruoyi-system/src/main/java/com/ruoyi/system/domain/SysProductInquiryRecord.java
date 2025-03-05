package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_product_inquiry_record")
public class SysProductInquiryRecord {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;
    private String email;
    private String mobile;
    private String company;
    private String remark;
    @TableLogic
    private Boolean deleted;
    private Long creatorId;
    private String creatorUsername;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}