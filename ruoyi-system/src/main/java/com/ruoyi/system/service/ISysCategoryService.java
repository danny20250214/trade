package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysCategory;

import java.util.List;

public interface ISysCategoryService extends IService<SysCategory> {
    
    /**
     * 查询类别列表
     */
    List<SysCategory> selectCategoryList(SysCategory category);

    /**
     * 根据ID查询类别
     */
    SysCategory selectCategoryById(Long categoryId);

    /**
     * 构建前端所需要树结构
     */
    List<TreeSelect> buildCategoryTreeSelect(List<SysCategory> categories);

    /**
     * 是否存在子节点
     */
    boolean hasChildByCategory(Long categoryId);

    /**
     * 查询类别是否存在用户
     */
    boolean checkCategoryNameUnique(SysCategory category);

    /**
     * 新增类别信息
     */
    boolean insertCategory(SysCategory category);

    /**
     * 修改类别信息
     */
    boolean updateCategory(SysCategory category);

    /**
     * 删除类别管理信息
     */
    boolean deleteCategory(Long categoryId);
} 