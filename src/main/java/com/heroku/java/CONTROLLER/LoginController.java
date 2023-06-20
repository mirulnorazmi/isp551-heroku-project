package com.heroku.java.CONTROLLER;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.heroku.java.MODEL.Users;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.sql.Connection;
// import java.util.*;

@Controller
public class LoginController {
  
  private final DataSource dataSource;

  @Autowired
  public LoginController(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

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
    System.out.println(">>>>(" + session.getAttribute("staffid") + ")["+ session.getAttribute("username") + "] logged out..." );
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
    try (Connection connection = dataSource.getConnection()) {
      final var statement = connection.createStatement();

      final var resultSet = statement.executeQuery("SELECT staffid, username, password,roles FROM staff");

      String returnPage = "";

      while (resultSet.next()) {
        String username = resultSet.getString("username");
        String pwd = resultSet.getString("password");
        String roles = resultSet.getString("roles");
        int staffid = resultSet.getInt("staffid");
        if (user.getUsername() != "" && user.getPassword() != "") {
          if (username.equals(user.getUsername()) && passwordEncoder.matches(user.getPassword(), pwd)) {
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", roles);
            session.setAttribute("staffid", staffid);
            session.setMaxInactiveInterval(1440 * 60);
            System.out.println(">>>>(" + staffid + ")["+ user.getUsername()+ "] logged in..." );
            returnPage = "redirect:/dashboard";
            break;
          } else {
            returnPage = "redirect:/login?error=true";
          }

        } else {
          returnPage = "redirect:/login?error=true";

        }
      }
      connection.close();
      return returnPage;

    } catch (Throwable t) {
      System.out.println("message : " + t.getMessage());
      return "index";
    }
  }
}
