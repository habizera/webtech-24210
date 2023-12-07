package com.honore.webtech.controller;

import com.honore.webtech.global.GlobalData;
import com.honore.webtech.model.Role;
import com.honore.webtech.model.User;
import com.honore.webtech.repository.RoleRepository;
import com.honore.webtech.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;





    @GetMapping("/loginPage")
    public String login(HttpServletRequest request) {
        GlobalData.cart.clear();
        final Logger logger = LoggerFactory.getLogger(LoginController.class);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            for (GrantedAuthority authority : auth.getAuthorities()) {
                if (authority.getAuthority().equals("ADMIN")) {
                    System.out.println("User has authority: {}"+ authority.getAuthority());
                    return "redirect:/admin";
                }
            }
        } else {
            return "redirect:/";
        }
        return "login";
    }




    @GetMapping("/logout")
    public String logout(){

        return "login";
    }

    @GetMapping("/register")
    public String registerGet(){

        return "register";
    }

    @GetMapping("/login?expired=true")
    public String LogoutSession (Model mode){
        return "login";
    }
    @GetMapping("/login?invalid=true")
    public String LogoutSessionUrl (Model mode){
        return "login";
    }

    @PostMapping("/register")
    public String registerPost(@ModelAttribute("user") User user, HttpServletRequest request)throws ServletException{
        String password =user.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        List<Role> roles = new ArrayList<>();
//        roles.add(roleRepository.findById(2).get());
        roleRepository.findById(2).ifPresent(role -> roles.add(role));

        user.setRoles(roles);
        userRepository.save(user);
        request.login(user.getEmail(),password);
        return "redirect:/";
    }
}











