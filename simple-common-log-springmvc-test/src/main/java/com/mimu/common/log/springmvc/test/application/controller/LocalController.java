package com.mimu.common.log.springmvc.test.application.controller;

import com.alibaba.fastjson.JSONObject;
import com.mimu.common.log.springmvc.test.api.param.NumberCalculateParam;
import com.mimu.common.log.springmvc.test.api.param.NumberOperationParam;
import com.mimu.common.log.springmvc.test.api.result.NumberCalculateResult;
import com.mimu.common.log.springmvc.test.api.result.NumberSeedResult;
import com.mimu.common.log.springmvc.test.api.result.RpcResult;
import com.mimu.common.log.springmvc.test.api.result.RpcResultUtil;
import com.mimu.common.log.springmvc.test.application.utils.NumberOperationBuilder;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LocalController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalController.class);

    @RequestMapping(value = "/api/num/gen", method = RequestMethod.GET)
    public RpcResult genNumberWithGet(NumberOperationParam param) {
        LOGGER.info("get param:{}", JSONObject.toJSONString(param));
        param.setNumber(param.getNumber() + NumberUtils.INTEGER_ONE);
        NumberSeedResult build = NumberOperationBuilder.build(param);
        return RpcResultUtil.success(build);
    }

    @RequestMapping(value = "/api/num/cal", method = RequestMethod.POST)
    public RpcResult calculateNumberInMvcLocal(@RequestBody NumberCalculateParam param) {
        LOGGER.info("get param:{}", JSONObject.toJSONString(param));
        NumberCalculateResult build = NumberOperationBuilder.build(param);
        return RpcResultUtil.success(build);
    }
}
