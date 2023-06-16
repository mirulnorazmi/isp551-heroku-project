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
import java.util.*;

@Controller
public class ItemController {
    private final DataSource dataSource;

  @Autowired
  public ItemController(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @GetMapping("/create-items/create-item-dry")
  public String dry(HttpSession session) {
    // if (session.getAttribute("username") != null) {
    //   return "supervisor/PAGE_CREATE_ITEM/create-item-wet";
    // } else {
    //   System.out.println("No valid session or session...");
    //   return "redirect:/";
    // }
    return "/supervisor/PAGE_CREATE_ITEM/create-item-dry";
  }

  @GetMapping("/create-items/create-item-stuff")
  public String stuff(HttpSession session) {
    // if (session.getAttribute("username") != null) {
    //   return "supervisor/PAGE_CREATE_ITEM/create-item-wet";
    // } else {
    //   System.out.println("No valid session or session...");
    //   return "redirect:/";
    // }
    return "/supervisor/PAGE_CREATE_ITEM/create-item-stuff";
  }
  
  @GetMapping("/create-items/create-item-wet")
  public String wet(HttpSession session) {
    // if (session.getAttribute("username") != null) {
    //   return "supervisor/PAGE_CREATE_ITEM/create-item-wet";
    // } else {
    //   System.out.println("No valid session or session...");
    //   return "redirect:/";
    // }
    return "/supervisor/PAGE_CREATE_ITEM/create-item-wet";
  }
}
