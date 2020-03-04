package com.zhangln.push.wspush.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Date;

/**
 * 时间过滤器
 */
@WebFilter("/**")
@Component
@Slf4j
public class TimeFilter implements Filter {

    @Override
    public void destroy() {
        log.info("time filter destroy");
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.info("time filter start");
        long start = new Date().getTime();
        chain.doFilter(request, response);
        log.info("time filter 耗时:"+ (new Date().getTime() - start)+"毫秒");
        log.info("time filter finish");
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        log.info("time filter init");
    }

}