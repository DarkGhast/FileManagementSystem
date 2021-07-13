package org.darkghast.fms.filter;

import org.darkghast.fms.entity.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限过滤器
 * 在用户访问敏感资源的时候 验证一下用户是否登录
 * 如果没有登录 重定向回登录页面
 * 如果已登录 那就放行
 * 在使用web.xml配置时，filter-mapping需要放在框架前，否则会因为绕过过滤器导致失效
 * 因此，在使用@WebFilter注解时过滤器会被绕过，需要在web.xml进行配置
 */
@WebFilter(urlPatterns = {"/user/*"})
public class AuthorityFilter extends HttpFilter {
    @Override
    public void init(FilterConfig filterConfig) {
        System.out.println("AuthorityFilter初始化");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //1.需要把ServletRequest ServletResponse 强转
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        if (user != null) {
            //说明已经登陆过,放行
            chain.doFilter(request, response);
        } else {
            //没有登录
            httpServletResponse.sendRedirect("../login.html");
        }
    }

    @Override
    public void destroy() {
        System.out.println("AuthorityFilter被摧毁");
    }
}
