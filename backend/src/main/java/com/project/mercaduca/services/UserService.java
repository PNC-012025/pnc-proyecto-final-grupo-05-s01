package com.project.mercaduca.services;

import com.project.mercaduca.dtos.UserUpdateDTO;
import com.project.mercaduca.models.User;
import com.project.mercaduca.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void updateProfile(UserUpdateDTO userUpdateDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByMail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setName(userUpdateDTO.getName());
        user.setLastName(userUpdateDTO.getLastName());
        user.setFaculty(userUpdateDTO.getFaculty());
        user.setMajor(userUpdateDTO.getMajor());

        userRepository.save(user);
    }

}
