package wechart.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/4
 * @description
 */
@Service
public class MyInterceptor implements HandlerInterceptor {

    Jedis jedis = new Jedis("192.168.1.66");

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        System.out.println(">>>MyInterceptor>>>>>>>在请求处理之前进行调用（Controller方法调用之前）");
        boolean flag = false;
        Cookie[] cookies = httpServletRequest.getCookies();

        if(cookies != null && cookies.length != 0) {
            try {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("wechart-cookie")) {
                        if(cookie!=null && cookie.getValue()!=null) {

                            flag = jedis.hexists("LOGININFO",cookie.getValue());

                        }
                    }
                }

                if(!flag) {
                    httpServletResponse.sendRedirect("http://localhost:8080//logininfo");
                    return false;
                }
                return true;

            } catch (Exception exp) {
                throw exp;
            }
        } else {
            //httpServletResponse.sendRedirect("logininfo");
            return true;
        }

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        System.out.println(">>>MyInterceptor>>>>>>>请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        System.out.println(">>>MyInterceptor>>>>>>>在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）");
    }
}
