package com.ofektom.springtry.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.ofektom.springtry.models.Users;
import com.ofektom.springtry.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Component
public class CSVUtils {
    private UserRepository userRepository;

    @Autowired
    public CSVUtils(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @PostConstruct
    public void readUserCSV(){
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/mac/Desktop/springTry/src/main/java/com/ofektom/springtry/users.csv"))){
            String line;
            boolean lineOne = false;
            while ((line=bufferedReader.readLine())!=null){
                String[]user = line.split(",");
                if(lineOne) {
                    Users user1 = Users.builder().fullName(user[1]).imageUrl(user[3])
                            .password(BCrypt.withDefaults().hashToString(12, user[2].trim().toCharArray()))
                            .username(user[0])
                            .build();
                    userRepository.save(user1);
                }
                lineOne = true;
                }
            }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
