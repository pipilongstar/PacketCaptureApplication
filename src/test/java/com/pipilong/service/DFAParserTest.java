package com.pipilong.service;

import com.pipilong.service.Impl.DFAParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author pipilong
 * @createTime 2023/5/15
 * @description
 */
@SpringBootTest
public class DFAParserTest {

    @Autowired
    private DFAParser dfaParser;

    @Test
    public void test(){
        String s = "  tcp.port =80  & arp";
        dfaParser.parser(s);
        dfaParser.print();
    }













}
