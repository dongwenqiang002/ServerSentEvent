package com.kuliao.oa.app.controller;

import com.kuliao.oa.app.model.Message;
import com.kuliao.oa.app.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 用于接收,发送，修改，订阅Message
 *
 */
@RestController("/message")
public class MessageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    /**
     * 客户端订阅消息，可以源源不断的获取服务器推送
     *
     * @param empNo 订阅者的标识符
     * @return 消息的无限响应流，首先密集的输出所有未读取的消息，之后待有消息即向客户端推送，除非客户端停止接收
     */
    @GetMapping()
    public Flux<ServerSentEvent<Message>> getEvent(String empNo) {
        return messageService.getEvent(empNo)
                .map(message-> ServerSentEvent.builder(message).build());
       /* //return messageService.getEvent(empNo);//这是一般返回的无限序列，上面的是浏览器可识别的*/
    }
    /**
     * 根据消息ID查找消息
     * */
    @GetMapping(params = "id")
    public Mono<Message> getMessage(String id) {
      return messageService.getMessage(id);
    }
    /**
     * 用于添加发布消息
     *
     * @param message 消息主体
     */
    @PostMapping()
    public Mono<Message> addMessage(Message message,  String empNo) {
        if(empNo ==null)empNo="111";
        return messageService.addEvent(message,empNo);
    }

    /**
     * 局部修改消息
     *
     * @param message 消息主体，消息ID必须指定
     */
    @PatchMapping()
    public Mono<Boolean> updateMessage(Message message) {
        if (message == null || message.getId() == null || message.getId().isEmpty()
                || message.getReadState() == null || message.getReadState().isEmpty()) {
            throw new IllegalArgumentException("参数不正确");
        }
        Mono<Message> resMessageMono = messageService.updateMessage(message);
        return resMessageMono.map(v-> v==null? false:true).cast(Boolean.class);
    }

    /**
     * 删除消息
     *
     * @param id 消息ID
     * @return 成功删除返回true
     */
    @DeleteMapping()
    public Mono<Boolean> deleteMessage(String id) {
        return messageService.delete(id);
    }
    /**
     * 获取未读消息数目
     * */
    @GetMapping("/count")
    public Flux<Integer> count(String empNo){
        //TODO
        return Flux.just(1);
    }

    /**
     * 统一异常处理
     *
     * @param ex       异常类型
     * @param response 服务器对象，用来设置HTTP的错误码
     * @return 异常信息
     */
    @ExceptionHandler(Exception.class)
    public Mono<String> handle(Exception ex, ServerHttpResponse response) {
        LOGGER.error(ex.toString()+"\n");
        ex.printStackTrace();
        response.setStatusCode(HttpStatus.valueOf(500));
        return Mono.just(ex.getMessage());
    }
    /**
     *  MongodDb出现问题
     * */
    @ExceptionHandler(UncategorizedMongoDbException.class)
    public Mono<String> handle(UncategorizedMongoDbException ex, ServerHttpResponse response) {
        LOGGER.error(ex.toString()+"\n");
        response.setStatusCode(HttpStatus.valueOf(500));
        return Mono.just(ex.getMessage());
    }
}
