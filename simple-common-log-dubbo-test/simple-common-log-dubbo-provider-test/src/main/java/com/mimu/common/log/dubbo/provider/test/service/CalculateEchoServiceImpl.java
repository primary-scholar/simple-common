package com.mimu.common.log.dubbo.provider.test.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mimu.common.log.dubbo.test.api.CalculateEchoService;
import com.mimu.common.log.dubbo.test.api.vo.CalculateEchoRequest;
import com.mimu.common.log.dubbo.test.api.vo.CalculateEchoResponse;
import com.mimu.common.util.RpcResult;
import com.mimu.common.util.RpcResultUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DubboService
public class CalculateEchoServiceImpl implements CalculateEchoService {
    private Logger logger = LoggerFactory.getLogger(CalculateEchoServiceImpl.class);

    @Override
    public RpcResult<CalculateEchoResponse> echo(CalculateEchoRequest request) {
        logger.info("echo request: {}", request);
        CalculateEchoResponse response = new CalculateEchoResponse();
        response.setResult(request.getResult());
        response.setMessage("receive your request");
        return RpcResultUtil.success(response);
    }

    @Override
    public RpcResult<CalculateEchoResponse> echoWithUglyParam(Integer integer, String string, JSONObject jsonObject, JSONArray jsonArray) {
        logger.info("echoWithUglyParam request: {},{},{},{}", integer, string, jsonObject, jsonArray);
        CalculateEchoResponse response = new CalculateEchoResponse();
        response.setResult(integer);
        response.setMessage(string);
        return RpcResultUtil.success(response);
    }
}
