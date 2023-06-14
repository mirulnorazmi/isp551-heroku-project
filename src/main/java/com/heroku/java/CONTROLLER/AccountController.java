package com.heroku.java.CONTROLLER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.servlet.ModelAndView;

import com.heroku.java.MODEL.Accounts;
import com.heroku.java.MODEL.AccountsRegister;

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
  public String showAccounts(HttpSession session,
      @RequestParam(value = "success", defaultValue = "false") boolean loginError, Model model) {
    // if (session.getAttribute("username") != null) {
    // return "supervisor/PAGE_ACCOUNT/accounts";
    // } else {
    // System.out.println("No valid session or session...");
    // return "redirect:/";
    // }
    try (Connection connection = dataSource.getConnection()) {
      final var statement = connection.createStatement();

      final var resultSet = statement.executeQuery("SELECT staffid, fullname, username, roles FROM staff");

      // String returnPage = "";

      // int columnCount = resultSet.getMetaData().getColumnCount();

      ArrayList<Accounts> accounts = new ArrayList<>();
      while (resultSet.next()) {
        int staffid = resultSet.getInt("staffid");
        String fullname = resultSet.getString("fullname");
        String username = resultSet.getString("username");
        String roles = resultSet.getString("roles");

        accounts.add(new Accounts(staffid, fullname, username, roles));
      }
      model.addAttribute("accounts", accounts);
      System.out.println("Account mode : " + model.getAttribute("accounts").toString());
      connection.close();
      return "supervisor/PAGE_ACCOUNT/accounts";
      // return returnPage;

    } catch (Throwable t) {
      System.out.println("message : " + t.getMessage());
      return "index";
    }
    // return "/supervisor/PAGE_ACCOUNT/accounts";
  }

  @GetMapping("/accounts/create-account")
  public String showSignUp(HttpSession session,
      @RequestParam(value = "success", defaultValue = "false") boolean loginError) {
    // if (session.getAttribute("username") != null) {
    // return "supervisor/PAGE_ACCOUNT/create-account";
    // } else {
    // System.out.println("No valid session or session...");
    // return "redirect:/";
    // }
    return "supervisor/PAGE_ACCOUNT/create-account";
  }

  @GetMapping("/accounts/update-account")
  public String showUpdateAccount(HttpSession session) {
    // if (session.getAttribute("username") != null) {
    // return "redirect:/dashboard";
    // } else {
    // System.out.println("No valid session or session...");
    // return "supervisor/PAGE_ACCOUNT/create-account";
    // }
    return "supervisor/PAGE_ACCOUNT/update-account";
  }

  @PostMapping("/addAccount")
  public String addAccount(HttpSession session, @ModelAttribute("account") AccountsRegister acc, Model model) {
    try {
      Connection connection = dataSource.getConnection();
      String sql = "INSERT INTO staff(fullname, username, password,roles,managerid) VALUES (?,?,?,?,?)";
      final var statement = connection.prepareStatement(sql);

      String fullname = acc.getFullname();
      String username = acc.getUsername();
      String password = acc.getPassword();
      String roles = acc.getRoles();

      if (fullname.equals("") && username.equals("") && password.equals("")) {
        connection.close();
        System.out.println("First failed..");
        return "redirect:/accounts/create-account?success=false";
      } else {
        statement.setString(1, fullname);
        statement.setString(2, username);
        statement.setString(3, passwordEncoder.encode(password));
        statement.setString(4, roles);
        statement.setInt(5, (int) session.getAttribute("staffid"));
        // statement.setInt(5, 1);
        statement.executeUpdate();

        connection.close();

        return "redirect:/accounts?success=true";
      }

    } catch (SQLException sqe) {
      System.out.println("Error Code = " + sqe.getErrorCode());
      System.out.println("SQL state = " + sqe.getSQLState());
      System.out.println("Message = " + sqe.getMessage());
      System.out.println("printTrace /n");
      sqe.printStackTrace();

      return "redirect:/accounts/create-account?success=false";
    } catch (Exception e) {
      System.out.println("E message : " + e.getMessage());
      return "redirect:/accounts/create-account?success=false";
    }

  }

}
