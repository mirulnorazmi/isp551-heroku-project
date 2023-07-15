package com.heroku.java.STASH;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.MODEL.Accounts;
import com.heroku.java.MODEL.Items;
import com.heroku.java.MODEL.ItemsDry;
import com.heroku.java.MODEL.ItemsFurniture;
import com.heroku.java.MODEL.ItemsWet;

import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.*;
import java.sql.Date;
import java.util.*;

@Controller
public class UpdateItemSupervisorController {
  private final DataSource dataSource;

  @Autowired
  public UpdateItemSupervisorController(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  // @GetMapping("/update-item-supervisor")
  // public String showuUpdateItem(HttpSession session, @RequestParam(name =
  // "itemsid") int id, Model model) {
  // if (session.getAttribute("username") != null) {

  // // return "supervisor/PAGE_ACCOUNT/create-account";
  // } else {
  // System.out.println("No valid session or session...");
  // return "redirect:/";
  // }
  // }

  // @PostMapping("/updateitemsdry")
  // public String updateItemsDry(HttpSession session, @ModelAttribute("items")
  // ItemsDry items,
  // @RequestParam(name = "itemsid") int id, Model model) {
  // if (session.getAttribute("username") != null) {

  // itemDAO.updateItemsDry(items, id);
  // return "redirect:/view?update_success=true";
  // } else {
  // System.out.println("No valid session or session...");
  // return "redirect:/";
  // }
  // }

  // @PostMapping("/updateitemsfurniture")
  // public String updateItemsDry(HttpSession session, @ModelAttribute("items")
  // ItemsFurniture items,
  // @RequestParam(name = "itemsid") int id, Model model) {
  // if (session.getAttribute("username") != null) {
  // try {
  // itemDAO.updateItemsFurniture(items, id);
  // return "redirect:/view?update_success=true";
  // } catch (SQLException sqe) {
  // System.out.println("Error Code = " + sqe.getErrorCode());
  // System.out.println("SQL state = " + sqe.getSQLState());
  // System.out.println("Message = " + sqe.getMessage());
  // sqe.printStackTrace();
  // return "redirect:/update-item-supervisor?success=false";
  // } catch (Exception e) {
  // System.out.println("E message : " + e.getMessage());
  // return "redirect:/update-item-supervisor?success=false";
  // }
  // } else {
  // System.out.println("No valid session or session...");
  // return "redirect:/";
  // }
  // }

  // @PostMapping("/updateitemswet")
  // public String updateItemsDry(HttpSession session, @ModelAttribute("items") ItemsWet items,
  //     @RequestParam(name = "itemsid") int id, Model model) {
  //   if (session.getAttribute("username") != null) {
  //     try {
  //       itemDAO.updateItemsWet(items, id);
  //       return "redirect:/view?update_success=true";
  //     } catch (SQLException sqe) {
  //       System.out.println("Error Code = " + sqe.getErrorCode());
  //       System.out.println("SQL state = " + sqe.getSQLState());
  //       System.out.println("Message = " + sqe.getMessage());
  //       sqe.printStackTrace();
  //       return "redirect:/update-item-supervisor?success=false";
  //     } catch (Exception e) {
  //       System.out.println("E message : " + e.getMessage());
  //       return "redirect:/update-item-supervisor?success=false";
  //     }
  //   } else {
  //     System.out.println("No valid session or session...");
  //     return "redirect:/";
  //   }
  // }
}