package com.pipilong.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.pipilong.domain.Frame;
import com.pipilong.domain.PacketData;
import com.pipilong.domain.packet.*;
import com.pipilong.enums.DNSType;
import com.pipilong.enums.ProtocolType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author pipilong
 * @createTime 2023/5/11
 * @description
 */
@Component
public class KryoSerializer {
    private final Kryo kryo;
    public KryoSerializer(){
        kryo = new Kryo();
        kryo.register(PacketData.class);
        kryo.register(ARPPacket.class);
        kryo.register(DNSPacket.class);
        kryo.register(DNSPacket.RR.class);
        kryo.register(DNSPacket.Queries.class);
        kryo.register(EthernetPacket.class);
        kryo.register(IPv4Packet.class);
        kryo.register(IPv6Packet.class);
        kryo.register(UDPPacket.class);
        kryo.register(Frame.class);
        kryo.register(Pair.class);
        kryo.register(ProtocolType.class);
        kryo.register(ArrayList.class);
        kryo.register(DNSType.class);
        kryo.register(HashMap.class);
        kryo.setReferences(true);
    }

    public byte[] serialize(Object obj){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        kryo.writeObject(output,obj);
        output.flush();
        output.close();
        return byteArrayOutputStream.toByteArray();
    }

    public <T> T deSerialize(byte[] bytes, Class<T> clazz){
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(byteArrayInputStream);
        T obj = kryo.readObject(input, clazz);
        input.close();
        return obj;
    }

}
