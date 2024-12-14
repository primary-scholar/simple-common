package com.mimu.common.trace.context;

public class CarrierItem {
    private String key;
    private String value;

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
        TRACE_ID_KEY("_TRACE_ID_");

        private String itemKey;

        ItemEnum(String key) {
            this.itemKey = key;
        }

        public String getItemKey() {
            return itemKey;
        }
    }
}
