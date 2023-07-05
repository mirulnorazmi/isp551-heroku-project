package com.heroku.java.CONTROLLER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import java.sql.Date;
import com.heroku.java.MODEL.ItemsDry;
import com.heroku.java.MODEL.ItemsStuff;
import com.heroku.java.MODEL.ItemsWet;

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
    if (session.getAttribute("username") != null) {
    return "supervisor/PAGE_CREATE_ITEM/create-item-dry";
    } else {
    System.out.println("No valid session or session...");
    return "redirect:/";
    }
    // return "supervisor/PAGE_CREATE_ITEM/create-item-dry";
  }

  @GetMapping("/create-items/create-item-stuff")
  public String stuff(HttpSession session) {
    if (session.getAttribute("username") != null) {
    return "supervisor/PAGE_CREATE_ITEM/create-item-stuff";
    } else {
    System.out.println("No valid session or session...");
    return "redirect:/";
    }
    // return "supervisor/PAGE_CREATE_ITEM/create-item-stuff";
  }

  @GetMapping("/create-items/create-item-wet")
  public String wet(HttpSession session) {
    if (session.getAttribute("username") != null) {
    return "supervisor/PAGE_CREATE_ITEM/create-item-wet";
    } else {
    System.out.println("No valid session or session...");
    return "redirect:/";
    }
    // return "supervisor/PAGE_CREATE_ITEM/create-item-wet";
  }

  @PostMapping("/createItemDry")
  public String createItemDry(@ModelAttribute("createItem") ItemsDry itemD, HttpSession session) {
    try {
      if (itemD.getName().equals(null) && itemD.getQuantity() == 0 && itemD.getAdded_date().equals(null)
          && itemD.getExpire_date().equals(null)) {
        return "redirect:/create-items/create-item-dry?success=false";
      } else {
        Connection connection = dataSource.getConnection();
        String sql_items = "INSERT INTO items(name, quantity, added_date) VALUES (?,?,?) RETURNING itemsid AS itemsid;";
        final var pstatement1 = connection.prepareStatement(sql_items);
        pstatement1.setString(1, itemD.getName());
        pstatement1.setInt(2, itemD.getQuantity());
        pstatement1.setDate(3, itemD.getAdded_date());
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
        pstatement2.setDate(2, itemD.getExpire_date());
        pstatement2.executeUpdate();

        return "redirect:/create-items/create-item-dry?success=true";
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

  @PostMapping("/create-item-stuff")
  public String createItemDry(@ModelAttribute("createStuff") ItemsStuff stuff, HttpSession session) {
    try {
      if (stuff.getName().equals(null) && stuff.getQuantity() == 0 && stuff.getAdded_date().equals(null)) {
        return "redirect:/create-items/create-item-stuff?success=false";
      } else {
        Connection connection = dataSource.getConnection();
        String sql_items = "INSERT INTO items(name, quantity, added_date) VALUES (?,?,?)RETURNING itemsid AS itemsid;";
        final var pstatement1 = connection.prepareStatement(sql_items);
        pstatement1.setString(1, stuff.getName());
        pstatement1.setInt(2, stuff.getQuantity());
        pstatement1.setDate(3, stuff.getAdded_date());
        pstatement1.execute();
        ResultSet items_d = pstatement1.getResultSet();
        int items_id = 0;
        if (items_d.next()) {
          items_id = items_d.getInt(1);
        }

        System.out.println(">>>>Item [" + items_id + "] created by staff[" + session.getAttribute("staffid") + "] " + session.getAttribute("username"));
        String sql_stuff = "INSERT INTO furniture(itemsid, location, warranty) VALUES (?,?,?)";

        final var pstatement2 = connection.prepareStatement(sql_stuff);
        pstatement2.setInt(1, items_id);
        pstatement2.setString(2, stuff.getlocation());
        pstatement2.setString(3, stuff.getWarranty());
        pstatement2.executeUpdate();

        return "redirect:/create-items/create-item-stuff?success=true";
      }

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

  
  @PostMapping("/create-item-wet")
  public String createItemWet(@ModelAttribute("createWet") ItemsWet wet, HttpSession session) {
    try {
      if (wet.getName().equals(null) && wet.getQuantity() == 0 && wet.getAdded_date().equals(null)) {
        return "redirect:/create-items/create-item-stuff?success=false";
      } else {
        Connection connection = dataSource.getConnection();
        String sql_items = "INSERT INTO items(name, quantity, added_date) VALUES (?,?,?)RETURNING itemsid AS itemsid;";
        final var pstatement1 = connection.prepareStatement(sql_items);
        pstatement1.setString(1, wet.getName());
        pstatement1.setInt(2, wet.getQuantity());
        pstatement1.setDate(3, wet.getAdded_date());
        pstatement1.execute();
        ResultSet items_d = pstatement1.getResultSet();
        int items_id = 0;
        if (items_d.next()) {
          items_id = items_d.getInt(1);
        }

        System.out.println(">>>>Item [" + items_id + "] created by staff[" + session.getAttribute("staffid") + "] " + session.getAttribute("username"));
        String sql_stuff = "INSERT INTO wet_ingredients(itemsid) VALUES (?)";

        final var pstatement2 = connection.prepareStatement(sql_stuff);
        pstatement2.setInt(1, items_id);
        pstatement2.executeUpdate();

        return "redirect:/create-items/create-item-wet?success=true";
      }

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
