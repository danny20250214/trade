package com.ruoyi.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.domain.SysProduct;

import java.util.List;

/**
 * @author danny
 * @description
 * @Date 13/02/2025
 */
public interface ISysProductService extends IService<SysProduct> {
    SysProduct getById(Long id);



    Boolean insertProduct(SysProduct product);
    
    int updateProduct(SysProduct product);
    
    int deleteProductByIds(Long[] ids);

    List<SysProduct> selectProductList(SysProduct post);
}
