package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.SysProductInquiryRecord;

import java.util.List;

public interface SysProductInquiryRecordMapper extends BaseMapper<SysProductInquiryRecord> {

    List<SysProductInquiryRecord> selectProductInquiryRecordList(SysProductInquiryRecord record);

}