package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.SysProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author danny
 * @description
 * @Date 13/02/2025
 */
@Mapper
public interface SysProductMapper extends BaseMapper<SysProduct>{
    List<SysProduct> selectProductList(SysProduct sysProduct);
    /**
     * 新增产品信息
     *
     * @param sysProduct 产品信息
     * @return 影响行数
     *//*
    int insert(SysProduct sysProduct);

    *//**
     * 修改产品信息
     *
     * @param sysProduct 产品信息
     * @return 影响行数
     *//*
    int updateById(SysProduct sysProduct);

    *//**
     * 根据ID删除产品信息
     *
     * @param id 产品ID
     * @return 影响行数
     *//*
    int deleteById(@Param("id") Long id);*/

    /**
     * 根据ID查询产品信息
     *
     * @param id 产品ID
     * @return 产品信息
     */
    SysProduct selectById(@Param("id") Long id);

}
