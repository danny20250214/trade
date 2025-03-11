package com.ruoyi.framework.config;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.ruoyi.common.config.RuoYiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.utils.ServletUtils;

import java.util.Objects;

/**
 * 服务相关配置
 * 
 * @author ruoyi
 */
@Component
public class ServerConfig
{
    /**
     * 获取完整的请求路径，包括：域名，端口，上下文访问路径
     * 
     * @return 服务地址
     */
    @Autowired
    private RuoYiConfig ruoYiConfig;

    public String getUrl()
    {
        HttpServletRequest request = ServletUtils.getRequest();
        String domain = getDomain(request);
        if(ObjectUtils.isNotEmpty(ruoYiConfig.getPicDomain())){
            domain = domain.replace("localhost",ruoYiConfig.getPicDomain());
        }
        return domain;
    }

    public String getDomain(HttpServletRequest request)
    {
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString();
    }
}
