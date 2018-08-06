package com.kuliao.oa.app.controller;

import com.kuliao.oa.app.model.Message;
import com.kuliao.oa.app.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author 董文强
 * @Time 2018/6/27 16:10
 */
@CrossOrigin(allowCredentials = "true")
@RestController
public class MessageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    @GetMapping("/")
    public Flux<ServerSentEvent<String>> getEvent() {
        LOGGER.info("被访问");
        return null;//messageService.getEvent();
    }

    @PostMapping("/")
    public Mono<String> addMessage(Message message) throws Exception {
        LOGGER.info("message");
        messageService.publishMsg(message);
        return Mono.just("OK");
    }

}
