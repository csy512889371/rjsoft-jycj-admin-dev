package com.rjsoft.jycj.admin.web.interceptor;


import com.rjsoft.magina.web.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Http 日志拦截器
 * 用于调试模式下对 http 请求响应的记录
 *
 * Created by zhangzhiyi on 2018/3/8.
 */
@Slf4j
public class HttpLogInterceptor extends HandlerInterceptorAdapter {

    private static final String START_TAG = "------------------------------ HTTP Request Start ------------------------------";
    private static final String END_TAG   = "------------------------------ HTTP Response End ------------------------------\n";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(log.isDebugEnabled()){
            log.debug(START_TAG);
            // 记录下请求内容
            log.debug("    [用户请求]");
            log.debug("        应用协议 : {}", request.getScheme());
            log.debug("        协议版本 : {}", request.getProtocol());
            log.debug("        请求链接 : {}", request.getRequestURL().toString());
            log.debug("        请求资源 : {}", request.getRequestURI());
            log.debug("        请求方法 : {}", request.getMethod());
            log.debug("        请求参数 :");
            HttpUtil.printRequestParameter(request);
            log.debug("        请求头   : ");
            HttpUtil.printRequestHeader(request);

            log.debug("        用户信息 : ");
            log.debug("            Session  : {}", request.getRequestedSessionId());
            log.debug("            Cookie   :");
            HttpUtil.printRequestCookie(request);
            log.debug("        远程端口 : {}: {}", request.getRemoteAddr(), request.getRemotePort());
            log.debug("        本地端口 : {}: {}", request.getLocalAddr(), request.getLocalPort());
            log.debug("        语言编码 : {}", request.getLocale().toLanguageTag());
            log.debug("        内容类型 : {}", request.getContentType());
            log.debug("        字符编码 : {}", request.getCharacterEncoding());
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
        super.postHandle(request, response, handler, mav);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("    [服务器响应]");
            log.debug("        响应头   : ");
            HttpUtil.printResponseHeader(response);
            log.debug("        响应状态 : {}", response.getStatus());
            log.debug("        语言编码 : {}", response.getLocale().toLanguageTag());
            log.debug("        内容类型 : {}", response.getContentType());
            log.debug("        字符编码 : {}", response.getCharacterEncoding());
            //log.debug("        响应内容 : {}", "");
            // 处理完请求，返回内容
            log.debug(END_TAG);
        }
        super.afterCompletion(request, response, handler, exception);
    }

}
