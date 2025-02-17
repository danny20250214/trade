package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysProduct;
import com.ruoyi.system.service.ISysProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.List;

/**
 * @author danny
 * @description
 * @Date 13/02/2025
 */
@RestController
@RequestMapping("/system/product")
public class SysProductController  extends BaseController {
    @Autowired
    private ISysProductService productService;

//    @PreAuthorize("@ss.hasPermi('system:post:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysProduct product)
    {
        startPage();
        List<SysProduct> list = productService.selectProductList(product);
        return getDataTable(list);
    }

//    @PreAuthorize("@ss.hasPermi('system:product:get')")
    @GetMapping(value = "/{id}")
    public AjaxResult getById(@PathVariable Long id)
    {
        return success(productService.getById(id));
    }

    @PreAuthorize("@ss.hasPermi('system:product:export')")
    @Log(title = "产品管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysProduct product) {
        List<SysProduct> list = productService.selectProductList(product);
        ExcelUtil<SysProduct> util = new ExcelUtil<>(SysProduct.class);
        util.exportExcel(response, list, "产品数据");
    }

    @PreAuthorize("@ss.hasPermi('system:product:add')")
    @Log(title = "产品管理", businessType = BusinessType.INSERT)
    @PostMapping( consumes = "application/json", produces = "application/json")
    public AjaxResult add(@RequestBody SysProduct product) {
        String decodedContext = new String(Base64.getDecoder().decode(product.getContext()));
        product.setContext(decodedContext);
        return toAjax(productService.insertProduct(product));
    }

    @PreAuthorize("@ss.hasPermi('system:product:edit')")
    @Log(title = "产品管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysProduct product) {
        String decodedContext = new String(Base64.getDecoder().decode(product.getContext()));
        product.setContext(decodedContext);
        return toAjax(productService.updateProduct(product));
    }

    @PreAuthorize("@ss.hasPermi('system:product:remove')")
    @Log(title = "产品管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(productService.deleteProductByIds(ids));
    }
}
