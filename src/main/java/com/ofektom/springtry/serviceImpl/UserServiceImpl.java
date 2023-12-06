package com.ofektom.springtry.serviceImpl;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.ofektom.springtry.dto.PasswordDto;
import com.ofektom.springtry.models.Users;
import com.ofektom.springtry.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserServiceImpl{
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Function<String, Users> findUserByUsername = (username)->
        userRepository.findByUsername(username)
                .orElseThrow(() -> new NullPointerException("User not found!"));
    public Function<Long, Users> findUserById = (id) -> userRepository.findById(id).orElseThrow(()->new NullPointerException("User not found"));

    public Function<Users, Users> saveUser = (user) -> userRepository.save(user);

    public Function<PasswordDto, Boolean> verifyPassword = passwordDto -> BCrypt.verifyer()
             .verify(passwordDto.getPassword().toCharArray(),
                     passwordDto.getHashPassword()
                             .toCharArray()).verified;
}
