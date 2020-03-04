package com.zhangln.push.wspush.filter;

import com.alibaba.fastjson.JSONObject;
import com.zhangln.push.wspush.vo.HttpResVo;
import com.zhangln.push.wspush.config.prop.AppProp;
import com.zhangln.push.wspush.controller.service.AccessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author sherry
 * @description
 * @date Create in 2019/12/9
 * @modified By:
 */
@WebFilter("/**")
@Component
@Slf4j
public class UrlFilter implements Filter {

    @Autowired
    private AppProp appProp;

    @Autowired
    private AntPathMatcher antPathMatcher;

    @Autowired
    private AccessService accessService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String url = httpRequest.getRequestURI();
        log.info("当前请求地址：{}", url);
//        可以被忽略的地址
        List<String> urls = appProp.getIgnoreUrl();

        for (String tmp : urls) {
            boolean flag = antPathMatcher.match(tmp, url);
            if (flag) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

//        执行token认证

        String token = httpRequest.getHeader("access");
        boolean exists = accessService.exists(token);

        if (exists) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
//            校验不通过
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setHeader("content-type", "application/json;charset=utf-8");
            log.info("token校验不通过");
            response.getWriter().write(JSONObject.toJSONString(HttpResVo.buildError("token无效："+token)));
        }


    }
}
