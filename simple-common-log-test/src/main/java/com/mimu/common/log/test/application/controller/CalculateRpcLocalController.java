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
import com.mimu.common.wrapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

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
        Future<NumberSeedResult> seedResultFuture = executorService.submit(() -> operationHttpService.numberSeedGet(param));
        NumberSeedResult first = null;
        try {
            first = seedResultFuture.get(waitSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOGGER.error("", e);
        }
        NumberSeedResult second = JSONObject.parseObject(JSONObject.toJSONString(first), NumberSeedResult.class);
        NumberCalculateParam operationParam = NumberOperationBuilder.build(first, second, param);
        Future<NumberCalculateResult> calculateResultFuture = executorService.submit(() -> operationHttpService.numberOperationPost(operationParam));
        NumberCalculateResult operationResult = null;
        try {
            operationResult = calculateResultFuture.get(waitSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOGGER.error("", e);
        }
        return Objects.isNull(operationResult) ? RpcResultUtil.fail() : RpcResultUtil.success(operationResult);
    }

    @RequestMapping(value = "/api/num/rpc/local/cal/get/execute/wrapper", method = RequestMethod.GET)
    public RpcResult calculateNumberExecuteWrapper(NumberOperationParam param) {
        LOGGER.info("get param:{}", JSONObject.toJSONString(param));
        TraceContextSnapshot snapshot = ContextManager.capture();
        Future<NumberSeedResult> seedResultFuture = executorService.submit(new CallableWrapper<>(() -> operationHttpService.numberSeedGet(param), snapshot));
        NumberSeedResult first = null;
        try {
            first = seedResultFuture.get(waitSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOGGER.error("", e);
        }
        NumberSeedResult second = JSONObject.parseObject(JSONObject.toJSONString(first), NumberSeedResult.class);
        NumberCalculateParam operationParam = NumberOperationBuilder.build(first, second, param);
        Future<NumberCalculateResult> calculateResultFuture = executorService.submit(new CallableWrapper<>(() -> operationHttpService.numberOperationPost(operationParam), snapshot));
        NumberCalculateResult operationResult = null;
        try {
            operationResult = calculateResultFuture.get(waitSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOGGER.error("", e);
        }
        return Objects.isNull(operationResult) ? RpcResultUtil.fail() : RpcResultUtil.success(operationResult);
    }

    @RequestMapping(value = "/api/num/rpc/local/cal/get/completefuture", method = RequestMethod.GET)
    public RpcResult calculateNumberCompleteFuture(NumberOperationParam param) {
        LOGGER.info("get param:{}", JSONObject.toJSONString(param));
        AtomicReference<NumberSeedResult> firstRef = new AtomicReference<>();
        CompletableFuture.supplyAsync(() -> operationHttpService.numberSeedGet(param)).thenAccept(numberSeedResult -> firstRef.set(numberSeedResult)).join();
        NumberSeedResult first = firstRef.get();
        NumberSeedResult second = JSONObject.parseObject(JSONObject.toJSONString(first), NumberSeedResult.class);
        NumberCalculateParam operationParam = NumberOperationBuilder.build(first, second, param);
        AtomicReference<NumberCalculateResult> thirdRef = new AtomicReference<>();
        RpcResult result = CompletableFuture.supplyAsync(() -> operationHttpService.numberOperationPost(operationParam)).thenAcceptAsync(numberCalculateResult -> thirdRef.set(numberCalculateResult)).thenApply((Function<Void, RpcResult>) unused -> Objects.isNull(thirdRef.get()) ? RpcResultUtil.fail() : RpcResultUtil.success(thirdRef.get())).join();
        return result;
    }


    @RequestMapping(value = "/api/num/rpc/local/cal/get/completefuture/wrapper", method = RequestMethod.GET)
    public RpcResult calculateNumberCompleteFutureWrapper(NumberOperationParam param) {
        LOGGER.info("get param:{}", JSONObject.toJSONString(param));
        AtomicReference<NumberSeedResult> firstRef = new AtomicReference<>();
        TraceContextSnapshot snapshot = ContextManager.capture();
        CompletableFuture.supplyAsync(new SupplierWrapper<>(() -> operationHttpService.numberSeedGet(param), snapshot)).thenAcceptAsync(new ConsumerWrapper<>(numberSeedResult -> firstRef.set(numberSeedResult), snapshot)).join();
        NumberSeedResult first = firstRef.get();
        NumberSeedResult second = JSONObject.parseObject(JSONObject.toJSONString(first), NumberSeedResult.class);
        NumberCalculateParam operationParam = NumberOperationBuilder.build(first, second, param);
        AtomicReference<NumberCalculateResult> thirdRef = new AtomicReference<>();
        RpcResult result = CompletableFuture.supplyAsync(new SupplierWrapper<>(() -> operationHttpService.numberOperationPost(operationParam), snapshot)).thenAcceptAsync(new ConsumerWrapper<>(numberCalculateResult -> thirdRef.set(numberCalculateResult), snapshot)).thenApply(new FunctionWrapper<>((Function<Void, RpcResult>) unused -> Objects.isNull(thirdRef.get()) ? RpcResultUtil.fail() : RpcResultUtil.success(thirdRef.get()), snapshot)).join();
        return result;
    }


}
