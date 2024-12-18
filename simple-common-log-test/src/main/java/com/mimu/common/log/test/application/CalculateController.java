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
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

@RestController
public class CalculateController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculateController.class);
    private static final Integer waitSeconds = 10000;

    @Autowired
    private RpcCalculateByHttpService rpcCalculateByHttpService;
    @Autowired
    private ExecutorService executorService;

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

    @RequestMapping(value = "/api/cal/remote/num/add/thread", method = RequestMethod.GET)
    public RpcResult getRemoteMethodNewThread(AddParam param) {
        LOGGER.info("get param:{}", JSONObject.toJSONString(param));
        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicReference<CalculateResult> atomicReference = new AtomicReference<>();
        Thread thread = new Thread(() -> {
            atomicReference.set(rpcCalculateByHttpService.rpcGetResult(param));
            countDownLatch.countDown();
        });
        thread.start();
        try {
            countDownLatch.await(waitSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }
        CalculateResult calculateResult = atomicReference.get();
        return Objects.isNull(calculateResult) ? RpcResultUtil.fail() : RpcResultUtil.success(calculateResult);
    }

    @RequestMapping(value = "/api/cal/remote/num/add/execute", method = RequestMethod.GET)
    public RpcResult getRemoteMethodExecute(AddParam param) {
        LOGGER.info("get param:{}", JSONObject.toJSONString(param));
        CalculateResult calculateResult = null;
        Future<CalculateResult> submit = executorService.submit(() -> rpcCalculateByHttpService.rpcGetResult(param));
        try {
            calculateResult = submit.get(waitSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
        }
        return Objects.isNull(calculateResult) ? RpcResultUtil.fail() : RpcResultUtil.success(calculateResult);
    }

    @RequestMapping(value = "/api/cal/remote/num/add/completefuture", method = RequestMethod.GET)
    public RpcResult getRemoteMethodCompleteFuture(AddParam param) {
        LOGGER.info("get param:{}", JSONObject.toJSONString(param));
        CalculateResult calculateResult = null;
        CompletableFuture<CalculateResult> completableFuture =
                CompletableFuture.supplyAsync(() -> rpcCalculateByHttpService.rpcGetResult(param), executorService).thenApply(calculateResult1 -> calculateResult1);
        try {
            calculateResult = completableFuture.get(waitSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
        }
        return Objects.isNull(calculateResult) ? RpcResultUtil.fail() : RpcResultUtil.success(calculateResult);
    }

}
