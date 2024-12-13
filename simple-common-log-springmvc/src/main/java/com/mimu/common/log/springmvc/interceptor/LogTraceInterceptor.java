package com.mimu.common.log.springmvc.interceptor;


import com.alibaba.fastjson.JSONObject;
import com.mimu.common.constants.*;
import com.mimu.common.trace.ContextCarrier;
import com.mimu.common.trace.ContextManager;
import com.mimu.common.trace.span.TraceSpan;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
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
            TraceSpan entrySpan = ContextManager.createEntrySpan(StringUtils.EMPTY, contextCarrier);
            fillSpanTag(request, response, entrySpan);
        } catch (UnsupportedEncodingException e) {
        }
        return Boolean.TRUE;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        try {
            TraceSpan traceSpan = ContextManager.activeSpan();
            fillSpanTag(request, response, traceSpan);
            ContextManager.stopSpan();
        } catch (Exception e) {
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {

    }

    private Map<String, Object> getParameter(HttpServletRequest request) {
        String requestStr = getRequest(request);
        if (HttpMethod.GET.equals(HttpMethod.resolve(request.getMethod()))) {
            return RequestParamResolver.decodeParams(requestStr);
        }
        if (HttpMethod.POST.equals(HttpMethod.resolve(request.getMethod()))) {
            JSONObject parsed = JSONObject.parseObject(requestStr);
            return Objects.isNull(parsed) ? Collections.emptyMap() : parsed;
        }
        return Collections.emptyMap();
    }

    private String getRequest(HttpServletRequest request) {
        String param = StringUtils.EMPTY;
        try {
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
            } else if (request instanceof StandardMultipartHttpServletRequest) {
                StandardMultipartHttpServletRequest multipartRequest = (StandardMultipartHttpServletRequest) request;
                Map<String, String[]> parameterMap = multipartRequest.getParameterMap();
                HashMap<String, Object> parameter = new HashMap<>();
                for (Map.Entry<String, String[]> keyValue : parameterMap.entrySet()) {
                    parameter.put(keyValue.getKey(), keyValue.getValue()[0]);
                }
                param = JSONObject.toJSONString(parameter);
            }
        } catch (UnsupportedEncodingException e) {
        }
        return param;
    }

    private String getResponse(HttpServletResponse response) {
        try {
            if (response instanceof ContentCachingResponseWrapper) {
                ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
                String responseStr;
                String encoding = responseWrapper.getCharacterEncoding();
                byte[] content = responseWrapper.getContentAsByteArray();
                responseStr = new String(content, encoding);
                return responseStr;
            }
        } catch (UnsupportedEncodingException e) {
        }
        return StringUtils.EMPTY;
    }

    private void fillCarrier(HttpServletRequest request, ContextCarrier carrier) {
        Map<String, String> tags = carrier.emptyTags();
        tags.replaceAll((k, v) -> request.getHeader(k));
        Map<String, Object> parameter = getParameter(request);
        if (StringUtils.isEmpty(carrier.getTraceId())) {
            String traceId = tags.get(NounConstant.TRACE_ID);
            if (StringUtils.isEmpty(traceId)) {
                traceId = parameter.getOrDefault(NounConstant.REQUEST_ID, StringUtils.EMPTY).toString();
            }
            carrier.setTraceId(traceId);
        }
        if (Objects.isNull(carrier.getParentSpanId())) {
            String parentSpanId = tags.get(NounConstant.PARENT_SPAN_ID);
            if (StringUtils.isEmpty(parentSpanId)) {
                parentSpanId = String.valueOf(ContextCarrier.DEFAULT_PARENT_SPAN_ID);
            }
            carrier.setParentSpanId(NumberUtils.toInt(parentSpanId, ContextCarrier.DEFAULT_PARENT_SPAN_ID));
        }
        if (Objects.isNull(carrier.getSpanId())) {
            String spanId = tags.get(NounConstant.SPAN_ID);
            if (StringUtils.isEmpty(spanId)) {
                spanId = String.valueOf(ContextCarrier.DEFAULT_SPAN_ID);
            }
            carrier.setSpanId(NumberUtils.toInt(spanId, ContextCarrier.DEFAULT_SPAN_ID));
        }

    }

    private void fillSpanTag(HttpServletRequest request, HttpServletResponse response, TraceSpan span) {
        Map<String, Object> parameter = getParameter(request);
        Map<String, String> tags = span.getTags();
        if (StringUtils.isEmpty(tags.get(NounConstant.URI))) {
            span.addTag(NounConstant.URI, request.getRequestURI());
        }
        if (StringUtils.isEmpty(tags.get(NounConstant.REQUEST))) {
            span.addTag(NounConstant.REQUEST, getRequest(request));
        }
        if (StringUtils.isEmpty(tags.get(NounConstant.CID))) {
            span.addTag(NounConstant.CID, parameter.getOrDefault(NounConstant.CID, NumberUtils.LONG_ZERO).toString());
        }
        if (StringUtils.isEmpty(tags.get(NounConstant.RESPONSE))) {
            span.addTag(NounConstant.RESPONSE, getResponse(response));
        }
    }

}
