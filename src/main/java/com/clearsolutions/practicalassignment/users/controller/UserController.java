package com.clearsolutions.practicalassignment.users.controller;

import com.clearsolutions.practicalassignment.users.domain.Range;
import com.clearsolutions.practicalassignment.users.domain.User;
import com.clearsolutions.practicalassignment.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody @Valid User user) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> readById(@PathVariable UUID id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findUser(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable UUID id, @RequestBody @Valid User user) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.updateUser(id, user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> patchUpdate(@PathVariable UUID id, @RequestBody User incomingUser) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.patchUpdateUser(id, incomingUser));
    }

    @GetMapping("/all")
    public List<User> readAll() {
        return service.getAllUsers();
    }

    @PostMapping("/search")
    public List<User> findByDateRange(@RequestBody @Valid Range range) {
        return service.findUsersByRange(range);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id) {
        if (service.deleteUser(id)) return ResponseEntity.status(HttpStatus.OK).build();
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
