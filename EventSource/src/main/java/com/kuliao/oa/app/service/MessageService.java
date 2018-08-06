package com.kuliao.oa.app.service;

import com.kuliao.oa.app.model.Message;
import com.kuliao.oa.app.model.User;
import com.kuliao.oa.app.repository.MessageRepository;
import com.kuliao.oa.app.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;


/**
 *
 */
@Service
public class MessageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);


    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    EventSource<Message> source;


    /**
     * 获取用户消息
     *
     * @param empNo 用户标识符
     * @return 消息
     */
    public Flux<Message> getEvent(String empNo) {
        //获取之前 已经存在但是未读的消息
        Flux<Message> mongoddbMessage = Flux.never();
        userRepository.findUserByEmpNo(empNo).defaultIfEmpty(new User(empNo, empNo, "new", new ArrayList<>()))
                .subscribe(user -> mongoddbMessage.mergeWith(messageRepository.findMessagesByReadStateEqualsAndIdIn(Message.UNREAD, user.getMessageId())));

        //将要发送的消息，（使用观察者模式，有新的消息将实时推送到客户端
        Flux<Message> nowMessage = Flux.create(emitter -> {
            //创建接收事件消息的客户，实现给他发消息的回调函数
            //将用户添加到消息源中，被消息源监听
            EventSource.Client client = source.Subscribe((o, a) -> {
                //发送一帧消息
                emitter.next(a);
            }, empNo);
            //链接被断开的回调函数
            emitter.onDispose(() -> {
                source.unsubscribe(client, empNo);
            });
        });
        //合并消息
        return mongoddbMessage.mergeWith(nowMessage);
    }

    /**
     * 实时添加消息，添加入数据库
     * 如果接收消息的用户在线，并实时推送
     *
     * @param msg   消息主体
     * @param empNo 用户标识符 (用户若在mongodDB中不存在，则创建一个)
     * @return 发送后的消息
     */
    public Mono<Message> addEvent(Message msg, String empNo) {
        msg.setTime(new Date());
        //添加消息入库
        Mono<Message> messageMono = messageRepository.save(msg).doOnNext(messageOk -> {
            Flux<User> userFlux = null;
            if (empNo == null) {
                userFlux = userRepository.findAll();
            } else {
                userFlux = userRepository.findUserByEmpNo(empNo).flux().defaultIfEmpty(new User(empNo, empNo, "test", new ArrayList<>()));
            }
            userFlux.subscribe(user -> {
                user.setMessageId(user.getMessageId() == null ? new ArrayList<>() : user.getMessageId());
                user.getMessageId().add(messageOk.getId());
                userRepository.save(user).subscribe(u -> {
                    //添加成功后进行推送
                    //将用户后台数据库中的消息更新完成
                    source.sentEvent(messageOk, u.getEmpNo());
                });

            });
        });
        return messageMono;
    }

    /**
     * 修改消息
     */
    public Mono<Message> updateMessage(Message message) {

        return messageRepository.save(message).map(v -> {
            LOGGER.info("update: {}", v.toString());
            if (v.getReadState().equals(Message.UNREAD)) {
                return v;
            } else {
                return null;
            }
        });
    }

    /**
     * 删除
     */
    public Mono<Boolean> delete(String id) {
        LOGGER.info("delete: {}", id);
        return messageRepository.deleteById(id).thenReturn(true);
    }

    /**
     * 根据消息ID查找消息
     * */
    public Mono<Message> getMessage(String id) {
        return messageRepository.findById(id);
    }
}
