package com.kuliao.oa.app.service;

import com.kuliao.oa.app.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Observable;
import java.util.Random;


/**
 * @author 董文强
 * @Time 2018/6/26 10:10
 */
@Service
public class MessageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);



    @Autowired
    private EventSource source;
    Random random = new Random();
    public Flux<Message> getEvent() {
        return Flux.create(emitter -> {
            //创建接收事件消息的客户，实现给他发消息的回调函数
            EventSource.Client client = ( o,  arg) -> {
                //发送一帧消息
                emitter.next((Message) arg);
            };
            //将用户添加到消息源中，被消息源监听
            source.Subscribe(client,"test");
            //链接被断开的回调函数
            emitter.onDispose(() -> {
                source.unsubscribe(client,"test");
            });
        });

    }
    public Flux<Integer> getCount() {
        return   Flux.interval(Duration.ofSeconds(2)).map(l->random.nextInt(50));
    }

}
