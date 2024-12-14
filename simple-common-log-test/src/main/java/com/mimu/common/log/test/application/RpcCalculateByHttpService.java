package com.mimu.common.log.test.application;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.mimu.common.log.http.CommonHttpClient;
import com.mimu.common.log.test.api.AddParam;
import com.mimu.common.log.test.api.CalculateResult;
import com.mimu.common.log.test.api.RpcResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RpcCalculateByHttpService {
    private static final String RPC_GET_URL = "http://localhost:8080/api/cal/num/add?first=%s&second=%s&description=%s";
    private static final String RPC_POST_URL = "http://localhost:8080/api/cal/num/multi";

    @Autowired
    private CommonHttpClient commonHttpClient;

    public CalculateResult rpcGetResult(AddParam param) {
        try {
            String rpcResult = commonHttpClient.get(String.format(RPC_GET_URL, param.getFirst(), param.getSecond(),
                    param.getDescription()));
            RpcResult<CalculateResult> parsedObject = JSONObject.parseObject(rpcResult,
                    new TypeReference<RpcResult<CalculateResult>>() {
                    });
            return parsedObject.getData();
        } catch (Exception e) {
            return null;
        }
    }

    public CalculateResult rpcPostResult(AddParam param) {
        try {
            String rpcResult = commonHttpClient.post(RPC_POST_URL, JSONObject.toJSONString(param));
            RpcResult<CalculateResult> parsedObject = JSONObject.parseObject(rpcResult,
                    new TypeReference<RpcResult<CalculateResult>>() {
                    });
            return parsedObject.getData();
        } catch (Exception e) {
            return null;
        }
    }
}
