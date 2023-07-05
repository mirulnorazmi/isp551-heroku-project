package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.MODEL.ItemsFurniture;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.apache.logging.log4j.*;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;

@Controller
public class ViewControllerFurniture {
  private final DataSource dataSource;

  @Autowired
  public ViewControllerFurniture(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

 @GetMapping("/view/furniture")
  public String showSignUp(HttpSession session,
      @RequestParam(value = "create_success", defaultValue = "false") boolean createSuccess, Model model) {
    if (session.getAttribute("username") != null) {
      try (Connection connection = dataSource.getConnection()) {
        final var statement = connection.createStatement();

        final var resultSet = statement.executeQuery("SELECT * FROM items i JOIN furniture fi ON (i.itemsid = fi.itemsid) ORDER BY i.itemsid");

        int row = 0;
        ArrayList<ItemsFurniture> itemsfurniture = new ArrayList<>();
        while (resultSet.next()) {
          String category = "";
          int itemsid_i = resultSet.getInt("itemsid");
          String name = resultSet.getString("name");
          int quantity = resultSet.getInt("quantity");
          String status = resultSet.getString("status");
          String approval = resultSet.getString("approval");
          Date added_date = resultSet.getDate("added_date");
          int itemsid_fi = resultSet.getInt("itemsid");
          String location = resultSet.getString("location");
          String warranty = resultSet.getString("warranty");
          if(location != null){
            category = "Furniture";
          }
          row++;
          itemsfurniture.add(new ItemsFurniture(itemsid_i, name, quantity, status, approval, added_date, category, itemsid_fi, location, warranty));
        }
        model.addAttribute("itemsfurniture", itemsfurniture);
        connection.close();
        return "supervisor/viewfurniture";

      } catch (Throwable t) {
        System.out.println("message : " + t.getMessage());
        return "index";
      }
      // return "supervisor/PAGE_ACCOUNT/accounts";
    } else {
      System.out.println("No valid session or session...");
      return "redirect:/";
    }

    // return "/supervisor/PAGE_ACCOUNT/accounts";
  }
 
}