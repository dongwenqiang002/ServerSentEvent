package com.kuliao.oa.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Observable;
import java.util.Observer;

/**
 * @author 董文强
 * @Time 2018/6/26 10:19
 * 这是一个事件的消息源
 */
@Component
public class EventSource extends Observable {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventSource.class);

    @PostConstruct
    public void init(){
        LOGGER.info("初始化");
    }
    public void sentEvent(String message){
        super.notifyObservers(message);
        super.setChanged();
    }

    public void Subscribe(Client client,String name){
        LOGGER.info("{}-开始订阅",name);
        super.addObserver(client);
    }

    public void unsubscribe(Client client,String name){
        LOGGER.info("{}-终止订阅",name);
        super.addObserver(client);
    }

    /**
     * 接收消息的客户端
     * */
    @FunctionalInterface
    public interface Client extends Observer{
    }
}
