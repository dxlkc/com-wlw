package com.lkc.filter;

import com.lkc.Utils.RedisUtil;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class checkFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (request.getRequestURI().equals("/industry/add/industry")) {
            //获取token
            String tokenvalue = request.getHeader("token");
            //获取name（用户名）（name 可有可无）
            String requestName = request.getParameter("name");

            //若没有token则返回505
            if (tokenvalue != null) {
                RedisUtil redisUtil = RedisUtil.getInstance();
                //刷新会话时间
                redisUtil.expire(tokenvalue, 600);
                String name = String.valueOf(redisUtil.get(tokenvalue));

                //若token存在 并且 前端传了name ： 若token对应的name和requestName 相等  则放行
                //若token存在 并且 前端没传name ： 若token存在（通过判断是否有对应name值） 则放行
                if (name != null && (requestName == null || requestName.equals(name))) {
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                }
            }
            response.sendError(505);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
