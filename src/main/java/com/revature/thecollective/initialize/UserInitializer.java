package com.revature.thecollective.initialize;

import com.revature.thecollective.model.Event;
import com.revature.thecollective.model.User;
import com.revature.thecollective.repository.EventRepository;
import com.revature.thecollective.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Component
@Profile("!test")
@Slf4j
public class UserInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;
    
    @Override
    public void run(String... args) {
            initialDataSetup();
    }

    private List<User> getData(){
        return Arrays.asList(new User(null,"Suman", "Das", "password1","suman.das@gmail.com", "Seattle"),
                new User(null,"Stacey", "Willis", "password1","stacey.willis@gmail.com", "Los Angeles"),
                new User(null,"Doug", "Varone", "password1","doug.dances@gmail.com", "New York"));
    }

    private List<Event> getEvents(){
        return Arrays.asList(new Event(null,"Dance","performance", "contemporary",100,80,"sunday","MTN", "1/1/2021", true, null, 1),
                new Event(null,"Dance", "class","jazz",20,45,"saturday","EST","1/1/2021", false, null, 2));
    }

    private void initialDataSetup() {
        userRepository.deleteAll()
                .thenMany(Flux.fromIterable(getData()))
                .flatMap(userRepository::save)
                .thenMany(userRepository.findAll())
                .subscribe(user -> {
                    log.info("User Inserted from CommandLineRunner " + user);
                });

        eventRepository.deleteAll()
                .thenMany(Flux.fromIterable(getEvents()))
                .flatMap(eventRepository::save)
                .thenMany(eventRepository.findAll())
                .subscribe(user -> {
                    log.info("Event Inserted from CommandLineRunner " + user);
                });

    }

}