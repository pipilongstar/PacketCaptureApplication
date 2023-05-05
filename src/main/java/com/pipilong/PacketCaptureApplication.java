package com.pipilong;

import com.pipilong.service.Impl.FileReader;
import com.pipilong.service.Impl.MMap;
import com.pipilong.service.Impl.PcapParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
public class PacketCaptureApplication {
    @Autowired
    PcapParser pcapParser;
    @Autowired
    MMap mMap;
    public static void main(String[] args) throws IOException {
        SpringApplication.run(PacketCaptureApplication.class, args);
        PcapParser pcapParser = new PcapParser();
        MMap mMap = new MMap();
        byte[] data = mMap.read("D:\\抓包软件\\第八天\\day8.pcap.txt");
        pcapParser.parser(data);
    }

}
