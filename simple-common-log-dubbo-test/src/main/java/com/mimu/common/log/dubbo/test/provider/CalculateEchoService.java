package com.mimu.common.log.dubbo.test.provider;

import com.mimu.common.log.dubbo.test.api.CalculateEchoRequest;
import com.mimu.common.log.dubbo.test.api.CalculateEchoResponse;
import com.mimu.common.util.RpcResult;
import com.mimu.common.util.RpcResultUtil;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class CalculateEchoService {

    RpcResult<CalculateEchoResponse> echo(CalculateEchoRequest request) {
        CalculateEchoResponse response = new CalculateEchoResponse();
        response.setResult(request.getResult());
        response.setMessage("receive result message");
        return RpcResultUtil.success(response);
    }
}
