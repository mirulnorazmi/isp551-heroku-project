package com.heroku.java.CONTROLLER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;

import com.heroku.java.MODEL.Accounts;
import com.heroku.java.MODEL.Items;
import com.heroku.java.MODEL.ItemsDry;
import com.heroku.java.MODEL.ItemsFurniture;
import com.heroku.java.MODEL.ItemsStuff;
import com.heroku.java.MODEL.ItemsWet;
import com.heroku.java.SERVICES.ItemServices;

import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Controller
public class ItemController {
  private final DataSource dataSource;
  private ItemServices itemServices;

  @Autowired
  public ItemController(DataSource dataSource, ItemServices itemServices) {
    this.dataSource = dataSource;
    this.itemServices = itemServices;
  }

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @GetMapping("/create-items/create-item-dry")
  public String dry(HttpSession session) {
    if (session.getAttribute("username") != null) {
      return "supervisor/PAGE_CREATE_ITEM/create-item-dry";
    } else {
      System.out.println("No valid session or session...");
      return "redirect:/";
    }
    // return "supervisor/PAGE_CREATE_ITEM/create-item-dry";
  }

  @GetMapping("/create-items/create-item-stuff")
  public String stuff(HttpSession session) {
    if (session.getAttribute("username") != null) {
      return "supervisor/PAGE_CREATE_ITEM/create-item-stuff";
    } else {
      System.out.println("No valid session or session...");
      return "redirect:/";
    }
    // return "supervisor/PAGE_CREATE_ITEM/create-item-stuff";
  }

  @GetMapping("/create-items/create-item-wet")
  public String wet(HttpSession session) {
    if (session.getAttribute("username") != null) {
      return "supervisor/PAGE_CREATE_ITEM/create-item-wet";
    } else {
      System.out.println("No valid session or session...");
      return "redirect:/";
    }
    // return "supervisor/PAGE_CREATE_ITEM/create-item-wet";
  }

  @PostMapping("/createItemDry")
  public String createItemDry(@ModelAttribute("createItem") ItemsDry itemD, HttpSession session) {
    try {
      if (itemD.getName() == null && itemD.getQuantity() == 0 && itemD.getAdded_date() == null
          && itemD.getExpire_date() == null) {
        return "redirect:/create-items/create-item-dry?success=false";
      } else {
        Accounts account = new Accounts();
        account.setStaffid((int) session.getAttribute("staffid"));
        account.setUsername((String) session.getAttribute("username"));

        boolean success = itemServices.createItemDry(itemD, account);
        if (success) {
          return "redirect:/create-items/create-item-dry?success=true";
        } else {
          return "redirect:/create-items/create-item-dry?success=false";
        }
      }
    } catch (Exception e) {
      System.out.println("Exception: " + e.getMessage());
      return "redirect:/create-items/create-item-dry?success=false";
    }
  }

  @PostMapping("/create-item-stuff")
  public String createItemStuff(@ModelAttribute("createStuff") ItemsStuff stuff) {
    try {
      if (stuff.getName() == null && stuff.getQuantity() == 0 && stuff.getAdded_date() == null) {
        return "redirect:/create-items/create-item-stuff?success=false";
      } else {
        boolean success = itemServices.createItemStuff(stuff);
        if (success) {
          return "redirect:/create-items/create-item-stuff?success=true";
        } else {
          return "redirect:/create-items/create-item-stuff?success=false";
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return "redirect:/create-items/create-item-stuff?success=false";
    }
  }

  @PostMapping("/create-item-wet")
  public String createItemWet(@ModelAttribute("createWet") ItemsWet wet, HttpSession session) {
    try {
      if (wet.getName() == null && wet.getQuantity() == 0 && wet.getAdded_date() == null) {
        return "redirect:/create-items/create-item-stuff?success=false";
      } else {
        itemServices.insertItemsWet(wet);

        System.out.println(">>>>Item [" + wet.getId() + "] created by staff[" + session.getAttribute("staffid") + "] "
            + session.getAttribute("username"));

        return "redirect:/create-items/create-item-wet?success=true";
      }

    } catch (SQLException sqe) {
      System.out.println("Error Code = " + sqe.getErrorCode());
      System.out.println("SQL state = " + sqe.getSQLState());
      System.out.println("Message = " + sqe.getMessage());
      System.out.println("printTrace /n");
      sqe.printStackTrace();

      return "redirect:/create-items/create-item-stuff?success=false";
    }

  }

  @GetMapping("/supervisor/items")
  public String viewAllItems(HttpSession session,
      @RequestParam(value = "create_success", defaultValue = "false") boolean createSuccess, Model model)
      throws Exception {
    if (session.getAttribute("username") != null) {
      ArrayList<Items> itemList = itemServices.getAllItems();
      model.addAttribute("items", itemList);
      return "supervisor/PAGE_VIEW_ITEM/view";
    } else {
      System.out.println("No valid session or session...");
      return "redirect:/";
    }
  }

  @GetMapping("/supervisor/items/food")
  public String viewFood(HttpSession session,
      @RequestParam(value = "create_success", defaultValue = "false") boolean createSuccess, Model model) {
    if (session.getAttribute("username") != null) {

      ArrayList<ItemsDry> itemsfood = itemServices.getAllFood();
      model.addAttribute("itemsfood", itemsfood);

      return "supervisor/PAGE_VIEW_ITEM/viewfood";
    } else {
      System.out.println("No valid session or session...");
      return "redirect:/";
    }
  }

  @GetMapping("/supervisor/items/furniture")
  public String viewFurniture(HttpSession session,
      @RequestParam(value = "create_success", defaultValue = "false") boolean createSuccess, Model model) {
    if (session.getAttribute("username") != null) {

      ArrayList<ItemsFurniture> itemsfurniture = itemServices.getAllFurniture();
      model.addAttribute("itemsfurniture", itemsfurniture);
      return "supervisor/PAGE_VIEW_ITEM/viewfurniture";
      // return "supervisor/PAGE_ACCOUNT/accounts";
    } else {
      System.out.println("No valid session or session...");
      return "redirect:/";
    }
  }

  @GetMapping("/deleteItems")
  public String deleteView(HttpSession session, @ModelAttribute("viewfood") Items items,
      @RequestParam(name = "itemsid") int id, Model model) {

    if (session.getAttribute("username") != null) {

      System.out.println("Items id  (delete): " + id);
      boolean status = itemServices.deleteItem(id);
      if (status) {
        return "redirect:/view?delete_success=true";
      } else {
        return "redirect:/view?delete_success=false";
      }

    } else {
      System.out.println("No valid session or session...");
      return "redirect:/";
    }

  }

  @GetMapping("/update-item-supervisor")
  public String showuUpdateItem(HttpSession session, @RequestParam(name = "itemsid") int id, Model model) {
    if (session.getAttribute("username") != null) {
      Items item = itemServices.getItemById(id);
      if (item != null) {
        model.addAttribute("items", item);
        return "supervisor/update-item-supervisor";
      } else {
        return "supervisor/update-item-supervisor?success=false";
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
        System.out.println("Expired date : " + items.getExpire_date());
        itemServices.updateItemsDry(items, id);
        return "redirect:/supervisor/items?update_success=true";
      } catch (SQLException sqe) {
        System.out.println("Error Code = " + sqe.getErrorCode());
        System.out.println("SQL state = " + sqe.getSQLState());
        System.out.println("Message = " + sqe.getMessage());
        sqe.printStackTrace();
        return "redirect:/supervisor/items?update_success=false";
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
        itemServices.updateItemsFurniture(items, id);
        return "redirect:/supervisor/items?update_success=true";
      } catch (SQLException sqe) {
        System.out.println("Error Code = " + sqe.getErrorCode());
        System.out.println("SQL state = " + sqe.getSQLState());
        System.out.println("Message = " + sqe.getMessage());
        sqe.printStackTrace();
        return "redirect:/supervisor/items?update_success=false";
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
        itemServices.updateItemsWet(items, id);
        return "redirect:/supervisor/items?update_success=true";
      } catch (SQLException sqe) {
        System.out.println("Error Code = " + sqe.getErrorCode());
        System.out.println("SQL state = " + sqe.getSQLState());
        System.out.println("Message = " + sqe.getMessage());
        sqe.printStackTrace();
        return "redirect:/supervisor/items?update_success=false";
      }
    } else {
      System.out.println("No valid session or session...");
      return "redirect:/";
    }
  }

  @GetMapping("/staff/items")
  public String showItemStaff(HttpSession session, Model model) {
    // if (session.getAttribute("username") != null) {
    // return "redirect:/dashboard";
    // } else {
    // System.out.println("No valid session or session...");
    // return "/supervisor/PAGE_ACCOUNT/create-account";
    // }
    ArrayList<Items> itemList = itemServices.getAllItems();
    model.addAttribute("items", itemList);
    return "staff/PAGE_VIEW_ITEM/view";

  }

  @GetMapping("/staff/items/food")
  public String showItemFoodStaff(HttpSession session, Model model) {
    // if (session.getAttribute("username") != null) {
    // return "redirect:/dashboard";
    // } else {
    // System.out.println("No valid session or session...");
    // return "/supervisor/PAGE_ACCOUNT/create-account";
    // }
    ArrayList<ItemsDry> itemsfood = itemServices.getAllFood();
    model.addAttribute("itemsfood", itemsfood);

    return "staff/PAGE_VIEW_ITEM/viewfood";

  }

  @GetMapping("/staff/items/furniture")
  public String showItemFurnitureStaff(HttpSession session, Model model) {
    // if (session.getAttribute("username") != null) {
    // return "redirect:/dashboard";
    // } else {
    // System.out.println("No valid session or session...");
    // return "/supervisor/PAGE_ACCOUNT/create-account";
    // }
      ArrayList<ItemsFurniture> itemsfurniture = itemServices.getAllFurniture();
      model.addAttribute("itemsfurniture", itemsfurniture);
      return "staff/PAGE_VIEW_ITEM/viewfurniture";

  }

}
