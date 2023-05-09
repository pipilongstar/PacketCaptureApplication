package com.pipilong.controller;

import com.alibaba.fastjson.JSON;
import com.pipilong.domain.Packet;
import com.pipilong.handler.DataHandler;
import com.pipilong.service.Impl.RawPacketCapturer;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

/**
 * @author pipilong
 * @createTime 2023/5/9
 * @description
 */
@Controller
@RequestMapping("/packetCapture")
public class PacketCaptureController {

    private final RawPacketCapturer rawPacketCapturer;

    private final PcapNetworkInterface networkInterface = Pcaps.getDevByName("\\Device\\NPF_{E924369A-8854-40C6-AD69-85ECD2F962B4}");

    @Autowired
    private Packet packet;

    @Autowired
    private DataHandler dataHandler;

    @Autowired
    public PacketCaptureController(RawPacketCapturer rawPacketCapturer) throws PcapNativeException {
        this.rawPacketCapturer = rawPacketCapturer;
    }

    @PostMapping("/open")
    @ResponseBody
    public void open() throws Exception {
        new Thread(()->{
            try {
                rawPacketCapturer.start(networkInterface);
            } catch (PcapNativeException | NotOpenException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @PostMapping("/close")
    @ResponseBody
    public void close(){
        rawPacketCapturer.close();
    }


}
