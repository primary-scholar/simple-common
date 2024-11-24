package com.mimu.common.log.http;

import org.junit.Before;
import org.junit.Test;


public class CommonHttpClientTest {

    private CommonHttpClient client;

    @Before
    public void initClient() {
        PoolingHttpConnectionManagerConfig poolingHttpConnectionManagerConfig =
                new PoolingHttpConnectionManagerConfig();
        client = new CommonHttpClient(poolingHttpConnectionManagerConfig);
    }

    @Test
    public void getTest() {
        String s = client.get("http://localhost:8081/api/local/num/add?first=2&second=3&description=加法");
        System.out.println(s);
    }

    @Test
    public void postTest() {
        String post = client.post("http://localhost:8081/api/local/num/multi", "{\"first\":\"2\",\"second\":3," +
                "\"description\":\"成法\"}");
        System.out.println(post);
    }

}