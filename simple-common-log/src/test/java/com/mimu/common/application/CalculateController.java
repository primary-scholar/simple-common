package com.mimu.common.application;


import com.alibaba.fastjson.JSONObject;
import com.mimu.common.api.AddParam;
import com.mimu.common.api.CalculateResult;
import com.mimu.common.api.RpcResult;
import com.mimu.common.api.RpcResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculateController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculateController.class);

    @RequestMapping(value = "/api/first/num/add", method = RequestMethod.GET)
    public RpcResult getMethod(AddParam param) {
        LOGGER.info("get param:{}", JSONObject.toJSONString(param));
        CalculateResult calculateResult = new CalculateResult(param.getFirst() + param.getSecond(),
                param.getDescription());
        return RpcResultUtil.success(calculateResult);
    }


    @RequestMapping(value = "/api/first/num/multi", method = RequestMethod.POST)
    public String postMethod(@RequestBody AddParam param) {
        LOGGER.info("post param:{}", JSONObject.toJSONString(param));
        CalculateResult calculateResult = new CalculateResult(param.getFirst() * param.getSecond(),
                param.getDescription());
        RpcResult<CalculateResult> success = RpcResultUtil.success(calculateResult);
        return JSONObject.toJSONString(success);
    }

}
