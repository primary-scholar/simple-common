package com.mimu.common.log.dubbo;

import com.mimu.common.trace.context.TraceContextCarrier;
import com.mimu.common.trace.context.TraceContextManager;
import com.mimu.common.trace.span.TraceSpan;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;


@Activate(group = CommonConstants.PROVIDER, order = 1)
public class ProviderTraceFilter extends AbstractTraceFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        TraceContextCarrier traceContextCarrier = new TraceContextCarrier();
        TraceSpan entrySpan = TraceContextManager.createEntrySpan(StringUtils.EMPTY, traceContextCarrier);
        fillCarrier(invocation, traceContextCarrier);
        fillSpanTag(invocation, entrySpan);
        Result result = null;
        try {
            result = invoker.invoke(invocation);
            return result;
        } finally {
            TraceSpan traceSpan = TraceContextManager.activeSpan();
            fillSpanTag(invocation, traceSpan, result);
            TraceContextManager.stopSpan();
        }
    }


}
