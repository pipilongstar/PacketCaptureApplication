package com.pipilong.service.Impl;
import com.pipilong.listener.Impl.DataPacketListener;
import com.pipilong.listener.Listener;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pipilong
 * @createTime 2023/5/7
 * @description
 */
@Service
@Slf4j
public class RawPacketCapturer {
    private volatile boolean flag;

    private final Listener dataPacketListener;
    private final List<Listener> listeners = new ArrayList<>();
    private PcapHandle handle;
    @Autowired
    public RawPacketCapturer(Listener dataPacketListener){
        this.dataPacketListener= dataPacketListener;
    }
    public void start(PcapNetworkInterface network) throws PcapNativeException, NotOpenException, InterruptedException {
        flag=true;
        int snapLen =65536;//所能捕获的最大长度
        int timeout =10;//捕获的超时时间 单位是s
        this.handle = network.openLive(snapLen, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, timeout);

        PacketListener listener =pcapPacket -> {
            byte[] packetData = pcapPacket.getRawData();
            byte[] packetHeader = new byte[16];
            //包头是用小端存数据
            int position;
            //生成日期
            OffsetDateTime currentTimestamp = OffsetDateTime.parse(pcapPacket.getTimestamp().toString(), DateTimeFormatter.ISO_DATE_TIME);
            long seconds = currentTimestamp.toEpochSecond();
            position=3;
            for(int i=0;i<4;i++){
                packetHeader[position--] = (byte) ((seconds >> (24 - i * 8)) & 0xff);
            }
            position=7;
            int nanos = currentTimestamp.getNano();
            for(int i=0;i<4;i++){
                packetHeader[position--] = (byte) ((nanos >> (24 - i * 8)) & 0xff);
            }
            //生成报文长度
            position=11;
            int capLen = packetData.length;
            int Len = packetData.length;
            for(int i=0;i<4;i++){
                packetHeader[position--] = (byte) ((capLen >> (24 - i * 8)) & 0xff);
            }
            position=15;
            for(int i=0;i<4;i++){
                packetHeader[position--] = (byte) ((Len >> (24 - i * 8)) & 0xff);
            }
            //解析数据
            dataPacketListener.parse(packetHeader,packetData);
//            for(Listener l : listeners){
//                log.info("aaa");
//                l.parse(packetHeader,packetData);
//            }
        };

        handle.loop(2000,listener);
    }

    public void close(){
        if(flag){
            handle.close();
            flag=false;
        }
    }

    public void addListener(Listener listener){
        listeners.add(listener);
    }


}
