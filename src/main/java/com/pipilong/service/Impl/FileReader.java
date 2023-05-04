package com.pipilong.service.Impl;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author pipilong
 * @createTime 2023/5/4
 * @description
 */
@Service
public class FileReader {

    public InputStream read(String filePath) throws IOException {
        return Files.newInputStream(Paths.get(filePath));
    }

    public boolean write(InputStream is,String toPath){
        try {
            OutputStream os = Files.newOutputStream(Paths.get(toPath));
            byte[] bytes=new byte[556002];
            int size = is.available();
            is.read(bytes);
            os.write(bytes,size-64,64);
            is.close();
            os.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

}
