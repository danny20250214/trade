package com.ruoyi.framework.interceptor.impl;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author danny
 * @description
 * @Date 16/02/2025
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    public MyMetaObjectHandler() {
        log.info("MyMetaObjectHandler 创建");
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            // 获取当前用户信息
            LoginUser loginUser = SecurityUtils.getLoginUser();
            if (loginUser != null) {
                // 创建者ID
                this.strictInsertFill(metaObject, "creatorId", Long.class, loginUser.getUserId());
                // 创建者用户名
                this.strictInsertFill(metaObject, "creatorUsername", String.class, loginUser.getUsername());
                // 修改者信息（初始时与创建者相同）
                this.strictInsertFill(metaObject, "modifierId", Long.class, loginUser.getUserId());
                this.strictInsertFill(metaObject, "modifierUsername", String.class, loginUser.getUsername());
            }
        } catch (Exception e) {
            // 处理异常情况
        }
        // 创建时间
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        // 修改时间
        this.strictInsertFill(metaObject, "modifyTime", LocalDateTime.class, LocalDateTime.now());
        // 版本号
        this.strictInsertFill(metaObject, "version", Integer.class, 1);
        // 删除标志
        this.strictInsertFill(metaObject, "deleted", Integer.class, 0);
        // 其他需要自动填充的字段...
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            // 获取当前用户信息
            LoginUser loginUser = SecurityUtils.getLoginUser();
            if (loginUser != null) {
                // 更新修改者信息
                this.strictUpdateFill(metaObject, "modifierId", Long.class, loginUser.getUserId());
                this.strictUpdateFill(metaObject, "modifierUsername", String.class, loginUser.getUsername());
            }
        } catch (Exception e) {
            // 处理异常情况
        }
        // 更新时间
        this.strictUpdateFill(metaObject, "modifyTime", LocalDateTime.class, LocalDateTime.now());
    }
}
