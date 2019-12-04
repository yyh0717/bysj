package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

@WebFilter(filterName = "Filter 1", urlPatterns = {"/*"})
public class Filter1_Date implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Filter 1 - date begins");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getRequestURI();
        if (path.contains(".css") || path.contains(".js") || path.contains(".png") || path.contains(".jpg"))
        {
            //如果发现是css或者js文件，直接放行
            filterChain.doFilter(request, response);
        } else {
            Calendar cal = Calendar.getInstance();
            String time = cal.get(Calendar.YEAR) + "年" + (cal.get(Calendar.MONTH) + 1) + "月" + cal.get(Calendar.DATE) + "日" + cal.get(Calendar.HOUR_OF_DAY) + ": " + cal.get(Calendar.MINUTE);
            System.out.println(path + " @ " + time);
            filterChain.doFilter(servletRequest, servletResponse);
            System.out.println("Filter 1 - date ends");
        }
    }
}
