package com.heroku.java.CONTROLLER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.servlet.ModelAndView;

import com.heroku.java.MODEL.Accounts;
import com.heroku.java.SERVICES.AccountServices;

import org.springframework.web.bind.annotation.ModelAttribute;
// // import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Controller
public class AccountController {
  private final DataSource dataSource;
  private AccountServices accountServices;

  @Autowired
  public AccountController(DataSource dataSource, AccountServices accountServices) {
    this.dataSource = dataSource;
    this.accountServices = accountServices;
  }

  @GetMapping("/accounts")
  public String showAccounts(HttpSession session,
      @RequestParam(value = "create_success", defaultValue = "false") boolean createSuccess, Model model) {
    if (session.getAttribute("username") != null) {

      ArrayList<Accounts> accounts = accountServices.getAllAccount();
      model.addAttribute("accounts", accounts);
      return "supervisor/PAGE_ACCOUNT/accounts";

    } else {
      System.out.println("No valid session or session...");
      return "redirect:/";
    }
  }

  @GetMapping("/accounts/create-account")
  public String showSignUp(HttpSession session,
      @RequestParam(value = "success", defaultValue = "false") boolean loginError) {
    if (session.getAttribute("username") != null) {
      return "supervisor/PAGE_ACCOUNT/create-account";
    } else {
      System.out.println("No valid session or session...");
      return "redirect:/";
    }
  }

  @GetMapping("/accounts/update-account")
  public String showUpdateAccount(HttpSession session, @RequestParam(name = "staffid") int id, Model model) {
    if (session.getAttribute("username") != null) {

      Accounts accounts = accountServices.getAccountById(id);
      model.addAttribute("accounts", accounts);
      return "supervisor/PAGE_ACCOUNT/update-account";

    } else {
      System.out.println("No valid session or session...");
      return "redirect:/";
    }
  }

  @GetMapping("/deleteAccount")
  public String deleteAccount(HttpSession session, @ModelAttribute("account") Accounts accounts,
      @RequestParam(name = "staffid") int id, Model model) {
    if (session.getAttribute("username") != null) {

      String url = accountServices.deleteAccountById(id);
      return url;
    } else {
      System.out.println("No valid session or session...");
      return "redirect:/";
    }
  }

  @PostMapping("/updateAccount")
  public String updateAccount(HttpSession session, @ModelAttribute("account") Accounts accounts,
      @RequestParam(name = "staffid") int id, Model model) {
    if (session.getAttribute("username") != null) {
      String url = accountServices.updateAccount(id, accounts);
      return url;
    } else {
      System.out.println("No valid session or session...");
      return "redirect:/";
    }
  }

  @PostMapping("/addAccount")
  public String addAccount(HttpSession session, @ModelAttribute("account") Accounts acc, Model model) {
    boolean status = accountServices.insertAccount(acc);
    if(status){
      return "redirect:/accounts?create_success=true";
    }else{
      return "redirect:/accounts/create-account?success=false";
    }
  }
  
}
