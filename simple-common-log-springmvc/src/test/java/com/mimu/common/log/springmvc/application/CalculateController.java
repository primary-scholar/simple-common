package com.mimu.common.log.springmvc.application;


import com.alibaba.fastjson.JSONObject;
import com.mimu.common.log.springmvc.api.AddParam;
import com.mimu.common.log.springmvc.api.CalculateResult;
import com.mimu.common.log.springmvc.api.RpcResult;
import com.mimu.common.log.springmvc.api.RpcResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculateController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculateController.class);

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

}
