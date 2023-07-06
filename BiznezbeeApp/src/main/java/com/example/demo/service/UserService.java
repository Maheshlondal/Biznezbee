package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> filterUsers(String userType, Integer ageGreater, Integer ageLessThan, String nameFilter) {
        List<User> users = userRepository.findAll();

        // Filter by user type
        if (userType != null && !userType.isEmpty()) {
            users = users.stream()
                    .filter(user -> user.getUserType().equalsIgnoreCase(userType))
                    .collect(Collectors.toList());
        }

        // Filter by age group
        if (ageGreater != null && ageLessThan != null) {
            users = users.stream()
                    .filter(user -> {
                        int age = calculateAge(user.getDateOfBirth());
                        return age >= ageGreater && age <= ageLessThan;
                    })
                    .collect(Collectors.toList());
        } else if (ageGreater != null) {
            users = users.stream()
                    .filter(user -> {
                        int age = calculateAge(user.getDateOfBirth());
                        return age >= ageGreater;
                    })
                    .collect(Collectors.toList());
        } else if (ageLessThan != null) {
            users = users.stream()
                    .filter(user -> {
                        int age = calculateAge(user.getDateOfBirth());
                        return age <= ageLessThan;
                    })
                    .collect(Collectors.toList());
        }

        // Filter by name starts with
        if (nameFilter != null && !nameFilter.isEmpty()) {
            users = users.stream()
                    .filter(user -> user.getName().toLowerCase().startsWith(nameFilter.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Ensure that the list is sorted by name
        users.sort(Comparator.comparing(User::getName));

        return users;
    }

    private int calculateAge(String dateOfBirth) {
        LocalDate currentDate = LocalDate.now();
        LocalDate birthDate = LocalDate.parse(dateOfBirth);
        return Period.between(birthDate, currentDate).getYears();
    }
}