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

import com.heroku.java.MODEL.ItemsDry;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.apache.logging.log4j.*;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;

@Controller
public class ViewControllerFood {
  private final DataSource dataSource;

  @Autowired
  public ViewControllerFood(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

 @GetMapping("/view/food")
  public String showSignUp(HttpSession session,
      @RequestParam(value = "create_success", defaultValue = "false") boolean createSuccess, Model model) {
    if (session.getAttribute("username") != null) {
      try (Connection connection = dataSource.getConnection()) {
        final var statement = connection.createStatement();

        final var resultSet = statement.executeQuery("SELECT * FROM items i LEFT JOIN dry_ingredients di ON (i.itemsid = di.itemsid) LEFT JOIN wet_ingredients wi ON (i.itemsid = wi.itemsid) LEFT JOIN furniture fi ON (i.itemsid = fi.itemsid) WHERE i.itemsid NOT IN (SELECT itemsid FROM furniture) ORDER BY i.itemsid");

        int row = 0;
        ArrayList<ItemsDry> itemsfood = new ArrayList<>();
        while (resultSet.next()) {
          String category = "";
          int itemsid_i = resultSet.getInt("itemsid");
          String name = resultSet.getString("name");
          int quantity = resultSet.getInt("quantity");
          String status = resultSet.getString("status");
          String approval = resultSet.getString("approval");
          Date added_date = resultSet.getDate("added_date");
          int itemsid_di = resultSet.getInt("itemsid");
          Date expire_date = resultSet.getDate("expire_date");
          if(expire_date != null){
            category = "Dry Food";
          }
          else{
            category = "Wet Food";
          }
          row++;
          itemsfood.add(new ItemsDry(itemsid_i, name, quantity, status, approval, added_date, category, itemsid_di, expire_date));
        }
        model.addAttribute("itemsfood", itemsfood);
        connection.close();
        return "supervisor/viewfood";

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