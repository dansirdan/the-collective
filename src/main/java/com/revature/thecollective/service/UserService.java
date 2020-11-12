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

    public Mono<User> findById(Integer user_id){
        return userRepository.findById(user_id);
    }

    public Mono<User> updateUser(Integer user_id,  User user){
        return userRepository.findById(user_id)
                .flatMap(dbUser -> {
                    dbUser.setUser_location(user.getUser_location());
                    // dbUser.setSalary(user.getSalary());
                    return userRepository.save(dbUser);
                });
    }

    public Mono<User> deleteUser(Integer user_id){
        return userRepository.findById(user_id)
                .flatMap(existingUser -> userRepository.delete(existingUser)
                .then(Mono.just(existingUser)));
    }

    public Flux<User> findUsersByLocation(String location){
        return userRepository.findByLocation(location);
    }

    public Flux<User> fetchUsers(List<Integer> user_ids) {
        return Flux.fromIterable(user_ids)
                .parallel()
                .runOn(Schedulers.elastic())
                .flatMap(i -> findById(i))
                .ordered((u1, u2) -> u2.getUser_id() - u1.getUser_id());
    }

    private Mono<Event> getEventByUserId(Integer event_userID){
        return eventRepository.findByUserId(event_userID);
    }

    public Mono<UserEventDTO> fetchUserAndEvent(Integer user_id){
        Mono<User> user = findById(user_id).subscribeOn(Schedulers.elastic());
        Mono<Event> event = getEventByUserId(user_id).subscribeOn(Schedulers.elastic());
        return Mono.zip(user, event, userEventDTOBiFunction);
    }

    private BiFunction<User, Event, UserEventDTO> userEventDTOBiFunction = (x1, x2) -> UserEventDTO.builder()
            .user_location(x1.getUser_location())
            .event_id(x2.getEvent_id())
            .user_firstname(x1.getUser_firstname())
            .user_id(x1.getUser_id()).build();
}