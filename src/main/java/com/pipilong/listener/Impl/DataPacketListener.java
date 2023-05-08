package com.pipilong.listener.Impl;

import com.pipilong.listener.Listener;
import com.pipilong.service.Impl.RawPacketCapturer;
import com.pipilong.service.Impl.RawPacketParser;
import com.pipilong.service.Parser;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author pipilong
 * @createTime 2023/5/7
 * @description
 */
@Slf4j
@Service
public class DataPacketListener implements Listener {

    @Autowired
    private Parser rawPacketParser;

    public DataPacketListener(){
//        this.rawPacketParser=rawPacketParser;
//        rawPacketCapturer.addListener(this);
//        this.rawPacketParser = rawPacketParser;
    }

    @Override
    public void parse(byte[] packetHeader, byte[] packetData) {
        byte[] data = new byte[packetHeader.length+packetData.length];
        System.arraycopy(packetHeader,0,data,0,packetHeader.length);
        System.arraycopy(packetData,0,data,packetHeader.length,packetData.length);

        rawPacketParser.parser(data);
    }

}
































