package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.SysProductInquiryRecord;

import java.util.List;

public interface ISysProductInquiryRecordService extends IService<SysProductInquiryRecord> {
    List<SysProductInquiryRecord> selectProductInquiryRecordList(SysProductInquiryRecord record);
}