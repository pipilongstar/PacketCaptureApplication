package com.pipilong.config;

import com.pipilong.handler.DataHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.xml.crypto.Data;

/**
 * @author pipilong
 * @createTime 2023/5/8
 * @description
 */
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {
    private final DataHandler dataHandler;
    @Autowired
    public WebSocketConfig(DataHandler dataHandler){
        this.dataHandler=dataHandler;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(dataHandler,"/data").setAllowedOriginPatterns("*");
    }

}




















