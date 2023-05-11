package com.pipilong.config;

import com.esotericsoftware.kryo.Kryo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
     * mmap内存映射
     * @return 内存映射
     * @throws IOException 文件读取异常
     */
    @Bean
    public MappedByteBuffer getMmap() throws IOException {
        FileChannel fileChannel = new RandomAccessFile("classpath:static/packet.data", "rw").getChannel();
        return fileChannel.map(FileChannel.MapMode.READ_WRITE,0,fileChannel.size());
    }


}
























