package com.mimu.common.log.test.api;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class AddParam implements Serializable {
    /**
     * 两个数 算术第一个
     */
    private Integer first;
    /**
     * 两个数 算术第二个
     */
    private Integer second;
    /**
     * 描述
     */
    private String description;
}
