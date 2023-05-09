package com.pipilong.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author pipilong
 * @createTime 2023/5/9
 * @description
 */
@Data
@Component
public class User implements Serializable {

    private String name;
    private String email;

}
