package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
    private final UserService userService;

	@Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        User addedUser = userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedUser);
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        Optional<User> existingUser = userService.getUserById(id);
        if (existingUser.isPresent()) {
            user.setId(id);
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> existingUser = userService.getUserById(id);
        if (existingUser.isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<List<User>> filterUsers(@RequestParam(required = false) String userType,
                                                  @RequestParam(required = false) Integer ageGreater,
                                                  @RequestParam(required = false) Integer ageLessThan,
                                                  @RequestParam(required = false) String nameStartsWith) {
        List<User> filteredUsers = userService.filterUsers(userType, ageGreater, ageLessThan, nameStartsWith);
        return ResponseEntity.ok(filteredUsers);
    }
}
