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
public class UpdateItemController {
      private final DataSource dataSource;

  @Autowired
  public UpdateItemController(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @GetMapping("/updateitem")
  public String updatetitem(HttpSession session) {
    //if (session.getAttribute("username") != null) {
    //return "supervisor/PAGE_ACCOUNT/accounts";
    //} else {
    //System.out.println("No valid session or session...");
    //return "redirect:/";
    //}
     return "staff/updateitem";
  }



}
