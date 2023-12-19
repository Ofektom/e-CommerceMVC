package com.ofektom.springtry.controller;

import com.ofektom.springtry.dto.UsersDTO;
import com.ofektom.springtry.dto.PasswordDTO;
import com.ofektom.springtry.models.Users;
import com.ofektom.springtry.serviceImpl.UsersServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
@Slf4j
public class UsersController {
    private UsersServiceImpl usersService;

    @Autowired
    public UsersController(UsersServiceImpl usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/register")
    public String indexPage(Model model){
        model.addAttribute("user", new UsersDTO());
        return "sign-up";
    }

    @GetMapping("/login")
    public ModelAndView loginPage(){
        return  new ModelAndView("login")
                .addObject("user", new UsersDTO());
    }

    @PostMapping("/sign-up")
    public String signUp(@ModelAttribute UsersDTO usersDTO){
        Users user = usersService.saveUser.apply(new Users(usersDTO));
        log.info("User details ---> {}", user);
        return "successful-register";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute UsersDTO usersDTO, HttpServletRequest request){
        Users user = usersService.findUsersByUsername.apply(usersDTO.getUsername());
        log.info("User details ---> {}", user);
        if (usersService.verifyUserPassword
                .apply(PasswordDTO.builder()
                        .password(usersDTO.getPassword())
                        .hashPassword(user.getPassword())
                        .build())){
            HttpSession session = request.getSession();
            session.setAttribute("userID", user.getId());
            return "dashboard";
        }
        return "redirect:/user/login";
    }

//    @PostMapping("/login")
//    public String loginUser(@ModelAttribute UsersDTO usersDTO, @RequestParam(name = "cart")String something, HttpServletRequest request){
//        Users user = usersService.findUsersByUsername.apply(usersDTO.getUsername());
//        log.info("User details ---> {}", user);
//        if (usersService.verifyUserPassword
//                .apply(PasswordDTO.builder()
//                        .password(usersDTO.getPassword())
//                        .hashPassword(user.getPassword())
//                        .build())){
//            HttpSession session = request.getSession();
//            session.setAttribute("userID", user.getId());
//            if (something!=null){
//                return "redirect:/products/all-cart";
//            }
//            throw new NullPointerException("No such product found with ID: " );
//        }
//        return "redirect:/user/login";
//    }


    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "index";
    }

}
