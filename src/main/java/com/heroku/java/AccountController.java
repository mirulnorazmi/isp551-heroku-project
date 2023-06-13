package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.*;
import java.sql.Connection;
import java.util.*;

@Controller
public class AccountController {
  private final DataSource dataSource;

  @Autowired
  public AccountController(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @GetMapping("/accounts")
  public String showAccounts(HttpSession session) {
    if (session.getAttribute("username") != null) {
      return "supervisor/PAGE_ACCOUNT/accounts";
    } else {
      System.out.println("No valid session or session...");
      return "redirect:/";
    }
    // return "/supervisor/PAGE_ACCOUNT/accounts";
  }

  @GetMapping("/accounts/create-account")
  public String showSignUp(HttpSession session) {
    if (session.getAttribute("username") != null) {
      return "supervisor/PAGE_ACCOUNT/create-account";
    } else {
      System.out.println("No valid session or session...");
      return "redirect:/";
    }
    // return "/supervisor/PAGE_ACCOUNT/create-account";
  }

  @GetMapping("/accounts/update-account")
  public String showUpdateAccount(HttpSession session) {
    // if (session.getAttribute("username") != null) {
    // return "redirect:/dashboard";
    // } else {
    // System.out.println("No valid session or session...");
    // return "/supervisor/PAGE_ACCOUNT/create-account";
    // }
    return "supervisor/PAGE_ACCOUNT/update-account";
  }

  @PostMapping("/addAccount")
  public String addAccount(HttpSession session, @ModelAttribute("account") Accounts acc, Model model) {
    try {
      Connection connection = dataSource.getConnection();
      String sql = "INSERT INTO staff(fullname, username, password,roles,managerid) VALUES (?,?,?,?,?)";
      final var statement = connection.prepareStatement(sql);

      statement.setString(1, acc.getFullname());
      statement.setString(2, acc.getUsername());
      statement.setString(3, passwordEncoder.encode(acc.getPassword()));
      statement.setString(4, acc.getRoles());
      statement.setInt(5, (int) session.getAttribute("staffid"));
      // statement.setInt(5, 1);
      statement.executeUpdate();

      connection.close();

      return "redirect:/accounts";

    } catch (SQLException sqe) {
      System.out.println("Error Code = " + sqe.getErrorCode());
      System.out.println("SQL state = " + sqe.getSQLState());
      System.out.println("Message = " + sqe.getMessage());
      System.out.println("printTrace /n");
      sqe.printStackTrace();

      return "redirect:/";
    } catch (Exception e) {
      System.out.println("E message : " + e.getMessage());
      return "redirect:/";
    }

  }

}
