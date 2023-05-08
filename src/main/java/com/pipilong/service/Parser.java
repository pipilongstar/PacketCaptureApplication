package com.pipilong.service;

import com.pipilong.domain.ParserResult;
import com.pipilong.enums.ProtocolType;
import org.springframework.stereotype.Service;

/**
 * @author pipilong
 * @createTime 2023/5/4
 * @description
 */
@Service
public interface Parser {

    ParserResult parser(byte[] data, ProtocolType protocol, int position);

    void parser(byte[] data);

}
