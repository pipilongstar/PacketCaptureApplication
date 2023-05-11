package com.pipilong.listener.Impl;

import com.alibaba.fastjson.JSON;
import com.pipilong.domain.Packet;
import com.pipilong.handler.DataHandler;
import com.pipilong.listener.Listener;
import com.pipilong.service.Impl.RawPacketCapturer;
import com.pipilong.service.Impl.RawPacketParser;
import com.pipilong.service.Parser;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;
import java.io.IOException;
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
    private RawPacketParser rawPacketParser;

    private final DataHandler dataHandler;

    @Autowired
    public DataPacketListener(DataHandler dataHandler){
        this.dataHandler = dataHandler;
//        this.rawPacketParser=rawPacketParser;
//        rawPacketCapturer.addListener(this);
//        this.rawPacketParser = rawPacketParser;
    }

    @Override
    public void parse(byte[] packetHeader, byte[] packetData, int id) {
        byte[] data = new byte[packetHeader.length+packetData.length];
        System.arraycopy(packetHeader,0,data,0,packetHeader.length);
        System.arraycopy(packetData,0,data,packetHeader.length,packetData.length);

        Packet packet = rawPacketParser.parser(data,id);
        log.info(packet.toString());
        String message = JSON.toJSONString(packet);
        try {
            DataHandler.sentData(new TextMessage(message));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
































