package com.pipilong.util;

import com.esotericsoftware.kryo.Kryo;
import org.springframework.stereotype.Component;

/**
 * @author pipilong
 * @createTime 2023/5/11
 * @description
 */
@Component
public class KryoSerializer {

    private Kryo kryo;

    public KryoSerializer(){
        kryo = new Kryo();
//        kryo.register();
    }

}
