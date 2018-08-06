package com.kuliao.oa.app.repository;

import com.kuliao.oa.app.model.Message;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author 董文强
 * @Time 2018/7/2 10:33
 */
@Repository
public interface MessageRepository extends ReactiveCrudRepository<Message,String> {
    Flux<Message> findMessagesByReadStateEquals(String state);
    Flux<Message> findMessagesByReadStateEqualsAndIdIn(String state, List ids);
}
