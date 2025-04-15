package com.example.techchallenge.controller;

import com.example.techchallenge.Request.UserRequest;
import com.example.techchallenge.Response.UserResponse;
import com.example.techchallenge.mapper.ModelMapperBase;
import com.example.techchallenge.model.UserEntity;
import com.example.techchallenge.serviceImpl.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        UserEntity createdUserEntity = userServiceImpl.createUser(userRequest.toEntity());
        return ResponseEntity.status(HttpStatus.OK).body(ModelMapperBase.map(createdUserEntity, UserResponse.class));
    }

    @PostMapping("/login")
     public ResponseEntity<String> loginUser(@RequestBody UserRequest userRequest) {
        if (userServiceImpl.validateLogin(userRequest.username(), userRequest.password())) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid login credentials");
        }
    }

    @GetMapping
       public ResponseEntity<Page<UserEntity>> getAllUsers(@RequestParam(value = "page",required = false) Integer page,
                                                                  @RequestParam(value = "size", required = false) Integer size) {
        Page<UserEntity> pages = userServiceImpl.getAll(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(pages);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest) {
        Optional<UserEntity> updatedUserEntity = userServiceImpl.updateUser(userRequest.toEntity());
        return ResponseEntity.status(HttpStatus.OK).body(ModelMapperBase.map(updatedUserEntity, UserResponse.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userServiceImpl.delete(id);
        return ResponseEntity.noContent().build();
    }

}