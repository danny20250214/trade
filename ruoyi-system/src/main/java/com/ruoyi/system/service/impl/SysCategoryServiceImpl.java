package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysCategory;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.SysCategoryMapper;
import com.ruoyi.system.service.ISysCategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类别管理 服务实现
 */
@Service
public class SysCategoryServiceImpl extends ServiceImpl<SysCategoryMapper, SysCategory> implements ISysCategoryService {

    @Override
    public List<SysCategory> selectCategoryList(SysCategory category) {
        return baseMapper.selectCategoryList(category);
    }

    @Override
    public SysCategory selectCategoryById(Long categoryId) {
        return getById(categoryId);
    }

    @Override
    public List<TreeSelect> buildCategoryTreeSelect(List<SysCategory> categories) {
        List<SysCategory> categoryTrees = buildCategoryTree(categories);
        return categoryTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }



    /**
     * 构建前端所需要树结构
     */
    private List<SysCategory> buildCategoryTree(List<SysCategory> categories) {
        List<SysCategory> returnList = new ArrayList<>();
        List<Long> tempList = categories.stream().map(SysCategory::getId).collect(Collectors.toList());
        for (SysCategory category : categories) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(category.getParentId())) {
                recursionFn(categories, category);
                returnList.add(category);
            }
        }
        if (returnList.isEmpty()) {
            returnList = categories;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysCategory> list, SysCategory t) {
        // 得到子节点列表
        List<SysCategory> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysCategory tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysCategory> getChildList(List<SysCategory> list, SysCategory t) {
        List<SysCategory> tlist = new ArrayList<>();
        for (SysCategory n : list) {
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().equals(t.getId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysCategory> list, SysCategory t) {
        return getChildList(list, t).size() > 0;
    }

    @Override
    public boolean hasChildByCategory(Long categoryId) {
        LambdaQueryWrapper<SysCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysCategory::getParentId, categoryId)
                   .eq(SysCategory::getDeleted, 0);
        return count(queryWrapper) > 0;
    }

    @Override
    public boolean checkCategoryNameUnique(SysCategory category) {
        Long categoryId = StringUtils.isNull(category.getId()) ? -1L : category.getId();
        LambdaQueryWrapper<SysCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysCategory::getName, category.getName())
                   .eq(SysCategory::getParentId, category.getParentId())
                   .eq(SysCategory::getDeleted, 0);
        
        SysCategory info = getOne(queryWrapper);
        return info == null || info.getId().equals(categoryId);
    }

    @Override
    public boolean insertCategory(SysCategory category) {
        if (category.getParentId() == null) {
            category.setParentId(0L);
            category.setAncestors("0");
        } else {
            SysCategory parent = getById(category.getParentId());
            if (parent != null) {
                category.setAncestors(parent.getAncestors() + "," + parent.getId());
            }
        }
        return save(category);
    }

    @Override
    public boolean updateCategory(SysCategory category) {
        SysCategory oldCategory = getById(category.getId());
        if (oldCategory != null && !oldCategory.getParentId().equals(category.getParentId())) {
            // 更新子元素关系
            updateChildren(category.getId(), category.getAncestors());
        }
        return updateById(category);
    }

    /**
     * 更新子元素关系
     */
    private void updateChildren(Long categoryId, String newAncestors) {
        LambdaQueryWrapper<SysCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysCategory::getDeleted, 0)
                   .apply("find_in_set({0}, ancestors)", categoryId);
        
        List<SysCategory> children = list(queryWrapper);
        
        for (SysCategory child : children) {
            child.setAncestors(newAncestors + "," + categoryId);
        }
        
        if (!children.isEmpty()) {
            updateBatchById(children);
        }
    }

    @Override
    public boolean deleteCategory(Long categoryId) {
        return removeById(categoryId);
    }
} 