package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "Filter 0", urlPatterns = {"/*"})

public class Filter0_Encoding implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Filter 0 - encoding begins");
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response=(HttpServletResponse)servletResponse;
        String path= request.getRequestURI();
        String method=request.getMethod();
        if (path.contains("/login")){
            System.out.println("未设置字符编码格式");
        }
        else if (path.contains(".css") || path.contains(".js") || path.contains(".png") || path.contains(".jpg"))
        {
            //如果发现是css或者js文件，直接放行
            filterChain.doFilter(request, response);
        }
        else {
            System.out.println(method);
            response.setContentType("text/html;charset=UTF-8");
            System.out.println("设置响应对象字符编码格式为utf8");
            if (method.equals("POST")||method.equals("PUT")){
                request.setCharacterEncoding("UTF-8");
                System.out.println("设置请求对象字符编码格式为utf8");
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("Filter 0 - encoding ends");
    }
}
