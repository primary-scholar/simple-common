package com.mimu.common.log.test.application.controller;

import com.alibaba.fastjson.JSONObject;

import com.mimu.common.log.test.api.param.NumberCalculateParam;
import com.mimu.common.log.test.api.param.NumberOperationParam;
import com.mimu.common.log.test.api.result.NumberCalculateResult;
import com.mimu.common.log.test.api.result.NumberSeedResult;
import com.mimu.common.log.test.api.result.RpcResult;
import com.mimu.common.log.test.api.result.RpcResultUtil;
import com.mimu.common.log.test.application.service.CalculateRpcLocalHttpService;
import com.mimu.common.log.test.application.utils.NumberOperationBuilder;
import com.mimu.common.trace.context.ContextManager;
import com.mimu.common.trace.context.TraceContextSnapshot;
import com.mimu.common.wrapper.RunnableWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

@RestController
public class CalculateRpcLocalController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculateRpcLocalController.class);
    private static final Integer waitSeconds = 3000;

    @Autowired
    private ExecutorService executorService;
    @Autowired
    private CalculateRpcLocalHttpService operationHttpService;

    @RequestMapping(value = "/api/num/rpc/local/cal/get", method = RequestMethod.GET)
    public RpcResult calculateNumber(NumberOperationParam param) {
        LOGGER.info("get param:{}", JSONObject.toJSONString(param));
        NumberSeedResult first = operationHttpService.numberSeedGet(param);
        NumberSeedResult second = JSONObject.parseObject(JSONObject.toJSONString(first), NumberSeedResult.class);
        NumberCalculateParam operationParam = NumberOperationBuilder.build(first, second, param);
        NumberCalculateResult operationResult = operationHttpService.numberOperationPost(operationParam);
        return Objects.isNull(operationResult) ? RpcResultUtil.fail() : RpcResultUtil.success(operationResult);
    }

    @RequestMapping(value = "/api/num/rpc/local/cal/get/thread", method = RequestMethod.GET)
    public RpcResult calculateNumberThread(NumberOperationParam param) {
        LOGGER.info("get param:{}", JSONObject.toJSONString(param));
        CountDownLatch stepF = new CountDownLatch(1);
        AtomicReference<NumberSeedResult> firstRef = new AtomicReference<>();
        new Thread(() -> {
            firstRef.set(operationHttpService.numberSeedGet(param));
            stepF.countDown();
        }).start();
        try {
            stepF.await();
        } catch (InterruptedException e) {
            LOGGER.error("", e);
        }
        NumberSeedResult first = firstRef.get();
        CountDownLatch stepS = new CountDownLatch(1);
        NumberSeedResult second = JSONObject.parseObject(JSONObject.toJSONString(first), NumberSeedResult.class);
        NumberCalculateParam operationParam = NumberOperationBuilder.build(first, second, param);
        AtomicReference<NumberCalculateResult> thirdRef = new AtomicReference<>();
        new Thread(() -> {
            thirdRef.set(operationHttpService.numberOperationPost(operationParam));
            stepS.countDown();
        }).start();
        try {
            stepS.await();
        } catch (InterruptedException e) {
            LOGGER.error("", e);
        }
        NumberCalculateResult operationResult = thirdRef.get();
        return Objects.isNull(operationResult) ? RpcResultUtil.fail() : RpcResultUtil.success(operationResult);
    }

    @RequestMapping(value = "/api/num/rpc/local/cal/get/thread/wrapper", method = RequestMethod.GET)
    public RpcResult calculateNumberThreadWrapper(NumberOperationParam param) {
        LOGGER.info("get param:{}", JSONObject.toJSONString(param));
        CountDownLatch stepF = new CountDownLatch(1);
        AtomicReference<NumberSeedResult> firstRef = new AtomicReference<>();
        TraceContextSnapshot snapshot = ContextManager.capture();
        new Thread(new RunnableWrapper(() -> {
            firstRef.set(operationHttpService.numberSeedGet(param));
            stepF.countDown();
        }, snapshot)).start();
        try {
            stepF.await();
        } catch (InterruptedException e) {
            LOGGER.error("", e);
        }
        NumberSeedResult first = firstRef.get();
        CountDownLatch stepS = new CountDownLatch(1);
        NumberSeedResult second = JSONObject.parseObject(JSONObject.toJSONString(first), NumberSeedResult.class);
        NumberCalculateParam operationParam = NumberOperationBuilder.build(first, second, param);
        AtomicReference<NumberCalculateResult> thirdRef = new AtomicReference<>();
        new Thread(new RunnableWrapper(() -> {
            thirdRef.set(operationHttpService.numberOperationPost(operationParam));
            stepS.countDown();
        }, snapshot)).start();
        try {
            stepS.await();
        } catch (InterruptedException e) {
            LOGGER.error("", e);
        }
        NumberCalculateResult operationResult = thirdRef.get();
        return Objects.isNull(operationResult) ? RpcResultUtil.fail() : RpcResultUtil.success(operationResult);
    }

    @RequestMapping(value = "/api/num/rpc/local/cal/get/execute", method = RequestMethod.GET)
    public RpcResult calculateNumberExecute(NumberOperationParam param) {
        LOGGER.info("get param:{}", JSONObject.toJSONString(param));
        Future<NumberSeedResult> resultFuture = executorService.submit(() -> operationHttpService.numberSeedGet(param));
        NumberSeedResult first = null;
        try {
            first = resultFuture.get(waitSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOGGER.error("", e);
        }
        NumberSeedResult second = JSONObject.parseObject(JSONObject.toJSONString(first), NumberSeedResult.class);
        NumberCalculateParam operationParam = NumberOperationBuilder.build(first, second, param);
        NumberCalculateResult operationResult = operationHttpService.numberOperationPost(operationParam);
        return Objects.isNull(operationResult) ? RpcResultUtil.fail() : RpcResultUtil.success(operationResult);
    }


}
