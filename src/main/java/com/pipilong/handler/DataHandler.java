package com.pipilong.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

/**
 * @author pipilong
 * @createTime 2023/5/8
 * @description
 */
@Slf4j
@Component
public class DataHandler implements WebSocketHandler {

    private static WebSocketSession currentSession;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        currentSession = session;
        System.out.println("success connection!" + session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        session.sendMessage(message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.info(session.getId()+"-----"+exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public static void sentData(WebSocketMessage<?> data) throws Exception {
        currentSession.sendMessage(data);
    }

}
















