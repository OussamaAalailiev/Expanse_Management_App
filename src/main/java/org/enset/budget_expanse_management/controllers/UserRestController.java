package org.enset.budget_expanse_management.controllers;

import org.enset.budget_expanse_management.model.User;
import org.enset.budget_expanse_management.repositories.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api")
public class UserRestController {

    private UserRepository userRepository;

    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/users")
    public List<User> getAllUsersController(){
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    public User getUserByIdController(@PathVariable(name = "id") String id){
        if (userRepository.findById(UUID.fromString(id)).isEmpty()){
            throw new RuntimeException("User was NOT Found !");
        }
        return userRepository.findById(UUID.fromString(id)).get();
    }

}
