package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysCategory;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysCategoryService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 类别信息
 */
@RestController
@RequestMapping("/system/category")
public class SysCategoryController extends BaseController {
    
    @Autowired
    private ISysCategoryService categoryService;

    /**
     * 获取类别列表
     */
    @PreAuthorize("@ss.hasPermi('system:category:list')")
    @GetMapping("/list")
    public AjaxResult list(SysCategory category) {
        List<SysCategory> categories = categoryService.selectCategoryList(category);
        return success(categories);
    }

    /**
     * 查询类别列表（排除节点）
     */
    @PreAuthorize("@ss.hasPermi('system:category:list')")
    @GetMapping("/list/exclude/{categoryId}")
    public AjaxResult excludeChild(@PathVariable(value = "categoryId", required = false) Long categoryId) {
        List<SysCategory> categories = categoryService.selectCategoryList(new SysCategory());
        categories.removeIf(d -> d.getId().equals(categoryId) || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), categoryId + ""));
        return success(categories);
    }

    /**
     * 根据类别编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:category:query')")
    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable Long categoryId) {
        return success(categoryService.selectCategoryById(categoryId));
    }

    /**
     * 获取类别下拉树列表
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysCategory category) {
        List<SysCategory> categories = categoryService.selectCategoryList(category);
        return success(categoryService.buildCategoryTreeSelect(categories));
    }

    /**
     * 新增类别
     */
    @PreAuthorize("@ss.hasPermi('system:category:add')")
    @Log(title = "类别管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysCategory category) {
        if (!categoryService.checkCategoryNameUnique(category)) {
            return error("新增类别'" + category.getName() + "'失败，类别名称已存在");
        }
        return toAjax(categoryService.insertCategory(category));
    }

    /**
     * 修改类别
     */
    @PreAuthorize("@ss.hasPermi('system:category:edit')")
    @Log(title = "类别管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysCategory category) {
        if (!categoryService.checkCategoryNameUnique(category)) {
            return error("修改类别'" + category.getName() + "'失败，类别名称已存在");
        }
        if (category.getParentId().equals(category.getId())) {
            return error("修改类别'" + category.getName() + "'失败，上级类别不能是自己");
        }
        return toAjax(categoryService.updateCategory(category));
    }

    /**
     * 删除类别
     */
    @PreAuthorize("@ss.hasPermi('system:category:remove')")
    @Log(title = "类别管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{categoryId}")
    public AjaxResult remove(@PathVariable Long categoryId) {
        if (categoryService.hasChildByCategory(categoryId)) {
            return warn("存在下级类别,不允许删除");
        }
        return toAjax(categoryService.deleteCategory(categoryId));
    }
} 