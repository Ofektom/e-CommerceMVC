package com.ofektom.springtry.models;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.ofektom.springtry.dto.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;
    private String imageUrl;
    private String fullName;
    private BigDecimal balance;

    public Users(UserDto userDto) {
        this.username = userDto.getUsername();
        this.password = BCrypt.withDefaults().hashToString(12, userDto.getPassword().toCharArray());
        this.fullName = userDto.getFullName();
        this.balance = new BigDecimal(50000000);
    }
}
