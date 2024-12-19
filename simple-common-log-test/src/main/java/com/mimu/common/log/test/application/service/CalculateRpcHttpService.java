package com.mimu.common.log.test.application.service;

import com.mimu.common.log.http.CommonHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalculateRpcHttpService extends AbstractCalculateService {
    private static final String RPC_GET_URL = "http://localhost:8080/api/num/gen?number=%s&p1=%s";
    private static final String RPC_POST_URL = "http://localhost:8080/api/num/cal";

    @Autowired
    private CommonHttpClient commonHttpClient;

    @Override
    public CommonHttpClient getClient() {
        return commonHttpClient;
    }

    @Override
    public String getRpcGetUrl() {
        return RPC_GET_URL;
    }

    @Override
    public String getRpcPostUrl() {
        return RPC_POST_URL;
    }
}
