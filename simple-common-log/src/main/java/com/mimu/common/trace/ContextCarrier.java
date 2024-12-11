package com.mimu.common.trace;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContextCarrier implements Serializable {
    private String traceId;
    private Integer spanId;
    private List<CarrierItem> itemList;

    public List<CarrierItem> items() {
        itemList = new ArrayList<>();
        CarrierItem.ItemEnum[] values = CarrierItem.ItemEnum.values();
        for (CarrierItem.ItemEnum value : values) {
            CarrierItem item = new CarrierItem();
            item.setKey(value.getItemKey());
            itemList.add(item);
        }
        return itemList;
    }


    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Integer getSpanId() {
        return spanId;
    }

    public void setSpanId(Integer spanId) {
        this.spanId = spanId;
    }

    public Boolean isValid() {
        return StringUtils.isNotBlank(traceId);
    }
}
