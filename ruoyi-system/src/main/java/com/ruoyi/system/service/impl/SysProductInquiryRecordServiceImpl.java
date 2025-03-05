package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.domain.SysProduct;
import com.ruoyi.system.domain.SysProductInquiryRecord;
import com.ruoyi.system.mapper.SysProductInquiryRecordMapper;
import com.ruoyi.system.service.ISysProductInquiryRecordService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SysProductInquiryRecordServiceImpl extends ServiceImpl<SysProductInquiryRecordMapper, SysProductInquiryRecord> implements ISysProductInquiryRecordService {

    private SysProductInquiryRecordMapper mapper;

    @Override
    public List<SysProductInquiryRecord> selectProductInquiryRecordList(SysProductInquiryRecord record) {
        return mapper.selectProductInquiryRecordList(record);
    }
}