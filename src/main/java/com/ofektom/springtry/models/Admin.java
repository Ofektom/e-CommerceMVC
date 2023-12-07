package com.ofektom.springtry.models;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.ofektom.springtry.dto.AdminDTO;
import com.ofektom.springtry.dto.UsersDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private String fullName;

    public Admin(AdminDTO adminDTO){
        this.username = adminDTO.getUsername();
        this.password =  BCrypt.withDefaults()
                .hashToString(12, adminDTO.getPassword().toCharArray());
        this.fullName = adminDTO.getFullName();
    }
}
