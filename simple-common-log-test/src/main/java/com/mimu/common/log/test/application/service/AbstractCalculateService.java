package com.mimu.common.log.test.application.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.mimu.common.log.http.CommonHttpClient;
import com.mimu.common.log.test.api.param.NumberCalculateParam;
import com.mimu.common.log.test.api.param.NumberOperationParam;
import com.mimu.common.log.test.api.result.NumberCalculateResult;
import com.mimu.common.log.test.api.result.NumberSeedResult;
import com.mimu.common.log.test.api.result.RpcResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCalculateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCalculateService.class);

    public abstract CommonHttpClient getClient();

    public abstract String getRpcGetUrl();

    public abstract String getRpcPostUrl();

    public NumberSeedResult numberSeedGet(NumberOperationParam param) {
        try {
            CommonHttpClient httpClient = getClient();
            String rpcResult = httpClient.get(String.format(getRpcGetUrl(), param.getNumber(), param.getP1()));
            RpcResult<NumberSeedResult> parsedObject = JSONObject.parseObject(rpcResult, new TypeReference<RpcResult<NumberSeedResult>>() {
            });
            return parsedObject.getData();
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
    }

    public NumberCalculateResult numberOperationPost(NumberCalculateParam param) {
        try {
            CommonHttpClient httpClient = getClient();
            String rpcResult = httpClient.post(getRpcPostUrl(), JSONObject.toJSONString(param));
            RpcResult<NumberCalculateResult> parsedObject = JSONObject.parseObject(rpcResult, new TypeReference<RpcResult<NumberCalculateResult>>() {
            });
            return parsedObject.getData();
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
    }

}
