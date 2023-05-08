package com.pipilong.service;

import com.pipilong.service.Impl.*;
import org.junit.jupiter.api.Test;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

/**
 * @author pipilong
 * @createTime 2023/5/7
 * @description
 */
@SpringBootTest
public class RawPacketCapturerTest {
    @Autowired
    private RawPacketCapturer packetCapturer;
    @Autowired
    private MMap mMap;
    @Autowired
    private PcapParser pcapParser;


    @Test
    public void test1() throws PcapNativeException, NotOpenException, InterruptedException {
        PcapNetworkInterface networkInterface = Pcaps.getDevByName("\\Device\\NPF_{E924369A-8854-40C6-AD69-85ECD2F962B4}");
        long start = System.currentTimeMillis();
        packetCapturer.start(networkInterface);
        long end = System.currentTimeMillis();
        System.out.println(end-start);
    }

    @Test
    public void test2() throws IOException {
        byte[] data = mMap.read("D:\\test2.pcap");
        pcapParser.parser(data);
    }


}















