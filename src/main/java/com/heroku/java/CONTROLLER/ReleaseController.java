package com.heroku.java.CONTROLLER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.MODEL.Items;
import com.heroku.java.SERVICES.ItemServices;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.apache.logging.log4j.*;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ReleaseController {
  private ItemServices itemServices;

  @Autowired
  public ReleaseController(ItemServices itemServices) {
    this.itemServices = itemServices;
  }

  @GetMapping("/release-item")
  public String release(HttpSession session,
      @RequestParam(value = "create_success", defaultValue = "false") boolean createSuccess, Model model)
      throws Exception {
    if (session.getAttribute("username") != null) {
      ArrayList<Items> itemList = itemServices.getAllItems();
      model.addAttribute("items", itemList);
      return "supervisor/PAGE_RELEASE_ITEM/release-item";
    } else {
      System.out.println("No valid session or session...");
      return "redirect:/";
    }
  }

  @PostMapping("/release-item/confirm")
  public String confirmRelease(@RequestParam(name = "itemsid") int[] itemIds,
  @RequestParam(name = "itemquantity") int[] itemQuantities,
   Model model) {
    for (int i = 0; i < itemIds.length; i++) {
      System.out.println("ID: " + itemIds[i] + " Quantity: " + itemQuantities[i]);
  }
    return "redirect:/release-item";
  }
}