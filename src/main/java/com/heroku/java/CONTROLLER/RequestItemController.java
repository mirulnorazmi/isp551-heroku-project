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
import com.heroku.java.MODEL.ItemsWet;
import com.heroku.java.SERVICES.ItemServices;

import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Controller
public class RequestItemController {
  private final DataSource dataSource;
  private ItemServices itemServices;

  @Autowired
  public RequestItemController(DataSource dataSource, ItemServices itemServices) {
    this.dataSource = dataSource;
    this.itemServices = itemServices;
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

  
  @PostMapping("/requestItemDry")
  public String createItemDry(@ModelAttribute("requestDry") ItemsDry reqD, HttpSession session) {
    try {
      if (reqD.getName() == null && reqD.getQuantity() == 0 && reqD.getAdded_date() == null
          && reqD.getExpire_date() == null) {
        return "redirect:/create-items/request-item-dry?success=false";
      } else {
        Accounts account = new Accounts();
        account.setStaffid((int) session.getAttribute("staffid"));
        account.setUsername((String) session.getAttribute("username"));

        boolean success = itemServices.createItemDry(reqD, account);
        if (success) {
          return "redirect:/request-items/request-item-dry?success=true";
        } else {
          return "redirect:/request-items/request-item-dry?success=false";
        }
      }
    } catch (Exception e) {
      System.out.println("Exception: " + e.getMessage());
      return "redirect:/request-items/request-item-dry?success=false";
    }
  }

  @PostMapping("/request-item-stuff")
  public String requestItemStuff(@ModelAttribute("requestStuff") ItemsStuff reqS) {
    try {
      if (reqS.getName() == null && reqS.getQuantity() == 0 && reqS.getAdded_date() == null) {
        return "redirect:/create-items/request-item-stuff?success=false";
      } else {
        boolean success = itemServices.requestItemStuff(reqS);
        if (success) {
          return "redirect:/request-items/request-item-stuff?success=true";
        } else {
          return "redirect:/request-items/request-item-stuff?success=false";
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return "redirect:/request-items/request-item-stuff?success=false";
    }
  }
  @PostMapping("/request-item-wet")
  public String createItemWet(@ModelAttribute("requestWet") ItemsWet reqW, HttpSession session) {
    try {
      if (reqW.getName() == null && reqW.getQuantity() == 0 && reqW.getAdded_date() == null) {
        return "redirect:/create-items/create-item-stuff?success=false";
      } else {
        itemServices.insertItemsWet(reqW);

        System.out.println(">>>>Item [" + reqW.getId() + "] created by staff[" + session.getAttribute("staffid") + "] "
            + session.getAttribute("username"));

        return "redirect:/request-items/request-item-wet?success=true";
      }

    } catch (SQLException sqe) {
      System.out.println("Error Code = " + sqe.getErrorCode());
      System.out.println("SQL state = " + sqe.getSQLState());
      System.out.println("Message = " + sqe.getMessage());
      System.out.println("printTrace /n");
      sqe.printStackTrace();

      return "redirect:/request-items/request-item-stuff?success=false";
    }

  }

}