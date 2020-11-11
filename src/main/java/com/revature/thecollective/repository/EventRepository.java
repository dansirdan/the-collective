package com.revature.thecollective.repository;

import com.revature.thecollective.model.Event;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface EventRepository extends ReactiveCrudRepository<Event,Integer> {
    Mono<Event> findByUserId(Integer userId);
}