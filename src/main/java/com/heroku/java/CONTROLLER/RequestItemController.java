package com.heroku.java.CONTROLLER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.MODEL.Accounts;
import com.heroku.java.MODEL.Items;
import com.heroku.java.MODEL.ItemsDry;
import com.heroku.java.MODEL.ItemsStuff;

import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Controller
public class RequestItemController {
  private final DataSource dataSource;

  @Autowired
  public RequestItemController(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @GetMapping("/request-items/request-item-dry")
  public String dry(HttpSession session) {
    if (session.getAttribute("username") != null) {
      return "staff/PAGE_REQUEST_ITEM/request-item-dry";
    } else {
      System.out.println("No valid session or session...");
      return "redirect:/";
    }
    // return "staff/PAGE_REQUEST_ITEM/request-item-dry";
  }

  @GetMapping("/request-items/request-item-stuff")
  public String stuff(HttpSession session) {
    if (session.getAttribute("username") != null) {
      return "staff/PAGE_REQUEST_ITEM/request-item-stuff";
    } else {
      System.out.println("No valid session or session...");
      return "redirect:/";
    }
    // return "staff/PAGE_REQUEST_ITEM/request-item-stuff";
  }

  @GetMapping("/request-items/request-item-wet")
  public String wet(HttpSession session) {
    if (session.getAttribute("username") != null) {
      return "staff/PAGE_REQUEST_ITEM/request-item-wet";
    } else {
      System.out.println("No valid session or session...");
      return "redirect:/";
    }
    // return "staff/PAGE_REQUEST_ITEM/request-item-wet";
  }

  

}