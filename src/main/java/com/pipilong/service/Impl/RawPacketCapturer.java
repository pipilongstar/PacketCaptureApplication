package com.pipilong.service.Impl;

import com.pipilong.exception.DataLengthOverException;
import com.pipilong.listener.Listener;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author pipilong
 * @createTime 2023/5/7
 * @description
 */
@Service
@Slf4j
public class RawPacketCapturer {

    private final Listener dataPacketListener;
    private final List<Listener> listeners = new ArrayList<>();
    private PcapHandle handle;

    private final int TOTAL_LENGTH = 100*1024*1024;
    private volatile byte[] data = new byte[TOTAL_LENGTH];
    private final ThreadPoolExecutor executor =new ThreadPoolExecutor(
            1,
            1,
            60,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10000));
    private int id = 1;
    private int pointer = 0;
    @Autowired
    public RawPacketCapturer(Listener dataPacketListener) {
        this.dataPacketListener = dataPacketListener;
        initDataArray();
    }

    public void start(PcapNetworkInterface network) throws PcapNativeException, NotOpenException, InterruptedException {

        int snapLen = 65536;//所能捕获的最大长度
        int timeout = 10;//捕获的超时时间 单位是s
        this.handle = network.openLive(snapLen, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, timeout);

        PacketListener listener = pcapPacket -> {
            byte[] packetData;
            if(Thread.currentThread().isInterrupted()){
                handle.close();
                return ;
            }else{
                packetData = pcapPacket.getRawData();
            }
            byte[] packetHeader = new byte[16];
            //包头是用小端存数据
            int position;
            //生成日期
            OffsetDateTime currentTimestamp = OffsetDateTime.parse(pcapPacket.getTimestamp().toString(), DateTimeFormatter.ISO_DATE_TIME);
            long seconds = currentTimestamp.toEpochSecond();
            position = 3;
            for (int i = 0; i < 4; i++) {
                packetHeader[position--] = (byte) ((seconds >> (24 - i * 8)) & 0xff);
            }
            position = 7;
            int nanos = currentTimestamp.getNano()/1000;
            for (int i = 0; i < 4; i++) {
                packetHeader[position--] = (byte) ((nanos >> (24 - i * 8)) & 0xff);
            }
            //生成报文长度
            position = 11;
            int capLen = packetData.length;
            int Len = packetData.length;
            for (int i = 0; i < 4; i++) {
                packetHeader[position--] = (byte) ((capLen >> (24 - i * 8)) & 0xff);
            }
            position = 15;
            for (int i = 0; i < 4; i++) {
                packetHeader[position--] = (byte) ((Len >> (24 - i * 8)) & 0xff);
            }
            //解析数据
            dataPacketListener.parse(packetHeader, packetData, id++);

            if(pointer+packetData.length+packetHeader.length <= TOTAL_LENGTH){
                for(byte b : packetHeader){
                    data[pointer++] = b;
                }
                for(byte b : packetData){
                    data[pointer++] = b;
                }
            }else {
                handle.close();
                try {
                    throw new DataLengthOverException("捕获数据包数量上限！");
                } catch (DataLengthOverException e) {
                    throw new RuntimeException(e);
                }
            }
//            for(Listener l : listeners){
//                log.info("aaa");
//                l.parse(packetHeader,packetData);
//            }
        };

        handle.loop(-1, listener);
    }

    public void close() {
        handle.close();
        id=1;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void initDataArray(){
        pointer = 0;
        id=1;
        data = new byte[TOTAL_LENGTH];
        int[] t = {0xd4,0xc3,0xb2,0xa1,0x02,0x00,0x04,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x04,0x00,0x01,0x00,0x00,0x00};
        for(int i : t){
            data[pointer++] = (byte) i;
        }
    }

    public byte[] getDataArray(){
        byte[] res = new byte[pointer];
        System.arraycopy(data,0,res,0,pointer);
        initDataArray();
        return res;
    }

    public void clearDataArray(){
        initDataArray();
    }


}
