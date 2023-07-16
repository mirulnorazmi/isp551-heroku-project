package com.heroku.java.CONTROLLER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.MODEL.Items;
import com.heroku.java.MODEL.Stock;
import com.heroku.java.SERVICES.ItemServices;
import com.heroku.java.SERVICES.QuantityItemServices;

import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Controller
public class RestockItemController {
  private ItemServices itemServices;
  private final DataSource dataSource;
  private QuantityItemServices quantityItemServices;

  // Get the current date
  LocalDate currentDate = LocalDate.now();

  // Convert LocalDate to java.util.Date
  Date utilDate = Date.valueOf(currentDate);

  @Autowired
  public RestockItemController(DataSource dataSource, ItemServices itemServices,
      QuantityItemServices quantityItemServices) {
    this.dataSource = dataSource;
    this.itemServices = itemServices;
    this.quantityItemServices = quantityItemServices;
  }

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @GetMapping("/restockitem")
  public String requestitem(HttpSession session,
      @RequestParam(value = "create_success", defaultValue = "false") boolean createSuccess, Model model)
      throws Exception {
    // if (session.getAttribute("username") != null) {
      ArrayList<Items> itemList = itemServices.getAllItems();
      model.addAttribute("items", itemList);
      return "staff/restockitem";
    // } else {
    //   System.out.println("No valid session or session...");
    //   return "redirect:/";
    // }
  }

  @PostMapping("/restockitem/confirm")
  public String confirmRelease(HttpSession session, @RequestParam(name = "itemsid") int[] itemIds,
      @RequestParam(name = "itemquantity") int[] itemQuantities,
      Model model) {
        
    Stock stocks = new Stock();
    stocks.setStaffid(1);    // dummy staff data
    stocks.setInvdate(utilDate);

    boolean status = false;

    for (int i = 0; i < itemIds.length; i++) {
      System.out.println("ID: " + itemIds[i] + " Quantity: " + itemQuantities[i]);
      stocks.setItemsid(itemIds[i]);
      stocks.setQuantity(itemQuantities[i]);
      status = quantityItemServices.insertStockMovement(stocks);
    }
    System.out.println("status : " + status);

    return "redirect:/restockitem";
  }
}