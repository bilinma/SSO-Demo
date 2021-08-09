package com.jack.orderservice.interceptor;

import com.jack.orderservice.utils.HttpUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;

public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断客户端到底有没有带 jsessionid过来
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("login").equals("login")) {
            return true;
        }

        // 判断是否有token，sso-server有没有颁发token给我
        String token = request.getParameter("token");
        System.out.println(token);
        if(token!=null){
            // 判断token有效性
            String reqUrl="http://www.sso.com:9090/checkToken";
            String content="token="+token;
            // java.net.URLConnection请求
            String result = HttpUtil.sendReq(reqUrl, content);
            if("correct".equals(result)) {
                // 为了后续能够免登录，要创建session对象
                // request.getSession.setAttribute("login","login");
                request.getSession().setAttribute("login","login");
                return true;
            }
        }

        String url = request.getRequestURL().toString();
        // 发现并没有sessionid   一旦后面登录成功了，还需要返回给用户受保护的资源  url=www.order.com:8082/order/wel
        response.sendRedirect("http://www.sso.com:9090/preLogin?url="+ URLEncoder.encode(url));

        return false;
    }
}
