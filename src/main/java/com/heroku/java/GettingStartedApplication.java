package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.apache.logging.log4j.*;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

@SpringBootApplication
@Controller
public class GettingStartedApplication {
  private final DataSource dataSource;
  // Object logger
  Logger logger = LogManager.getLogger(GettingStartedApplication.class);

  @Autowired
  public GettingStartedApplication(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @GetMapping("/")
  public String index() {
    return "index";
  }

  @GetMapping("/logout")
  public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/";
  }

  @GetMapping("/homepage")
  public String homepageForm(Model model) {
  model.addAttribute("staff", new Staff());
  return "homepage";
  }

  // @PostMapping("/homepage")
  // public String homepageSubmit(@ModelAttribute Staff staff, Model model) {
  // model.addAttribute("staff", staff);
  // System.out.println("Staff data-------- : " + staff);
  // return "homepage";
  // }

  @PostMapping("/homepage")
  // String homepage(@RequestParam("username") String userName,
  // @RequestParam("password") String password, @ModelAttribute Staff staff, Model
  // model) {
  String homepage(HttpSession session, @ModelAttribute("user") Staff staff, Model model) {
    try (Connection connection = dataSource.getConnection()) {
      final var statement = connection.createStatement();

      final var resultSet = statement.executeQuery("SELECT username, password,roles FROM staff");
      
      String returnPage = "";

      while (resultSet.next()) {
        String username = resultSet.getString("username");
        String pwd = resultSet.getString("password");
        String roles = resultSet.getString("roles");

        if (username.equals(staff.getUsername()) && pwd.equals(staff.getPassword())) {
          // output.add("Username : " + username + "<br>roles : " + roles);
          model.addAttribute("staff", staff);
          
          session.setAttribute("user", staff);
          returnPage = "homepage";
          break;
        } else {
          returnPage = "redirect:/";
        }
      }

      logger.info("Successfully login!");
      return returnPage;

    } catch (Throwable t) {
      System.out.println("message : " + t.getMessage());
      return "error";
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

  public static void main(String[] args) {
    SpringApplication.run(GettingStartedApplication.class, args);
  }
}
