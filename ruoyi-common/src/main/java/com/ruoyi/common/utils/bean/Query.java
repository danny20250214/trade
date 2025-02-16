package com.ruoyi.common.utils.bean;


import javax.validation.constraints.NotNull;

/**
 * 分页工具
 *
 * @author michael
 */
public class Query {

    /**
     * 当前页
     */
    @NotNull(message = "当前页不能为空")
    private Integer current;

    /**
     * 每页的数量
     */
    @NotNull(message = "分页大小不能为空")
    private Integer size;



    public Query() {
    }

    public Query(Integer current, Integer size) {
        this.current = current;
        this.size = size;
    }


    public Integer getCurrent() {
        return current;
    }


    public Integer getSize() {
        return size;
    }

}
