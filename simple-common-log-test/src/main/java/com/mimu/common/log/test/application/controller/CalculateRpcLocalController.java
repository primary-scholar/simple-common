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
import com.mimu.common.trace.context.TraceContextManager;
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
        CountDownLatch stepF = new CountDownLatch(2);
        AtomicReference<NumberSeedResult> firstRef = new AtomicReference<>();
        AtomicReference<NumberSeedResult> secondRef = new AtomicReference<>();
        TraceContextSnapshot snapshotF = TraceContextManager.capture();
        new Thread(new RunnableWrapper(() -> {
            firstRef.set(operationHttpService.numberSeedGet(param));
            stepF.countDown();
        }, snapshotF)).start();
        TraceContextSnapshot snapshotS = TraceContextManager.capture();
        new Thread(new RunnableWrapper(() -> {
            secondRef.set(operationHttpService.numberSeedGet(param));
            stepF.countDown();
        }, snapshotS)).start();
        try {
            stepF.await();
        } catch (InterruptedException e) {
            LOGGER.error("", e);
        }
        NumberSeedResult first = firstRef.get();
        NumberSeedResult second = secondRef.get();
        CountDownLatch stepT = new CountDownLatch(1);
        NumberCalculateParam operationParam = NumberOperationBuilder.build(first, second, param);
        AtomicReference<NumberCalculateResult> thirdRef = new AtomicReference<>();
        TraceContextSnapshot snapshotT = TraceContextManager.capture();
        new Thread(new RunnableWrapper(() -> {
            thirdRef.set(operationHttpService.numberOperationPost(operationParam));
            stepT.countDown();
        }, snapshotT)).start();
        try {
            stepT.await();
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
        NumberSeedResult first = null;
        NumberSeedResult second = null;
        TraceContextSnapshot snapshotF = TraceContextManager.capture();
        Future<NumberSeedResult> firstFuture = executorService.submit(new CallableWrapper<>(() -> operationHttpService.numberSeedGet(param), snapshotF));
        try {
            first = firstFuture.get(waitSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOGGER.error("", e);
        }
        TraceContextSnapshot snapshotS = TraceContextManager.capture();
        Future<NumberSeedResult> secondFuture = executorService.submit(new CallableWrapper<>(() -> operationHttpService.numberSeedGet(param), snapshotS));
        try {
            second = secondFuture.get(waitSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOGGER.error("", e);
        }
        NumberCalculateParam operationParam = NumberOperationBuilder.build(first, second, param);
        TraceContextSnapshot snapshotT = TraceContextManager.capture();
        Future<NumberCalculateResult> calculateResultFuture = executorService.submit(new CallableWrapper<>(() -> operationHttpService.numberOperationPost(operationParam), snapshotT));
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
        AtomicReference<NumberSeedResult> secondRef = new AtomicReference<>();
        TraceContextSnapshot snapshotF = TraceContextManager.capture();
        CompletableFuture.supplyAsync(new SupplierWrapper<>(() -> operationHttpService.numberSeedGet(param), snapshotF)).thenAcceptAsync(new ConsumerWrapper<>(numberSeedResult -> firstRef.set(numberSeedResult), snapshotF)).join();
        TraceContextSnapshot snapshotS = TraceContextManager.capture();
        CompletableFuture.supplyAsync(new SupplierWrapper<>(() -> operationHttpService.numberSeedGet(param), snapshotS)).thenAcceptAsync(new ConsumerWrapper<>(numberSeedResult -> secondRef.set(numberSeedResult), snapshotS)).join();
        NumberSeedResult first = firstRef.get();
        NumberSeedResult second = secondRef.get();
        NumberCalculateParam operationParam = NumberOperationBuilder.build(first, second, param);
        AtomicReference<NumberCalculateResult> thirdRef = new AtomicReference<>();
        TraceContextSnapshot snapshotT = TraceContextManager.capture();
        RpcResult result = CompletableFuture.supplyAsync(new SupplierWrapper<>(() -> operationHttpService.numberOperationPost(operationParam), snapshotT)).thenAcceptAsync(new ConsumerWrapper<>(numberCalculateResult -> thirdRef.set(numberCalculateResult), snapshotT)).thenApply(new FunctionWrapper<>((Function<Void, RpcResult>) unused -> Objects.isNull(thirdRef.get()) ? RpcResultUtil.fail() : RpcResultUtil.success(thirdRef.get()), snapshotT)).join();
        return result;
    }


}
