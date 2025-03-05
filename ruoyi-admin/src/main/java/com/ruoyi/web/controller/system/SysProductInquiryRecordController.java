package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysProductInquiryRecord;
import com.ruoyi.system.service.ISysProductInquiryRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/system/inquiry")
public class SysProductInquiryRecordController extends BaseController {
    @Autowired
    private ISysProductInquiryRecordService inquiryRecordService;

    @PreAuthorize("@ss.hasPermi('system:inquiry:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysProductInquiryRecord inquiryRecord) {
        startPage();
        List<SysProductInquiryRecord> list = inquiryRecordService.selectProductInquiryRecordList(inquiryRecord);
        return getDataTable(list);
    }

    @Anonymous
//    @PreAuthorize("@ss.hasPermi('system:inquiry:add')")
    @Log(title = "产品咨询", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysProductInquiryRecord inquiryRecord) {
        return toAjax(inquiryRecordService.save(inquiryRecord));
    }

    @PreAuthorize("@ss.hasPermi('system:inquiry:edit')")
    @Log(title = "产品咨询", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysProductInquiryRecord inquiryRecord) {
        return toAjax(inquiryRecordService.updateById(inquiryRecord));
    }

    @PreAuthorize("@ss.hasPermi('system:inquiry:remove')")
    @Log(title = "产品咨询", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(inquiryRecordService.removeByIds(Arrays.asList(ids)));
    }

    @PreAuthorize("@ss.hasPermi('system:inquiry:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(inquiryRecordService.getById(id));
    }
}