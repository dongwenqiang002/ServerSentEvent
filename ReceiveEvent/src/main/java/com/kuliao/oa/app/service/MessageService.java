package com.kuliao.oa.app.service;

import com.kuliao.oa.app.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


/**
 * @author 董文强
 * @Time 2018/6/26 10:10
 */
@Service
public class MessageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
    @Autowired
    private EventSource source;
    WebClient webClient = WebClient.create("http://127.0.0.1:8703");
   /* public Flux<ServerSentEvent<String>> getEvent() {
        return Flux.create(emitter -> {
            //创建接收事件消息的客户，实现给他发消息的回调函数
            EventSource.Client client = (o, arg) -> {
                //发送一帧消息
                emitter.next(ServerSentEvent.builder((String) arg).build());
            };
            //将用户添加到消息源中，被消息源监听
            source.Subscribe(client,"test");
            //链接被断开的回调函数
            emitter.onDispose(() -> {
                source.unsubscribe(client,"test");
            });
        });

    }*/

   /* public void sendEvent(String message) {
        this.source.sentEvent(message);
    }
*/
    public Mono<Boolean> publishMsg(Message message) {
        //webClient.post().uri("").body()
       return webClient.post()
                .uri("/")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(message)
                .retrieve()
                .bodyToMono(Boolean.class);

    }
}
