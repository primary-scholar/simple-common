package com.mimu.common.log.dubbo.consumer.test.controller;

import com.alibaba.fastjson.JSONObject;
import com.mimu.common.log.dubbo.consumer.test.service.CalculateConsumerService;
import com.mimu.common.log.dubbo.test.api.vo.CalculateEchoRequest;
import com.mimu.common.log.dubbo.test.api.vo.CalculateEchoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculateController {
    private static final Logger logger = LoggerFactory.getLogger(CalculateController.class);

    @Autowired
    private CalculateConsumerService calculateConsumerService;

    @ResponseBody
    @RequestMapping(value = "/api/num/dubbo/echo/get", method = RequestMethod.GET)
    public CalculateEchoResponse echo(CalculateEchoRequest request) {
        logger.info("request:{}", JSONObject.toJSONString(request));
        CalculateEchoResponse response = calculateConsumerService.echoResponse();
        return response;
    }
}
