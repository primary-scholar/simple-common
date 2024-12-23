package com.mimu.common.log.http;

import com.alibaba.fastjson.JSONObject;
import com.mimu.common.constants.HttpNounConstant;
import com.mimu.common.constants.NounConstant;
import com.mimu.common.trace.context.TraceContextCarrier;
import com.mimu.common.trace.context.TraceContextManager;
import com.mimu.common.trace.span.TraceSpan;
import com.mimu.common.util.RequestParamResolver;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class CommonHttpClient {
    private static final Logger IO = LoggerFactory.getLogger("IO");


    private PoolingHttpClientConnectionManager connectionManager;
    private CloseableHttpClient httpClient;

    public CommonHttpClient(PoolingHttpConnectionManagerConfig connectionManagerConfig) {
        this.connectionManager = initPoolingConnectionManager(connectionManagerConfig);
        this.httpClient =
                HttpClients.custom().evictExpiredConnections().evictIdleConnections(connectionManager.getValidateAfterInactivity(), TimeUnit.MILLISECONDS).setConnectionManager(connectionManager).build();
    }

    public PoolingHttpClientConnectionManager initPoolingConnectionManager(PoolingHttpConnectionManagerConfig builder) {
        PoolingHttpClientConnectionManager clientConnectionManager = new PoolingHttpClientConnectionManager();
        clientConnectionManager.setMaxTotal(builder.getMaxTotal());
        clientConnectionManager.setDefaultMaxPerRoute(builder.getMaxPerRoute());
        clientConnectionManager.setValidateAfterInactivity(builder.getValidConnectionInterval());
        return clientConnectionManager;
    }

    public String get(String url) {
        try {
            return requestByGet(url);
        } catch (IOException e) {
            IO.error("", e);
        }
        return StringUtils.EMPTY;
    }

    public String post(String url, String jsonParam) {
        try {
            return requestByPost(url, jsonParam);
        } catch (IOException e) {
            IO.error("", e);
        }
        return StringUtils.EMPTY;
    }

    private String requestByGet(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        HttpEntity entity = null;
        String result = StringUtils.EMPTY;
        try {
            TraceContextCarrier traceContextCarrier = new TraceContextCarrier();
            TraceSpan exitSpan = TraceContextManager.createExitSpan(StringUtils.EMPTY, traceContextCarrier,
                    StringUtils.EMPTY);
            extractCarrier(httpGet, traceContextCarrier);
            fillSpanTag(httpGet, exitSpan);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            entity = response.getEntity();
            if (Objects.nonNull(entity)) {
                result = EntityUtils.toString(entity);
            }
            TraceSpan traceSpan = TraceContextManager.activeSpan();
            fillSpanTag(httpGet, result, traceSpan);
            TraceContextManager.stopSpan();
        } catch (IOException e) {
            IO.error("", e);
        } finally {
            EntityUtils.consume(entity);
        }
        return result;
    }

    private String requestByPost(String url, String query) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        String result = StringUtils.EMPTY;
        HttpEntity entity = null;
        try {
            TraceContextCarrier traceContextCarrier = new TraceContextCarrier();
            TraceSpan exitSpan = TraceContextManager.createExitSpan(StringUtils.EMPTY, traceContextCarrier,
                    StringUtils.EMPTY);
            extractCarrier(httpPost, traceContextCarrier);
            fillSpanTag(httpPost, query, exitSpan);
            StringEntity param = new StringEntity(query, StandardCharsets.UTF_8);
            param.setContentType("application/json");
            httpPost.setEntity(param);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            entity = response.getEntity();
            if (Objects.nonNull(entity)) {
                result = EntityUtils.toString(entity);
            }
            TraceSpan traceSpan = TraceContextManager.activeSpan();
            fillSpanTag(httpPost, query, traceSpan, result);
            TraceContextManager.stopSpan();
        } catch (Exception e) {
            IO.error("", e);
        } finally {
            EntityUtils.consume(entity);
        }
        return result;
    }

    @Setter
    @Getter
    public static class RequestConfigBuilder {
        private Integer connectionTimeOut = 3000;
        private Integer connectionRequestTimeout = 3000;
        private Integer socketTimeout = 3000;
    }

    private void extractCarrier(HttpGet httpGet, TraceContextCarrier carrier) {
        Map<String, String> tags = carrier.tags();
        for (Map.Entry<String, String> next : tags.entrySet()) {
            httpGet.setHeader(next.getKey(), next.getValue());
        }
    }

    private void extractCarrier(HttpPost httpPost, TraceContextCarrier carrier) {
        Map<String, String> tags = carrier.tags();
        for (Map.Entry<String, String> next : tags.entrySet()) {
            httpPost.setHeader(next.getKey(), next.getValue());
        }
    }

    private String getRequest(String uri) {
        String param = StringUtils.EMPTY;
        try {
            param = URLDecoder.decode(uri, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            IO.error("", e);
        }
        return param;
    }

    private String getRequestPath(String uri) {
        String path = StringUtils.EMPTY;
        String request = getRequest(uri);
        int httpSlash = request.indexOf(HttpNounConstant.HTTP_SLASH);
        if (httpSlash > NumberUtils.INTEGER_MINUS_ONE) {
            path = request.substring(httpSlash + HttpNounConstant.HTTP_SLASH.length());
        }
        int slashIndex = path.indexOf(HttpNounConstant.SLASH);
        if (slashIndex > NumberUtils.INTEGER_MINUS_ONE) {
            path = path.substring(slashIndex + HttpNounConstant.SLASH.length());
        }
        return path;
    }

    private String getRequestQuery(String uri) {
        String query = StringUtils.EMPTY;
        String request = getRequest(uri);
        int questionIndex = request.indexOf(HttpNounConstant.QUESTION);
        if (questionIndex > NumberUtils.INTEGER_MINUS_ONE) {
            query = request.substring(questionIndex + HttpNounConstant.HTTP_SLASH.length());
        }
        return query;
    }

    private void fillSpanTag(HttpGet httpGet, TraceSpan span) {
        Map<String, String> tags = span.getTags();
        String string = httpGet.getURI().toString();
        if (StringUtils.isEmpty(tags.get(NounConstant.URI))) {
            span.addTag(NounConstant.URI, getRequestPath(string));
        }
        if (StringUtils.isEmpty(tags.get(NounConstant.REQUEST))) {
            span.addTag(NounConstant.REQUEST, getRequestQuery(string));
        }
        String tagCid = tags.get(NounConstant.CID);
        if (StringUtils.isEmpty(tagCid) || NumberUtils.toLong(tagCid) <= NumberUtils.LONG_ZERO) {
            Map<String, Object> params = RequestParamResolver.decodeParams(getRequest(httpGet.getURI().toString()));
            span.addTag(NounConstant.CID, params.getOrDefault(NounConstant.CID, NumberUtils.LONG_ZERO).toString());
        }
    }

    private void fillSpanTag(HttpGet httpGet, String result, TraceSpan span) {
        Map<String, String> tags = span.getTags();
        fillSpanTag(httpGet, span);
        if (StringUtils.isEmpty(tags.get(NounConstant.RESPONSE))) {
            span.addTag(NounConstant.RESPONSE, result);
        }
    }

    private void fillSpanTag(HttpPost httpPost, String request, TraceSpan span) {
        Map<String, String> tags = span.getTags();
        String uri = httpPost.getURI().toString();
        if (StringUtils.isEmpty(tags.get(NounConstant.URI))) {
            span.addTag(NounConstant.URI, getRequestPath(uri));
        }
        if (StringUtils.isEmpty(tags.get(NounConstant.REQUEST))) {
            span.addTag(NounConstant.REQUEST, request);
        }
        String tagCid = tags.get(NounConstant.CID);
        if (StringUtils.isEmpty(tagCid) || NumberUtils.toLong(tagCid) <= NumberUtils.LONG_ZERO) {
            JSONObject parsed = JSONObject.parseObject(request);
            RequestParamResolver.fillCidParam(parsed);
            span.addTag(NounConstant.CID, parsed.getOrDefault(NounConstant.CID, NumberUtils.LONG_ZERO).toString());
        }
    }

    private void fillSpanTag(HttpPost httpPost, String request, TraceSpan span, String response) {
        Map<String, String> tags = span.getTags();
        fillSpanTag(httpPost, request, span);
        if (StringUtils.isEmpty(tags.get(NounConstant.RESPONSE))) {
            span.addTag(NounConstant.RESPONSE, response);
        }
    }

}
