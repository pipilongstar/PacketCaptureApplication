package com.pipilong.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author pipilong
 * @createTime 2023/5/4
 * @description
 */
public class DateConvert {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String convert(long timestamp){
        return simpleDateFormat.format(new Date(timestamp));
    }

}
