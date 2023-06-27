package com.heroku.java.CONTROLLER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.heroku.java.MODEL.Accounts;

// import com.heroku.java.MODEL.Users;

// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.apache.logging.log4j.*;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.sql.Connection;
import java.util.ArrayList;
// import java.util.Map;

@Controller
public class DashboardController {
  
  private final DataSource dataSource;

  @Autowired
  public DashboardController(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  // @Autowired
  // private BCryptPasswordEncoder passwordEncoder;

  @GetMapping("/dashboard")
  public String showDashboard(HttpSession session, Model model) {
    // Check if user is logged in

    if (session.getAttribute("username") != null) {

      try (Connection connection = dataSource.getConnection()) {
        final var statement = connection.createStatement();
  
        final var resultSet = statement.executeQuery("SELECT staffid, fullname, username, password, roles FROM staff");
  
        // String returnPage = "";
  
        // int columnCount = resultSet.getMetaData().getColumnCount();
        int row = 0 , rowSupervisor = 0, rowStaff = 0;
        ArrayList<Accounts> accounts = new ArrayList<>();
        while (resultSet.next()) {
          int staffid = resultSet.getInt("staffid");
          String fullname = resultSet.getString("fullname");
          String username = resultSet.getString("username");
          String password = resultSet.getString("password");
          String roles = resultSet.getString("roles");
  
          accounts.add(new Accounts(staffid, fullname, username, password, roles));
          row++;
          if(roles.equals("supervisor")){
            rowSupervisor++;
          }else if(roles.equals("staff")){
            rowStaff++;
          }
        }
        // System.out.println("GSON: " + new Gson().toJson(row));
        model.addAttribute("accounts", accounts);
        model.addAttribute("rowAccount", row);
        model.addAttribute("rowSupervisor", rowSupervisor);
        model.addAttribute("rowStaff", rowStaff);
      
        connection.close();
        // return "/supervisor/PAGE_ACCOUNT/accounts";
        // return returnPage;
  
      } catch (Throwable t) {
        System.out.println("message : " + t.getMessage());
        // return "index";
      }

      if (session.getAttribute("role").equals("supervisor")) {
        return "supervisor/dashboard";
      } else {
        return "staff/dashboard";
      }
    } else {
      System.out.println("Session expired or invalid...");
      return "redirect:/";
    }
    // return "supervisor/dashboard";
  }
  
}
