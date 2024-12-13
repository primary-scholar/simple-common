package com.mimu.common.log.http;

import com.mimu.common.trace.ContextCarrier;
import com.mimu.common.trace.ContextManager;
import com.mimu.common.trace.span.TraceSpan;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
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
import java.nio.charset.Charset;
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
        }
        return StringUtils.EMPTY;
    }

    public String post(String url, String jsonParam) {
        try {
            return requestByPost(url, jsonParam);
        } catch (IOException e) {
        }
        return StringUtils.EMPTY;
    }

    private String requestByGet(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        HttpEntity entity = null;
        String result = StringUtils.EMPTY;
        try {
            ContextCarrier contextCarrier = new ContextCarrier();
            ContextManager.createExitSpan(StringUtils.EMPTY, contextCarrier, StringUtils.EMPTY);
            extractCarrier(httpGet, contextCarrier);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            entity = response.getEntity();
            if (Objects.nonNull(entity)) {
                result = EntityUtils.toString(entity);
            }
            TraceSpan traceSpan = ContextManager.activeSpan();
            ContextManager.stopSpan();
        } catch (IOException e) {
        } finally {
            EntityUtils.consume(entity);
        }
        return result;
    }

    private String requestByPost(String url, String content) throws IOException {
        IO.info("");
        HttpPost httpPost = new HttpPost(url);
        String result = StringUtils.EMPTY;
        HttpEntity entity = null;
        try {
            ContextCarrier contextCarrier = new ContextCarrier();
            ContextManager.createExitSpan(StringUtils.EMPTY, contextCarrier, StringUtils.EMPTY);
            extractCarrier(httpPost, contextCarrier);
            StringEntity param = new StringEntity(content, Charset.defaultCharset());
            param.setContentType("application/json");
            httpPost.setEntity(param);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            entity = response.getEntity();
            if (Objects.nonNull(entity)) {
                result = EntityUtils.toString(entity);
            }
            TraceSpan traceSpan = ContextManager.activeSpan();
            ContextManager.stopSpan();
        } catch (Exception e) {
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

    private void extractCarrier(HttpGet httpGet, ContextCarrier carrier) {
        Map<String, String> tags = carrier.tags();
        for (Map.Entry<String, String> next : tags.entrySet()) {
            httpGet.setHeader(next.getKey(), next.getValue());
        }
    }

    private void extractCarrier(HttpPost httpPost, ContextCarrier carrier) {
        Map<String, String> tags = carrier.tags();
        for (Map.Entry<String, String> next : tags.entrySet()) {
            httpPost.setHeader(next.getKey(), next.getValue());
        }
    }
}
