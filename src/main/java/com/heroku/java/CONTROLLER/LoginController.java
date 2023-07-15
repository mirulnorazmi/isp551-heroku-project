package com.heroku.java.CONTROLLER;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.heroku.java.MODEL.Users;
import com.heroku.java.SERVICES.AccountServices;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.sql.Connection;
// import java.util.*;

@Controller
public class LoginController {

  private final DataSource dataSource;
  private AccountServices accountServices;

  @Autowired
  public LoginController(DataSource dataSource, AccountServices accountServices) {
    this.dataSource = dataSource;
    this.accountServices = accountServices;
  }

  @GetMapping("/")
  public String index(HttpSession session) {
    if (session.getAttribute("username") != null) {
      return "redirect:/dashboard";
    } else {
      System.out.println("Session expired or invalid...");
      return "index";
    }
  }

  @GetMapping("/logout")
  public String logout(HttpSession session) {
    System.out.println(
        ">>>>(" + session.getAttribute("staffid") + ")[" + session.getAttribute("username") + "] logged out...");
    session.invalidate();
    return "redirect:/";
  }

  @GetMapping("/login")
  public String displayLogin(HttpSession session,
      @RequestParam(value = "error", defaultValue = "false") boolean loginError) {
    if (session.getAttribute("username") != null) {
      return "redirect:/dashboard";
    } else {
      System.out.println("Session expired or invalid...");
      return "index";
    }
  }

  @PostMapping("/login")
  String homepage(HttpSession session, @ModelAttribute("user") Users user,
      @RequestParam(value = "error", defaultValue = "false") boolean loginError, Model model) {
    System.out.println("Login Error PAram : " + loginError);
    String url = accountServices.loginHandler(user);
    return url;
  }
}
