package com.revature.thecollective.repository;

import com.revature.thecollective.model.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface UserRepository extends ReactiveCrudRepository<User,Integer> {
    @Query("select * from users where user_location = $1")
    Flux<User> findByLocation(String user_location);
}