package com.heroku.java.CONTROLLER;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.MODEL.Items;
import com.heroku.java.MODEL.ItemsDry;
import com.heroku.java.MODEL.Users;

import jakarta.servlet.http.HttpSession;

@Controller
public class GenerateController {
  
  @GetMapping("/generate-item")
  public String showGenerateItem(){
    return "staff/generate-item";
  }

  @PostMapping("/generate-item")
  public String downloadGenerateItem(HttpSession session,@ModelAttribute("generateItem") Items items, Model model){
    
  System.out.println(items.getCategory());

    return "staff/generate-item";
  }
}
