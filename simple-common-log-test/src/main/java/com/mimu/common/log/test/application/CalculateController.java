package com.mimu.common.log.test.application;


import com.alibaba.fastjson.JSONObject;
import com.mimu.common.log.test.api.AddParam;
import com.mimu.common.log.test.api.CalculateResult;
import com.mimu.common.log.test.api.RpcResult;
import com.mimu.common.log.test.api.RpcResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class CalculateController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculateController.class);

    @Autowired
    private RpcCalculateByHttpService rpcCalculateByHttpService;

    @RequestMapping(value = "/api/cal/num/add", method = RequestMethod.GET)
    public RpcResult getMethod(AddParam param) {
        LOGGER.info("get param:{}", JSONObject.toJSONString(param));
        CalculateResult calculateResult = new CalculateResult(param.getFirst() + param.getSecond(),
                param.getDescription());
        return RpcResultUtil.success(calculateResult);
    }


    @RequestMapping(value = "/api/cal/num/multi", method = RequestMethod.POST)
    public String postMethod(@RequestBody AddParam param) {
        LOGGER.info("post param:{}", JSONObject.toJSONString(param));
        CalculateResult calculateResult = new CalculateResult(param.getFirst() * param.getSecond(),
                param.getDescription());
        RpcResult<CalculateResult> success = RpcResultUtil.success(calculateResult);
        return JSONObject.toJSONString(success);
    }

    @RequestMapping(value = "/api/cal/num/multi/form", method = RequestMethod.POST)
    public String postMethodForm(AddParam param) {
        LOGGER.info("post param:{}", JSONObject.toJSONString(param));
        CalculateResult calculateResult = new CalculateResult(param.getFirst() * param.getSecond(),
                param.getDescription());
        RpcResult<CalculateResult> success = RpcResultUtil.success(calculateResult);
        return JSONObject.toJSONString(success);
    }

    @RequestMapping(value = "/api/cal/remote/num/add", method = RequestMethod.GET)
    public RpcResult getRemoteMethod(AddParam param) {
        LOGGER.info("get param:{}", JSONObject.toJSONString(param));
        CalculateResult calculateResult = rpcCalculateByHttpService.rpcGetResult(param);
        return Objects.isNull(calculateResult) ? RpcResultUtil.fail() : RpcResultUtil.success(calculateResult);
    }

    @RequestMapping(value = "/api/cal/remote/num/multi", method = RequestMethod.POST)
    public String postRemoteMethod(@RequestBody AddParam param) {
        LOGGER.info("post param:{}", JSONObject.toJSONString(param));
        CalculateResult calculateResult = rpcCalculateByHttpService.rpcPostResult(param);
        RpcResult<CalculateResult> result = Objects.isNull(calculateResult) ? RpcResultUtil.fail() :
                RpcResultUtil.success(calculateResult);
        return JSONObject.toJSONString(result);
    }

    @RequestMapping(value = "/api/cal/remote/num/multi/form", method = RequestMethod.POST)
    public String postRemoteMethodWithForm(AddParam param) {
        LOGGER.info("post param:{}", JSONObject.toJSONString(param));
        CalculateResult calculateResult = rpcCalculateByHttpService.rpcPostResult(param);
        RpcResult<CalculateResult> result = Objects.isNull(calculateResult) ? RpcResultUtil.fail() :
                RpcResultUtil.success(calculateResult);
        return JSONObject.toJSONString(result);
    }

}
