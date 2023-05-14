package com.pipilong.service.Impl;

import com.pipilong.domain.PacketData;
import com.pipilong.util.KryoSerializer;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pipilong
 * @createTime 2023/5/14
 * @description
 */
@Service
public class DataWriter {

    private final FileChannel fileChannel;
    private final KryoSerializer kryoSerializer;
    private final Map<Integer, Pair<Long,Integer>> cache;

    @Autowired
    public DataWriter(
            FileChannel fileChannel,
            KryoSerializer kryoSerializer
    ){
        this.fileChannel=fileChannel;
        this.kryoSerializer=kryoSerializer;
        cache=new HashMap<>();
    }

    public void setDetailOfPacket(PacketData packetData) throws IOException {
        byte[] packetDataByteArray = kryoSerializer.serialize(packetData);
        long position = fileChannel.size();
        fileChannel.position(position);
        fileChannel.truncate(fileChannel.position()+packetDataByteArray.length);
        MappedByteBuffer mmap = fileChannel.map(FileChannel.MapMode.READ_WRITE, fileChannel.position(), packetDataByteArray.length);
        mmap.put(packetDataByteArray);
        mmap.force();
        cache.put(packetData.getFrame().getFrameId(),new Pair<>(position,packetDataByteArray.length));
    }

    public PacketData getDetailOfPacket(int id) throws IOException {
        Pair<Long, Integer> metaData = cache.get(id);
        MappedByteBuffer mmap = fileChannel.map(FileChannel.MapMode.READ_WRITE, metaData.getKey(), metaData.getValue());
        byte[] data = new byte[metaData.getValue()];
        mmap.get(data);
        return kryoSerializer.deSerialize(data, PacketData.class);
    }

}
