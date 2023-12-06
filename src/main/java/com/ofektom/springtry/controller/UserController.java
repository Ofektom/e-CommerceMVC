package com.ofektom.springtry.controller;

import com.ofektom.springtry.dto.PasswordDto;
import com.ofektom.springtry.dto.UserDto;
import com.ofektom.springtry.models.Users;
import com.ofektom.springtry.serviceImpl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {
    private UserServiceImpl usersService;

    @Autowired
    public UserController(UserServiceImpl usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/register")
    public String indexPage(Model model){
        model.addAttribute("user", new UserDto());
        return "sign-up";
    }

    @GetMapping("/login")
    public ModelAndView loginPage(){
        return  new ModelAndView("login")
                .addObject("user", new UserDto());
    }

    @PostMapping("/sign-up")
    public String signUp(@ModelAttribute UserDto usersDTO){
        Users user = usersService.saveUser.apply(new Users(usersDTO));
        log.info("User details ---> {}", user);
        return "successful-register";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute UserDto usersDTO, HttpServletRequest request, Model model){
        Users user = usersService.findUserByUsername.apply(usersDTO.getUsername());
        log.info("User details ---> {}", user);
        if (usersService.verifyPassword
                .apply(PasswordDto.builder()
                        .password(usersDTO.getPassword())
                        .hashPassword(user.getPassword())
                        .build())){
            HttpSession session = request.getSession();
            session.setAttribute("userID", user.getId());
            return "redirect:/products/all";
        }
        return "redirect:/user/login";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "index";
    }
}
