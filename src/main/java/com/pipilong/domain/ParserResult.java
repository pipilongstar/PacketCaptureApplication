package com.pipilong.domain;

import com.pipilong.enums.ProtocolType;
import lombok.Data;

/**
 * @author pipilong
 * @createTime 2023/5/4
 * @description
 */
@Data
public class ParserResult {

    boolean isSuccessful;
    ProtocolType nextProtocol;
    int dataLength;
    public ParserResult(){}
    public ParserResult(boolean isSuccessful, ProtocolType nextProtocol, int dataLength) {
        this.isSuccessful = isSuccessful;
        this.nextProtocol = nextProtocol;
        this.dataLength = dataLength;
    }
}



































