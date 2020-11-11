package com.revature.thecollective.service;

import com.revature.thecollective.dto.UserEventDTO;
import com.revature.thecollective.model.Event;
import com.revature.thecollective.model.User;
import com.revature.thecollective.repository.EventRepository;
import com.revature.thecollective.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.function.BiFunction;

@Service
@Slf4j
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    public Mono<User> createUser(User user){
        return userRepository.save(user);
    }

    public Flux<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Mono<User> findById(Integer userId){
        return userRepository.findById(userId);
    }

    public Mono<User> updateUser(Integer userId,  User user){
        return userRepository.findById(userId)
                .flatMap(dbUser -> {
                    dbUser.setUser_location(user.getUser_location());
                    // dbUser.setSalary(user.getSalary());
                    return userRepository.save(dbUser);
                });
    }

    public Mono<User> deleteUser(Integer userId){
        return userRepository.findById(userId)
                .flatMap(existingUser -> userRepository.delete(existingUser)
                .then(Mono.just(existingUser)));
    }

    public Flux<User> findUsersByLocation(String location){
        return userRepository.findByLocation(location);
    }

    public Flux<User> fetchUsers(List<Integer> userIds) {
        return Flux.fromIterable(userIds)
                .parallel()
                .runOn(Schedulers.elastic())
                .flatMap(i -> findById(i))
                .ordered((u1, u2) -> u2.getUser_id() - u1.getUser_id());
    }

    private Mono<Event> getEventByUserId(Integer userId){
        return eventRepository.findByUserId(userId);
    }

    public Mono<UserEventDTO> fetchUserAndEvent(Integer userId){
        Mono<User> user = findById(userId).subscribeOn(Schedulers.elastic());
        Mono<Event> event = getEventByUserId(userId).subscribeOn(Schedulers.elastic());
        return Mono.zip(user, event, userEventDTOBiFunction);
    }

    private BiFunction<User, Event, UserEventDTO> userEventDTOBiFunction = (x1, x2) -> UserEventDTO.builder()
            .userLocation(x1.getUser_location())
            .eventId(x2.getEvent_id())
            .userName(x1.getUser_firstname())
            .userId(x1.getUser_id()).build();
}