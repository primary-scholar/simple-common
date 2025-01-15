package com.mimu.common.log.dubbo.consumer.test.service;

import com.mimu.common.log.dubbo.test.api.CalculateEchoService;
import com.mimu.common.log.dubbo.test.api.vo.CalculateEchoRequest;
import com.mimu.common.log.dubbo.test.api.vo.CalculateEchoResponse;
import com.mimu.common.util.RpcResult;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
public class CalculateConsumerService {

    @DubboReference
    private CalculateEchoService calculateEchoService;

    public CalculateEchoResponse echoResponse() {
        CalculateEchoRequest request = new CalculateEchoRequest();
        request.setResult(5);
        RpcResult<CalculateEchoResponse> result = calculateEchoService.echo(request);
        CalculateEchoResponse data = result.getData();
        return data;
    }
}
