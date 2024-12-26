package com.mimu.common.log.dubbo;

import com.mimu.common.trace.context.TraceContextCarrier;
import com.mimu.common.trace.context.TraceContextManager;
import com.mimu.common.trace.span.TraceSpan;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

@Activate(group = CommonConstants.CONSUMER, order = 1)
public class ConsumerTraceFilter extends AbstractTraceFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        TraceContextCarrier traceContextCarrier = new TraceContextCarrier();
        TraceSpan exitSpan = TraceContextManager.createExitSpan(StringUtils.EMPTY, traceContextCarrier,
                StringUtils.EMPTY);
        extractCarrier(invocation, traceContextCarrier);
        fillSpanTag(invocation, exitSpan);
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
