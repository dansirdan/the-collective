package com.revature.thecollective.controller;

import com.revature.thecollective.dto.UserEventDTO;
import com.revature.thecollective.model.User;
import com.revature.thecollective.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
@Autowired
private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> create(@RequestBody User user){
        return userService.createUser(user);
    }

    @GetMapping
    public Flux<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{user_id}")
    public Mono<ResponseEntity<User>> getUserById(@PathVariable Integer user_id){
        Mono<User> user = userService.findById(user_id);
        return user.map( u -> ResponseEntity.ok(u))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{user_id}")
    public Mono<ResponseEntity<User>> updateUserById(@PathVariable Integer user_id, @RequestBody User user){
        return userService.updateUser(user_id,user)
                .map(updatedUser -> ResponseEntity.ok(updatedUser))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{user_id}")
    public Mono<ResponseEntity<Void>> deleteUserById(@PathVariable Integer user_id){
        return userService.deleteUser(user_id)
                .map( r -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/location/{user_location}")
    public Flux<User> getUsersByLocation(@PathVariable String user_location) {
        return userService.findUsersByLocation(user_location);
    }

    @PostMapping("/search/id")
    public Flux<User> fetchUsersByIds(@RequestBody List<Integer> user_ids) {
        return userService.fetchUsers(user_ids);
    }

    @GetMapping("/{user_id}/event")
    public Mono<UserEventDTO> fetchUserAndEvent(@PathVariable Integer user_id){
        return userService.fetchUserAndEvent(user_id);
    }

}