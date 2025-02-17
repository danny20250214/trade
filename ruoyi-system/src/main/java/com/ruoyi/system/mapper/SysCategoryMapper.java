package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.core.domain.entity.SysCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysCategoryMapper extends BaseMapper<SysCategory> {
    List<SysCategory> selectCategoryList(SysCategory sysCategory);
} 