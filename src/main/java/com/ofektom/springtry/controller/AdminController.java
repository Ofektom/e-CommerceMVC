package com.ofektom.springtry.controller;

import com.ofektom.springtry.dto.AdminDTO;
import com.ofektom.springtry.dto.PasswordDTO;
import com.ofektom.springtry.dto.UsersDTO;
import com.ofektom.springtry.models.Admin;
import com.ofektom.springtry.models.Users;
import com.ofektom.springtry.serviceImpl.AdminServiceImpl;
import com.ofektom.springtry.serviceImpl.UsersServiceImpl;
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

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private AdminServiceImpl adminService;

    @Autowired
    public AdminController(AdminServiceImpl adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/register")
    public String indexPage(Model model){
        model.addAttribute("admin", new AdminDTO());
        return "admin-signup";
    }

    @GetMapping("/admin-login")
    public ModelAndView loginPage(){
        return  new ModelAndView("admin-login")
                .addObject("admin", new AdminDTO());
    }

    @PostMapping("/admin-signup")
    public String signUp(@ModelAttribute AdminDTO adminDTO){
        Admin admin = adminService.saveAdmin.apply(new Admin(adminDTO));
        log.info("Admin details ---> {}", admin);
        return "successful-register";
    }

    @PostMapping("/admin-login")
    public String loginUser(@ModelAttribute AdminDTO adminDTO, HttpServletRequest request, Model model){
        Admin admin = adminService.findAdminByUsername.apply(adminDTO.getUsername());
        log.info("Admin details ---> {}", admin);
        if (adminService.verifyUserPassword
                .apply(PasswordDTO.builder()
                        .password(adminDTO.getPassword())
                        .hashPassword(admin.getPassword())
                        .build())){
            HttpSession session = request.getSession();
            session.setAttribute("userID", admin.getId());
            return "redirect:/products/all";
        }
        return "redirect:/admin/admin-login";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "index";
    }

}
