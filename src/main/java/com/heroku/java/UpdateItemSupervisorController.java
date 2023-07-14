package com.heroku.java;

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

  @GetMapping("/update-item-supervisor")
  public String showuUpdateItem(HttpSession session, @RequestParam(name = "itemsid") int id, Model model) {
    if (session.getAttribute("username") != null) {
      try {
        Connection connection = dataSource.getConnection();

        String sql = "SELECT * FROM items i LEFT OUTER JOIN dry_ingredients d ON (i.itemsid = d.itemsid) " +
            "LEFT JOIN wet_ingredients w ON (i.itemsid = w.itemsid) " +
            "LEFT JOIN furniture fi ON (i.itemsid = fi.itemsid) WHERE i.itemsid = ?;";
        final var statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        final var resultSet = statement.executeQuery();

        while (resultSet.next()) {
          String category = "";
          int itemsid_i = resultSet.getInt("itemsid");
          String name = resultSet.getString("name");
          int quantity = resultSet.getInt("quantity");
          String status = resultSet.getString("status");
          String approval = resultSet.getString("approval");
          Date added_date = resultSet.getDate("added_date");
          Date expire_date = resultSet.getDate("expire_date");
          String location = resultSet.getString("location");
          String warranty = resultSet.getString("warranty");
          System.out.println("status : " + resultSet.next());
          if (expire_date != null) {
            category = "Dry Food";
            ItemsDry itemsdry = new ItemsDry(itemsid_i, name, quantity, status, approval, added_date, category,
                itemsid_i, expire_date);
            model.addAttribute("items", itemsdry);
          } else if (location != null) {
            category = "Furniture";
            ItemsFurniture itemsFurniture = new ItemsFurniture(itemsid_i, name, quantity, status, approval, added_date,
                category, itemsid_i, location, warranty);
            model.addAttribute("items", itemsFurniture);
          } else {
            category = "Wet Food";
            Items itemswet = new Items(itemsid_i, name, quantity, status, approval, added_date, category);
            model.addAttribute("items", itemswet);
          }
        }
         return "supervisor/update-item-supervisor";
      } catch (Throwable t) {
        System.out.println("message : " + t.getMessage());
        return "index";
      }
      // return "supervisor/PAGE_ACCOUNT/create-account";
    } else {
      System.out.println("No valid session or session...");
      return "redirect:/";
    }
  }


@PostMapping("/updateitemsdry")
public String updateItemsDry(HttpSession session, @ModelAttribute("items") ItemsDry items, 
    @RequestParam(name = "itemsid") int id, Model model) {
    if (session.getAttribute("username") != null) {
        try {
            Connection connection = dataSource.getConnection();
            String sqlupdate1 = "UPDATE items SET name=?, quantity=?, added_date=? WHERE itemsid = ?";
            String sqlupdate2 = "UPDATE dry_ingredients SET expire_date=? WHERE itemsid = ?";
            String category = "";
            
            String name = items.getName();
            int quantity = items.getQuantity();
            Date added_date = items.getAdded_date();
            Date expire_date = items.getExpire_date();

                PreparedStatement statement1 = connection.prepareStatement(sqlupdate1);
                PreparedStatement statement2 = connection.prepareStatement(sqlupdate2);

                statement2.setDate(1, expire_date);
                statement2.setInt(2, id);
                statement2.executeUpdate();

                statement1.setString(1, name);
                statement1.setInt(2, quantity);
                statement1.setDate(3, added_date);
                statement1.setInt(4, id);
                statement1.executeUpdate();
            return "redirect:/view?update_success=true";
        } catch (SQLException sqe) {
            System.out.println("Error Code = " + sqe.getErrorCode());
            System.out.println("SQL state = " + sqe.getSQLState());
            System.out.println("Message = " + sqe.getMessage());
            sqe.printStackTrace();
            return "redirect:/update-item-supervisor?success=false";
        } catch (Exception e) {
            System.out.println("E message : " + e.getMessage());
            return "redirect:/update-item-supervisor?success=false";
        }
    } else {
        System.out.println("No valid session or session...");
        return "redirect:/";
    }
  }

@PostMapping("/updateitemsfurniture")
public String updateItemsDry(HttpSession session, @ModelAttribute("items") ItemsFurniture items, 
    @RequestParam(name = "itemsid") int id, Model model) {
    if (session.getAttribute("username") != null) {
        try {
            Connection connection = dataSource.getConnection();
            String sqlupdate1 = "UPDATE items SET name=?, quantity=?, added_date=? WHERE itemsid = ?";
            String sqlupdate2 = "UPDATE furniture SET location=?, warranty=? WHERE itemsid = ?";
            
            String name = items.getName();
            int quantity = items.getQuantity();
            Date added_date = items.getAdded_date();
            String location = items.getLocation();
            String warranty = items.getWarranty();

                PreparedStatement statement1 = connection.prepareStatement(sqlupdate1);
                PreparedStatement statement2 = connection.prepareStatement(sqlupdate2);

                statement2.setString(1, location);
                statement2.setString(2, warranty);
                statement2.setInt(3, id);
                statement2.executeUpdate();

                statement1.setString(1, name);
                statement1.setInt(2, quantity);
                statement1.setDate(3, added_date);
                statement1.setInt(4, id);
                statement1.executeUpdate();

            return "redirect:/view?update_success=true";
        } catch (SQLException sqe) {
            System.out.println("Error Code = " + sqe.getErrorCode());
            System.out.println("SQL state = " + sqe.getSQLState());
            System.out.println("Message = " + sqe.getMessage());
            sqe.printStackTrace();
            return "redirect:/update-item-supervisor?success=false";
        } catch (Exception e) {
            System.out.println("E message : " + e.getMessage());
            return "redirect:/update-item-supervisor?success=false";
        }
    } else {
        System.out.println("No valid session or session...");
        return "redirect:/";
    }
  }

  @PostMapping("/updateitemswet")
public String updateItemsDry(HttpSession session, @ModelAttribute("items") ItemsWet items, 
    @RequestParam(name = "itemsid") int id, Model model) {
    if (session.getAttribute("username") != null) {
        try {
            Connection connection = dataSource.getConnection();
            String sqlupdate1 = "UPDATE items SET name=?, quantity=?, added_date=? WHERE itemsid = ?";
            
            String name = items.getName();
            int quantity = items.getQuantity();
            Date added_date = items.getAdded_date();

                PreparedStatement statement1 = connection.prepareStatement(sqlupdate1);

                statement1.setString(1, name);
                statement1.setInt(2, quantity);
                statement1.setDate(3, added_date);
                statement1.setInt(4, id);
                statement1.executeUpdate();

            return "redirect:/view?update_success=true";
        } catch (SQLException sqe) {
            System.out.println("Error Code = " + sqe.getErrorCode());
            System.out.println("SQL state = " + sqe.getSQLState());
            System.out.println("Message = " + sqe.getMessage());
            sqe.printStackTrace();
            return "redirect:/update-item-supervisor?success=false";
        } catch (Exception e) {
            System.out.println("E message : " + e.getMessage());
            return "redirect:/update-item-supervisor?success=false";
        }
    } else {
        System.out.println("No valid session or session...");
        return "redirect:/";
    }
  }
}