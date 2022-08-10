package org.enset.budget_expanse_management.controllers;

import org.enset.budget_expanse_management.model.User;
import org.enset.budget_expanse_management.repositories.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

//@Transactional
@RestController
@RequestMapping(path = "/api")
@CrossOrigin(origins = "http://localhost:4090")//Means that only pages
// from this Website are allowed to request resources from This App(Backend), it could be integrated by Spring Security.
public class UserRestController {

    private final UserRepository userRepository;

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

    @PostMapping(path = "/users/admin")
    public User addNewUserController(@RequestBody User user){
        System.out.println(" -----------------------------------");
        System.out.println(" ------------- User is added ----------");
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    @PutMapping(path = "/users/admin/{id}")
    public User editUserController(@PathVariable(name = "id") String id ,@RequestBody User user){
        boolean isUserPresent = userRepository.findById(UUID.fromString(id)).isPresent();
        if (!isUserPresent){
            throw new RuntimeException("User is not found, please edit an existing user!");
        }
        // Maybe to be hidden
        user.setId(UUID.fromString(id));
        return userRepository.save(user);
    }

    @DeleteMapping(path = "/users/admin/delete/{id}")
    public void deleteUser(@PathVariable(name = "id") String id){
        boolean isUserPresent = userRepository.findById(UUID.fromString(id)).isPresent();
        if (!isUserPresent){
            throw new RuntimeException("User is not found, please delete an existing user!");
        }
        User userToBeDeleted = userRepository.findById(UUID.fromString(id)).get();
        userRepository.delete(userToBeDeleted);
    }

}
