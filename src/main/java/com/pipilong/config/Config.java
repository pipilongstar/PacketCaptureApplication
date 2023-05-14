package com.pipilong.config;

import com.esotericsoftware.kryo.Kryo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author pipilong
 * @createTime 2023/5/11
 * @description
 */
@Configuration
public class Config {

    /**
     * 文件通道，用来创建内存映射的。
     * @return 文件通道
     * @throws IOException 文件读取异常
     */
    @Bean
    public FileChannel getMmap() throws IOException {
        FileChannel fileChannel = new RandomAccessFile(new ClassPathResource("static/packet.data").getFile(), "rw").getChannel();
        fileChannel.position(0);
        fileChannel.force(true);
        fileChannel.truncate(0);
        return fileChannel;
    }


}
























