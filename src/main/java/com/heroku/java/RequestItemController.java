package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.heroku.java.MODEL.ItemsDry;

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

  @GetMapping("/requestitem")
  public String requestitem(HttpSession session) {
    if (session.getAttribute("username") != null) {
      return "staff/requestitem";
      } else {
      System.out.println("No valid session or session...");
      return "redirect:/";
      }
      // return "supervisor/PAGE_CREATE_ITEM/create-item-dry";
    }

     @PostMapping("/requestitem")
  public String requestitem(@ModelAttribute("requestitem") ItemsDry itemDry, HttpSession session) {
    try {
      if (itemDry.getName().equals(null) && itemDry.getQuantity() == 0 && itemDry.getAdded_date().equals(null)
          && itemDry.getExpire_date().equals(null)) {
        return "redirect:/requestitem?success=false";
      } else {
        Connection connection = dataSource.getConnection();
        String sql_items = "INSERT INTO items(name, quantity, added_date, approval) VALUES (?,?,?,?) RETURNING itemsid AS itemsid;";
        final var pstatement1 = connection.prepareStatement(sql_items);
        pstatement1.setString(1, itemDry.getName());
        pstatement1.setInt(2, itemDry.getQuantity());
        pstatement1.setDate(3, itemDry.getAdded_date());
         pstatement1.setString(4, itemDry.getApproval());
        pstatement1.execute();
        ResultSet items_d = pstatement1.getResultSet();
        int items_id = 0;
        if (items_d.next()) {
          items_id = items_d.getInt(1);
        }
 
        System.out.println(">>>>Item [" + items_id + "] created by staff[" + session.getAttribute("staffid") + "] " + session.getAttribute("username"));
        String sql_dry = "INSERT INTO dry_ingredients(itemsid, expire_date) VALUES (?,?)";

        final var pstatement2 = connection.prepareStatement(sql_dry);
        pstatement2.setInt(1, items_id);
        pstatement2.setDate(2, itemDry.getExpire_date());
        pstatement2.executeUpdate();

        return "redirect:/requestitem?success=true";
      }

    } catch (SQLException sqe) {
      System.out.println("Error Code = " + sqe.getErrorCode());
      System.out.println("SQL state = " + sqe.getSQLState());
      System.out.println("Message = " + sqe.getMessage());
      System.out.println("printTrace /n");
      sqe.printStackTrace();

      return "redirect:/requestitem?success=false";
    } catch (Exception e) {
      System.out.println("E message : " + e.getMessage());
      return "redirect:/requestitem?success=false";
    }
  }


}