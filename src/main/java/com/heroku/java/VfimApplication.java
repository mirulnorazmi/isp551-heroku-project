package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.apache.logging.log4j.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

@SpringBootApplication
@Controller
public class VfimApplication {
  private final DataSource dataSource;
  // Object logger
  Logger logger = LogManager.getLogger(VfimApplication.class);

  @Autowired
  public VfimApplication(DataSource dataSource) {
    this.dataSource = dataSource;
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
    session.invalidate();
    return "redirect:/";
  }

  @PostMapping("/login")
  String homepage(HttpSession session, @ModelAttribute("user") Users user, Model model) {
    try (Connection connection = dataSource.getConnection()) {
      final var statement = connection.createStatement();

      final var resultSet = statement.executeQuery("SELECT staffid, username, password,roles FROM staff");

      String returnPage = "";

      while (resultSet.next()) {
        String username = resultSet.getString("username");
        String pwd = resultSet.getString("password");
        String roles = resultSet.getString("roles");
        int staffid = resultSet.getInt("staffid");
        
        if (username.equals(user.getUsername()) && bCryptPasswordEncoder().matches(user.getPassword(), pwd)) {

          session.setAttribute("username", user.getUsername());
          session.setAttribute("role", roles);
          session.setAttribute("staffid", staffid);
          session.setMaxInactiveInterval(1440 * 60);
          returnPage = "redirect:/dashboard";
          break;
        } else {
          returnPage = "index";
        }
      }
      return returnPage;

    } catch (Throwable t) {
      System.out.println("message : " + t.getMessage());
      return "index";
    }
  }

  @GetMapping("/dashboard")
  public String showDashboard(HttpSession session) {
    // Check if user is logged in
    if (session.getAttribute("username") != null) {
      if (session.getAttribute("role").equals("supervisor")) {
        return "supervisor/dashboard";
      } else {
        return "staff/dashboard";
      }
    } else {
      System.out.println("Session expired or invalid...");
      return "redirect:/";
    }

  }

  @GetMapping("/database")
  String database(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      final var statement = connection.createStatement();
      statement.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      statement.executeUpdate("INSERT INTO ticks VALUES (now())");

      final var resultSet = statement.executeQuery("SELECT tick FROM ticks");
      final var output = new ArrayList<>();
      while (resultSet.next()) {
        output.add("Read from DB: " + resultSet.getTimestamp("tick"));
      }

      model.put("records", output);
      return "database";

    } catch (Throwable t) {
      model.put("message", t.getMessage());
      return "error";
    }
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  public static void main(String[] args) {
    SpringApplication.run(VfimApplication.class, args);
  }
}
