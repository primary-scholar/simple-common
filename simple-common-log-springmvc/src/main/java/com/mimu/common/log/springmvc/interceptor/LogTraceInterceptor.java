package com.mimu.common.log.springmvc.interceptor;


import com.mimu.common.constants.ContextManager;
import com.mimu.common.constants.NounConstant;
import com.mimu.common.constants.TraceContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
public class LogTraceInterceptor implements HandlerInterceptor {
    private static final Logger IO = LoggerFactory.getLogger("IO");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String traceId = getOrGenerateTraceId(request);
        putStartTimeInRequest(request);
        try {
            MDC.put(NounConstant.TRACE_ID, traceId);
            MDC.put(NounConstant.PROTOCOL,
                    new StringBuilder(NounConstant.PROTOCOL_HTTP).append(NounConstant.COLON).append(request.getMethod()).toString());
            MDC.put(NounConstant.URI, request.getRequestURI());
            MDC.put(NounConstant.URL, getFullUrl(request));
            MDC.put(NounConstant.REQUEST, getRequest(request));
            IO.info("");
        } catch (Exception e) {
            IO.warn("LogTraceInterceptor preHandle error", e);
        }
        return Boolean.TRUE;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        try {
            Long startTime = (Long) request.getAttribute(NounConstant.START_TIME);
            long cost = System.currentTimeMillis() - startTime;
            MDC.put(NounConstant.PROTOCOL,
                    new StringBuilder(NounConstant.PROTOCOL_HTTP).append(NounConstant.COLON).append(request.getMethod()).toString());
            MDC.put(NounConstant.URI, request.getRequestURI());
            MDC.put(NounConstant.URL, getFullUrl(request));
            MDC.put(NounConstant.REQUEST, getRequest(request));
            MDC.put(NounConstant.COST, String.valueOf(cost));
            MDC.put(NounConstant.RESPONSE, getResponse(response));
            IO.info("");
        } catch (Exception e) {
            IO.warn("LogTraceInterceptor postHandle error", e);
        } finally {
            MDC.remove(NounConstant.TRACE_ID);
            MDC.remove(NounConstant.COST);
            MDC.remove(NounConstant.PROTOCOL);
            MDC.remove(NounConstant.URI);
            MDC.remove(NounConstant.URL);
            MDC.remove(NounConstant.REQUEST);
            MDC.remove(NounConstant.RESPONSE);
            IO.info("");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {

    }

    private String getOrGenerateTraceId(HttpServletRequest request) {
        TraceContext context = ContextManager.getContext();
        String requestId = request.getHeader(NounConstant.REQUEST_ID);
        if (Objects.nonNull(requestId)){
            context.setTraceId(requestId);
        }
        return context.getTraceId();
    }

    private void putStartTimeInRequest(HttpServletRequest request) {
        Object startTimeObj = request.getAttribute(NounConstant.START_TIME);
        if (Objects.isNull(startTimeObj)) {
            startTimeObj = System.currentTimeMillis();
        }
        request.setAttribute(NounConstant.START_TIME, startTimeObj);
    }

    private String getFullUrl(HttpServletRequest request) throws UnsupportedEncodingException {
        String uri = request.getRequestURI();
        if (HttpMethod.GET.equals(HttpMethod.resolve(request.getMethod()))) {
            String queryString = request.getQueryString();
            if (StringUtils.isNotEmpty(queryString)) {
                queryString = URLDecoder.decode(request.getQueryString(), request.getCharacterEncoding());
                uri = new StringBuilder(uri).append(NounConstant.QUESTION).append(queryString).toString();
            }
            return uri;
        }
        return uri;
    }

    private String getRequest(HttpServletRequest request) throws UnsupportedEncodingException {
        String param = StringUtils.EMPTY;
        if (request instanceof ContentCachingRequestWrapper) {
            ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
            if (HttpMethod.GET.equals(HttpMethod.resolve(request.getMethod()))) {
                param = URLDecoder.decode(requestWrapper.getQueryString(), requestWrapper.getCharacterEncoding());
            }
            if (HttpMethod.POST.equals(HttpMethod.resolve(request.getMethod()))) {
                String encoding = requestWrapper.getCharacterEncoding();
                byte[] content = requestWrapper.getContentAsByteArray();
                param = new String(content, encoding);
            }
            return param;
        }
        return param;
    }

    private String getResponse(HttpServletResponse response) throws UnsupportedEncodingException {
        if (response instanceof ContentCachingResponseWrapper) {
            ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
            String responseStr;
            String encoding = responseWrapper.getCharacterEncoding();
            byte[] content = responseWrapper.getContentAsByteArray();
            responseStr = new String(content, encoding);
            return responseStr;
        }
        return StringUtils.EMPTY;
    }

}
