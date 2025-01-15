package com.mimu.common.log.dubbo.test.api;

import com.mimu.common.log.dubbo.test.api.vo.CalculateEchoRequest;
import com.mimu.common.log.dubbo.test.api.vo.CalculateEchoResponse;
import com.mimu.common.util.RpcResult;

public interface CalculateEchoService {
    RpcResult<CalculateEchoResponse> echo(CalculateEchoRequest request);
}
