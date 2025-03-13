package com.mimu.common.log.dubbo.test.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mimu.common.log.dubbo.test.api.vo.CalculateEchoRequest;
import com.mimu.common.log.dubbo.test.api.vo.CalculateEchoResponse;
import com.mimu.common.util.RpcResult;

public interface CalculateEchoService {
    RpcResult<CalculateEchoResponse> echo(CalculateEchoRequest request);

    RpcResult<CalculateEchoResponse> echoWithUglyParam(Integer integer, String string, JSONObject jsonObject, JSONArray jsonArray);
}
