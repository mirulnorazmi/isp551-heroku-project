package com.heroku.java.CONTROLLER;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.HELPER.PdfGenerator;
import com.heroku.java.MODEL.Items;
import com.heroku.java.SERVICES.ItemServices;
import com.lowagie.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class GenerateController {
  private ItemServices itemServices;

  public GenerateController(ItemServices itemServices) {
    this.itemServices = itemServices;
  }

  @GetMapping("/generate-item")
  public String showGenerateItem() {
    return "staff/generate-item";
  }

  @PostMapping("/generate-item")
  public String downloadGenerateItem(HttpSession session, @ModelAttribute("generateItem") Items items, Model model) {

    System.out.println(items.getCategory());

    return "staff/generate-item";
  }

  @PostMapping("/export-to-pdf")
  public void generatePdfFile(HttpServletResponse response, @RequestParam("category") String category,
      @RequestParam("date_from") java.sql.Date date_from, @RequestParam("date_to") java.sql.Date date_to)
      throws DocumentException, IOException {

    response.setContentType("application/pdf");
    DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
    String currentDateTime = dateFormat.format(new Date());
    String headerkey = "Content-Disposition";
    String headervalue = "attachment; filename=student" + currentDateTime + ".pdf";
    response.setHeader(headerkey, headervalue);
    // List < Items > listofStudents = new ArrayList<>();

    System.out.println(category);
    System.out.println("date from : " + date_from);
    System.out.println("date to : " + date_to);
    List<Items> listofStudents = new ArrayList<>();

    if (category.equals("food")) {
      listofStudents = itemServices.getAllFoodDate(date_from, date_to);
      System.out.println("food cat");
    }
    if (category.equals("furniture")) {
      listofStudents = itemServices.getAllFurnitureDate(date_from, date_to);
      System.out.println("furniture cat");
    } 
    if (category.equals("all")) {
      listofStudents = itemServices.getAllDate(date_from, date_to);
      System.out.println("all cat");
    }
    itemServices.generate(listofStudents, response);
    // PdfGenerator generator = new PdfGenerator();

  }
}
