package com.mimu.common.trace.id;

import lombok.Getter;

@Getter
public class DistributedId {
    private final String id;

    public DistributedId() {
        this.id = GlobalIdGenerator.generate();
    }

    public DistributedId(String id) {
        this.id = id;
    }
}
