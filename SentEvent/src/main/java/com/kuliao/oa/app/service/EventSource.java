package com.kuliao.oa.app.service;

import com.kuliao.oa.app.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/**
 * @author 董文强
 * @Time 2018/6/26 10:19
 * 这是一个事件的消息源
 */
@Component
public class EventSource extends Observable {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventSource.class);
    WebClient webClient = WebClient.create("http://127.0.0.1:8703");

    @PostConstruct
    public void init() {
       // Flux<Message> m = webClient.get().uri("/").retrieve().bodyToFlux(Message.class);
        Flux<Message> m=  webClient.get()
                .uri("/")
               // .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .flatMapMany(response -> response.bodyToFlux(Message.class));
        m.subscribe(v -> {
            LOGGER.info(v.toString());
            this.sentEvent(v);
        });
    }

    public void sentEvent(Message message) {
        super.notifyObservers(message);
        super.setChanged();
    }

    public void Subscribe(Client client, String name) {
        LOGGER.info("{}-开始订阅", name);
        super.addObserver(client);
    }

    public void unsubscribe(Client client, String name) {
        LOGGER.info("{}-终止订阅", name);
        super.addObserver(client);
    }

    /**
     * 接收消息的客户端
     */
    @FunctionalInterface
    public interface Client extends Observer {
        @Override
        void update(Observable o, Object arg);
    }
}
