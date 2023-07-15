package com.heroku.java.STASH;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.MODEL.Accounts;
import com.heroku.java.MODEL.Items;
import com.heroku.java.MODEL.ItemsDry;
import com.heroku.java.MODEL.ItemsFurniture;
import com.heroku.java.SERVICES.ItemServices;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.apache.logging.log4j.*;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@Controller
public class ViewController {
  private final DataSource dataSource;
  private ItemServices itemServices;

  @Autowired
  public ViewController(DataSource dataSource, ItemServices itemServices) {
    this.dataSource = dataSource;
    this.itemServices = itemServices;
  }

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  // @GetMapping("/view")
  // public String viewAllItems(HttpSession session,
  //     @RequestParam(value = "create_success", defaultValue = "false") boolean createSuccess, Model model)
  //     throws Exception {
  //   if (session.getAttribute("username") != null) {
  //     ArrayList<Items> itemList = itemServices.getAllItems();
  //     model.addAttribute("items", itemList);
  //     return "supervisor/view";
  //   } else {
  //     System.out.println("No valid session or session...");
  //     return "redirect:/";
  //   }

  //   // return "/supervisor/PAGE_ACCOUNT/accounts";
  // }

  // @GetMapping("/view/food")
  // public String viewFood(HttpSession session,
  //     @RequestParam(value = "create_success", defaultValue = "false") boolean createSuccess, Model model) {
  //   if (session.getAttribute("username") != null) {
  //     ArrayList<ItemsDry> itemsfood = itemServices.getAllFood();
  //     model.addAttribute("itemsfood", itemsfood);
  //     return "supervisor/viewfood";
  //     // return "supervisor/PAGE_ACCOUNT/accounts";
  //   } else {
  //     System.out.println("No valid session or session...");
  //     return "redirect:/";
  //   }

  //   // return "/supervisor/PAGE_ACCOUNT/accounts";
  // }

  // @GetMapping("/view/furniture")
  // public String viewFurniture(HttpSession session,
  //     @RequestParam(value = "create_success", defaultValue = "false") boolean createSuccess, Model model) {
  //   if (session.getAttribute("username") != null) {

  //     ArrayList<ItemsFurniture>  itemsfurniture = itemServices.getAllFurniture();
  //     model.addAttribute("itemsfurniture", itemsfurniture);
  //     return "supervisor/viewfurniture";
  //     // return "supervisor/PAGE_ACCOUNT/accounts";
  //   } else {
  //     System.out.println("No valid session or session...");
  //     return "redirect:/";
  //   }

  //   // return "/supervisor/PAGE_ACCOUNT/accounts";
  // }

  // @GetMapping("/deleteItems")
  // public String deleteView(HttpSession session, @ModelAttribute("viewfood") Items items,
  //     @RequestParam(name = "itemsid") int id, Model model) {

  //   if (session.getAttribute("username") != null) {

  //     System.out.println("Items id  (delete): " + id);
  //     boolean status = itemServices.deleteItem(id);
  //     if(status){
  //       return "redirect:/view?delete_success=true";
  //     }else{
  //       return "redirect:/view?delete_success=false";
  //     }
      
  //   } else {
  //     System.out.println("No valid session or session...");
  //     return "redirect:/";
  //   }

  // }
}