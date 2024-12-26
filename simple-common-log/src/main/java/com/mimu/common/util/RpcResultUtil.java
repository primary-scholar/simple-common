package com.mimu.common.util;

public class RpcResultUtil {

    public static <T> RpcResult<T> success(T data) {
        RpcResult<T> rpcResult = new RpcResult<>();
        rpcResult.setCode(200);
        rpcResult.setMessage("success");
        rpcResult.setData(data);
        return rpcResult;
    }

    public static <T> RpcResult<T> fail() {
        RpcResult<T> rpcResult = new RpcResult<>();
        rpcResult.setCode(400);
        rpcResult.setMessage("fail");
        rpcResult.setData(null);
        return rpcResult;
    }
}
