package com.mimu.common.trace.span;

public interface AbstractSpan {

    /**
     * 是否是 entrySpan 节点
     *
     * @return
     */
    Boolean isEntry();

    /**
     * 是否是 exitSpan 节点
     *
     * @return
     */
    Boolean isExit();
}
