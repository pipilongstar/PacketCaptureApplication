package com.pipilong;

import com.pipilong.service.Impl.FileReader;
import com.pipilong.service.Impl.MMap;
import com.pipilong.service.Impl.PcapParser;
import com.pipilong.service.Impl.RawPacketCapturer;
import org.apache.catalina.core.ApplicationContext;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

@SpringBootApplication
@EnableAsync
@EnableCaching
@EnableAspectJAutoProxy
@EnableWebSocket
public class PacketCaptureApplication {

    public static void main(String[] args) {

        SpringApplication.run(PacketCaptureApplication.class, args);
    }

}
