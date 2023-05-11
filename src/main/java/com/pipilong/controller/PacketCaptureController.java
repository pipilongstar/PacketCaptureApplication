package com.pipilong.controller;

import com.pipilong.service.Impl.RawPacketCapturer;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author pipilong
 * @createTime 2023/5/9
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/packetCapture")
public class PacketCaptureController {

    private final RawPacketCapturer rawPacketCapturer;

    private static Thread currentThread;

    @Autowired
    public PacketCaptureController(RawPacketCapturer rawPacketCapturer) throws PcapNativeException {
        this.rawPacketCapturer = rawPacketCapturer;
    }

    @PostMapping("/open")
    public void open(@RequestBody String network) throws PcapNativeException, UnsupportedEncodingException {
        String decode = URLDecoder.decode(network, "utf-8");
        decode = decode.substring(0,decode.length()-1);
        PcapNetworkInterface dev = Pcaps.getDevByName(decode);
        log.info(decode);
        currentThread = new Thread(() -> {
            try {
                rawPacketCapturer.start(dev);
            } catch (PcapNativeException | NotOpenException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        currentThread.start();
    }

    @PostMapping("/close")
    public void close(){
        currentThread.stop();
    }

}
