package com.kuliao.oa.app.repository;

import com.kuliao.oa.app.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * @author 董文强
 * @Time 2018/7/3 13:02
 */
@Repository
public interface UserRepository extends ReactiveCrudRepository<User,String> {
    Mono<User> findUserByEmpNo(String empNo);

}
