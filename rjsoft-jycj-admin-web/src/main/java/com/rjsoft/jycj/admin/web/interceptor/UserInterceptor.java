package com.rjsoft.jycj.admin.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptor - 获取用户信息
 */
@Slf4j
public class UserInterceptor extends HandlerInterceptorAdapter {

    //@Autowired
    //private UserConsumerService userConsumerService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String username = request.getHeader("currentUsername");
            //LoginUserDto loginUser = userConsumerService.getCurrentUserInfo(username);
            request.setAttribute("loginUser", null);
        } catch (Exception e) {

            log.error(e.getMessage(), e);

            //response.addHeader("loginStatus", "获取当前用户信息失败");
            //response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            //TODO 由于接口目前还不稳定。先采用写死的方式。
/*            LoginUserDto loginUser = new LoginUserDto();
            loginUser.setUsername("105840843");
            loginUser.setRybs(105840843);
            loginUser.setNickname("杨建一");
            loginUser.setOrgName("厦门市中级人民法院");
            loginUser.setFydm(1615);
            loginUser.setFyfjm("D00");
            loginUser.setBmbs(105840669L);
            loginUser.setDepartmentName("司法鉴定处");
            request.setAttribute("loginUser", loginUser);*/
            return false;
        }
        return true;
    }
}
