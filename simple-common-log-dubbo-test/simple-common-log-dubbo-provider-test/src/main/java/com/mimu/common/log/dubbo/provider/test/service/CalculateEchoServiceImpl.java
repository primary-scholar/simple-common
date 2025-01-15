package com.mimu.common.log.dubbo.provider.test.service;

import com.mimu.common.log.dubbo.test.api.CalculateEchoService;
import com.mimu.common.log.dubbo.test.api.vo.CalculateEchoRequest;
import com.mimu.common.log.dubbo.test.api.vo.CalculateEchoResponse;
import com.mimu.common.util.RpcResult;
import com.mimu.common.util.RpcResultUtil;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(interfaceClass = CalculateEchoService.class)
public class CalculateEchoServiceImpl implements CalculateEchoService {
    @Override
    public RpcResult<CalculateEchoResponse> echo(CalculateEchoRequest request) {
        CalculateEchoResponse response = new CalculateEchoResponse();
        response.setResult(response.getResult());
        response.setMessage("receive your request");
        return RpcResultUtil.success(response);
    }
}
