package com.mimu.common.log.springmvc.interceptor;


import com.alibaba.fastjson.JSONObject;
import com.mimu.common.constants.*;
import com.mimu.common.trace.CarrierItem;
import com.mimu.common.trace.ContextCarrier;
import com.mimu.common.trace.ContextManager;
import com.mimu.common.trace.TraceContext;
import com.mimu.common.trace.span.TraceSpan;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.*;

@Component
public class LogTraceInterceptor implements HandlerInterceptor {
    private static final Logger IO = LoggerFactory.getLogger("IO");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            request.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            ContextCarrier contextCarrier = new ContextCarrier();
            fillCarrier(request, contextCarrier);
            ContextManager.createEntrySpan(StringUtils.EMPTY, contextCarrier);
        } catch (UnsupportedEncodingException e) {
        }
        return Boolean.TRUE;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        try {
            TraceSpan traceSpan = ContextManager.activeSpan();
            ContextManager.stopSpan();
        } catch (Exception e) {
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {

    }

    private String getOrGenerateTraceId(HttpServletRequest request) {
        TraceContext context = ContextManager.getContext();
        String requestId = request.getHeader(NounConstant.REQUEST_ID);
        if (Objects.nonNull(requestId)) {
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

    private Map<String, Object> getParameter(HttpServletRequest request) {
        String requestStr;
        try {
            requestStr = getRequest(request);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        if (HttpMethod.GET.equals(HttpMethod.resolve(request.getMethod()))) {
            return RequestParamResolver.decodeParams(requestStr);
        }
        if (HttpMethod.POST.equals(HttpMethod.resolve(request.getMethod()))) {
            return JSONObject.parseObject(requestStr);
        }
        return Collections.emptyMap();
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

    private void fillCarrier(HttpServletRequest request, ContextCarrier carrier) {
        List<CarrierItem> itemList = carrier.items();
        for (CarrierItem item : itemList) {
            item.setValue(request.getHeader(item.getKey()));
        }
        for (CarrierItem item : itemList) {
            if (NounConstant.TRACE_ID.equals(item.getKey())) {
                String value = item.getValue();
                if (StringUtils.isEmpty(value)) {
                    Map<String, Object> parameter = getParameter(request);
                    String requestId = parameter.getOrDefault(NounConstant.REQUEST_ID, StringUtils.EMPTY).toString();
                    item.setValue(requestId);
                    if (StringUtils.isEmpty(carrier.getTraceId())) {
                        carrier.setTraceId(requestId);
                    }
                }
            }
        }
    }


}
