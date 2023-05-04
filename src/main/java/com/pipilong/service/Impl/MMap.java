package com.pipilong.service.Impl;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author pipilong
 * @createTime 2023/5/4
 * @description
 */
@Service
public class MMap {

    public byte[] read(String filePath) throws IOException {
        byte[] bytes;

        try(FileChannel fileChannel = new RandomAccessFile(filePath,"rw").getChannel();) {
            MappedByteBuffer map = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileChannel.size());
            bytes=new byte[(int)fileChannel.size()];
            map.get(bytes);
        }

        return bytes;
    }

    public void write(byte[] bytes,String toPath) throws IOException {
        try(OutputStream os = Files.newOutputStream(Paths.get(toPath))){
            os.write(bytes);
        }
    }


}
