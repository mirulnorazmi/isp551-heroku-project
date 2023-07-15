package com.heroku.java.CONTROLLER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.apache.logging.log4j.*;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

@Controller
public class ReleaseController {

  @GetMapping("/release-item")
  public String release(HttpSession session) {
    // if (session.getAttribute("username") != null) {
    //   return "redirect:/dashboard";
    // } else {
    //   System.out.println("No valid session or session...");
    //   return "/supervisor/PAGE_ACCOUNT/create-account";
    // }
    return "/supervisor/PAGE_RELEASE_ITEM/release-item";
  }
 
}