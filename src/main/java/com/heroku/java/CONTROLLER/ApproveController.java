package com.heroku.java.CONTROLLER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.MODEL.Items;
import com.heroku.java.SERVICES.ItemServices;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.apache.logging.log4j.*;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ApproveController {

  private ItemServices itemServices;

  @Autowired
  public ApproveController(ItemServices itemServices) {

    this.itemServices = itemServices;
  }

  @GetMapping("/approve/approveitems")
  public String showApproveitems(HttpSession session, Model model) {
    // if (session.getAttribute("username") != null) {
    // return "redirect:/dashboard";
    // } else {
    // System.out.println("No valid session or session...");
    // return "/supervisor/PAGE_ACCOUNT/create-account";
    // }
    ArrayList<Items> itemList = itemServices.getAllItemsPending();
    model.addAttribute("items", itemList);
    return "supervisor/PAGE_APPROVE/approveitems";
  }

  @PostMapping("/approveItems")
  public String updateApproveItem(HttpSession session, @RequestParam("chk") List<String> checkboxValue) {
    // if (session.getAttribute("username") != null) {
    // return "redirect:/dashboard";
    // } else {
    // System.out.println("No valid session or session...");
    // return "/supervisor/PAGE_ACCOUNT/create-account";
    // }
    boolean status = false;
    for (String value : checkboxValue) {
      // packageServices.insertPackageDetails(pack_id, value);
      System.out.println("approved : " + value);
      try {
        status = itemServices.approveItemsStatus(Integer.parseInt(value));
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    if (status) {
      return "redirect:/approve/approveitems?success=true";
    } else {
      return "redirect:/approve/approveitems?success=false";
    }

  }

  @PostMapping("/rejectItem")
  public String rejectApproveItem(HttpSession session, @RequestParam("chk") List<String> checkboxValue) {
    // if (session.getAttribute("username") != null) {
    // return "redirect:/dashboard";
    // } else {
    // System.out.println("No valid session or session...");
    // return "/supervisor/PAGE_ACCOUNT/create-account";
    // }
    boolean status = false;
    for (String value : checkboxValue) {
      // packageServices.insertPackageDetails(pack_id, value);
      System.out.println("rejected : " + value);
      try {
        status = itemServices.rejectItemsStatus(Integer.parseInt(value));
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    if (status) {
      return "redirect:/approve/approveitems?success=true";
    } else {
      return "redirect:/approve/approveitems?success=false";
    }

  }

}
