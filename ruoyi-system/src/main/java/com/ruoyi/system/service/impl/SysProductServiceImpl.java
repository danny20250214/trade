package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.BaseEntityNew;
import com.ruoyi.system.domain.SysProduct;
import com.ruoyi.system.mapper.SysProductMapper;
import com.ruoyi.system.service.ISysProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author danny
 * @description
 * @Date 13/02/2025
 */
@Service
public class SysProductServiceImpl extends ServiceImpl<SysProductMapper, SysProduct> implements ISysProductService {
    @Autowired
    private SysProductMapper mapper;

    @Override
    public SysProduct getById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public List<SysProduct> selectProductList(SysProduct product) {
        return mapper.selectProductList(product);
    }

    @Override
    public Boolean insertProduct(SysProduct product) {
        return this.save(product);
    }

    @Override
    public int updateProduct(SysProduct product) {
        return mapper.updateById(product);
    }

    @Override
    public boolean deleteProductByIds(Long[] ids) {
        LambdaUpdateWrapper<SysProduct> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(SysProduct::getId, ids)
                .set(SysProduct::getDeleted, 1);
        return this.update(updateWrapper);
    }


}
