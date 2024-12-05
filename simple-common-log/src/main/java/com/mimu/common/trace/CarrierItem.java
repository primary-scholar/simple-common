package com.mimu.common.trace;

import java.util.*;

public class CarrierItem {
    private String key;
    private String value;
    private CarrierItem next;

    public CarrierItem(String key, String value) {
        this(key, value, null);
    }

    public CarrierItem(String key, String value, CarrierItem next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }

    public CarrierItem next() {
        return this.next;
    }

    public Boolean hasNext() {
        return Objects.nonNull(this.next);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    enum ItemEnum {
        CID_KEY("cid"), TRACE_ID_KEY("_TRACE_ID_"), URL("_URL_"),
        REQUEST_KEY("_REQUEST_"), RESPONSE_KEY("_RESPONSE_");

        private String itemKey;

        ItemEnum(String key) {
            this.itemKey = key;
        }

        public String getItemKey() {
            return itemKey;
        }
    }
}
