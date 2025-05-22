package com.project.mercaduca.controllers;

import com.project.mercaduca.dtos.UserUpdateDTO;
import com.project.mercaduca.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestBody UserUpdateDTO userUpdateDTO) {
        userService.updateProfile(userUpdateDTO);
        return ResponseEntity.ok("Perfil actualizado correctamente");
    }
}
