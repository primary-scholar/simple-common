package com.mimu.common.log.test.application.controller;


import com.alibaba.fastjson.JSONObject;
import com.mimu.common.log.test.api.param.NumberCalculateParam;
import com.mimu.common.log.test.api.param.NumberOperationParam;
import com.mimu.common.log.test.api.result.NumberCalculateResult;
import com.mimu.common.log.test.api.result.NumberSeedResult;
import com.mimu.common.log.test.api.result.RpcResult;
import com.mimu.common.log.test.api.result.RpcResultUtil;
import com.mimu.common.log.test.application.service.CalculateRpcHttpService;
import com.mimu.common.log.test.application.utils.NumberOperationBuilder;
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
import java.util.function.Supplier;

@RestController
public class CalculateRpcController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculateRpcController.class);
    private static final Integer waitSeconds = 1000;

    @Autowired
    private ExecutorService executorService;
    @Autowired
    private CalculateRpcHttpService operationHttpService;

    @RequestMapping(value = "/api/num/rpc/cal/get", method = RequestMethod.GET)
    public RpcResult calculateWithGet(NumberOperationParam param) {
        LOGGER.info("get param:{}", JSONObject.toJSONString(param));
        NumberSeedResult first = operationHttpService.numberSeedGet(param);
        NumberSeedResult second = operationHttpService.numberSeedGet(param);
        NumberCalculateParam operationParam = NumberOperationBuilder.build(first, second, param);
        NumberCalculateResult operationResult = operationHttpService.numberOperationPost(operationParam);
        return Objects.isNull(operationResult) ? RpcResultUtil.fail() : RpcResultUtil.success(operationResult);
    }


    @RequestMapping(value = "/api/num/rpc/cal/post", method = RequestMethod.POST)
    public RpcResult calculateWithPostBody(@RequestBody NumberOperationParam param) {
        LOGGER.info("post param:{}", JSONObject.toJSONString(param));
        NumberSeedResult first = operationHttpService.numberSeedGet(param);
        NumberSeedResult second = operationHttpService.numberSeedGet(param);
        NumberCalculateParam operationParam = NumberOperationBuilder.build(first, second, param);
        NumberCalculateResult operationResult = operationHttpService.numberOperationPost(operationParam);
        return Objects.isNull(operationResult) ? RpcResultUtil.fail() : RpcResultUtil.success(operationResult);
    }


    @RequestMapping(value = "/api/num/rpc/cal/post/form", method = RequestMethod.POST)
    public RpcResult calculateWithPostForm(NumberOperationParam param) {
        LOGGER.info("post param:{}", JSONObject.toJSONString(param));
        NumberSeedResult first = operationHttpService.numberSeedGet(param);
        NumberSeedResult second = operationHttpService.numberSeedGet(param);
        NumberCalculateParam operationParam = NumberOperationBuilder.build(first, second, param);
        NumberCalculateResult operationResult = operationHttpService.numberOperationPost(operationParam);
        return Objects.isNull(operationResult) ? RpcResultUtil.fail() : RpcResultUtil.success(operationResult);
    }

    @RequestMapping(value = "/api/num/rpc/cal/get/thread", method = RequestMethod.GET)
    public RpcResult calculateNumberInMvcThread(NumberOperationParam param) {
        LOGGER.info("get param:{}", JSONObject.toJSONString(param));
        CountDownLatch stepF = new CountDownLatch(2);
        AtomicReference<NumberSeedResult> firstRef = new AtomicReference<>();
        AtomicReference<NumberSeedResult> secondRef = new AtomicReference<>();
        new Thread(() -> {
            firstRef.set(operationHttpService.numberSeedGet(param));
            stepF.countDown();
        }).start();
        new Thread(() -> {
            secondRef.set(operationHttpService.numberSeedGet(param));
            stepF.countDown();
        }).start();
        try {
            stepF.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        NumberSeedResult first = firstRef.get();
        NumberSeedResult second = secondRef.get();
        CountDownLatch stepS = new CountDownLatch(1);
        NumberCalculateParam operationParam = NumberOperationBuilder.build(first, second, param);
        AtomicReference<NumberCalculateResult> thirdRef = new AtomicReference<>();
        new Thread(() -> {
            thirdRef.set(operationHttpService.numberOperationPost(operationParam));
            stepS.countDown();
        }).start();
        try {
            stepS.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        NumberCalculateResult operationResult = thirdRef.get();
        return Objects.isNull(operationResult) ? RpcResultUtil.fail() : RpcResultUtil.success(operationResult);
    }


    @RequestMapping(value = "/api/num/rpc/cal/get/execute", method = RequestMethod.GET)
    public RpcResult calculateWithGetExecute(NumberOperationParam param) {
        LOGGER.info("get param:{}", JSONObject.toJSONString(param));
        NumberCalculateResult operationResult = null;
        Future<NumberCalculateResult> submit = executorService.submit(() -> null);
        try {
            operationResult = submit.get(waitSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
        }
        return Objects.isNull(operationResult) ? RpcResultUtil.fail() : RpcResultUtil.success(operationResult);
    }

    @RequestMapping(value = "/api/num/rpc/cal/get/completefuture", method = RequestMethod.GET)
    public RpcResult calculateWithGetCompletefuture(NumberOperationParam param) {
        LOGGER.info("get param:{}", JSONObject.toJSONString(param));
        NumberCalculateResult operationResult = null;
        AtomicReference<NumberCalculateResult> atomicReference = new AtomicReference<>();
        CompletableFuture<NumberCalculateResult> completableFuture =
                CompletableFuture.supplyAsync((Supplier<NumberCalculateResult>) () -> null).whenComplete((numberCalculateResult, throwable) -> atomicReference.set(numberCalculateResult));
        try {
            operationResult = completableFuture.get(waitSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
        }
        return Objects.isNull(operationResult) ? RpcResultUtil.fail() : RpcResultUtil.success(operationResult);
    }

}
