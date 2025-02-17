package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.SysProduct;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author danny
 * @description
 * @Date 13/02/2025
 */
@Mapper
public interface SysProductMapper extends BaseMapper<SysProduct>{
    List<SysProduct> selectProductList(SysProduct sysProduct);

}
