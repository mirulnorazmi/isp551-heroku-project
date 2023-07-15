package com.heroku.java.CONTROLLER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.heroku.java.MODEL.Accounts;
import com.heroku.java.SERVICES.DashboardServices;

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
  private DashboardServices dashboardServices;

  @Autowired
  public DashboardController(DataSource dataSource, DashboardServices dashboardServices) {
    this.dataSource = dataSource;
    this.dashboardServices = dashboardServices;
  }

  // @Autowired
  // private BCryptPasswordEncoder passwordEncoder;

  @GetMapping("/dashboard")
  public String showDashboard(HttpSession session, Model model) {
    // Check if user is logged in

    if (session.getAttribute("username") != null) {
      
      int row = dashboardServices.getAllRowUser();
      int rowSupervisor = dashboardServices.getRowSupervisor();
      int rowStaff = dashboardServices.getRowStaff();
      // model.addAttribute("accounts", accounts);
      model.addAttribute("rowAccount", row);
      model.addAttribute("rowSupervisor", rowSupervisor);
      model.addAttribute("rowStaff", rowStaff);

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
