package com.heroku.java.MODEL;
import java.sql.Date;

public class Items {
  private int id;
  private String name;
  private int quantity;
  private String status;
  private String approval;
  private Date added_date;
  private String category;

   public Items() {
  }

  public Items(String name, int quantity, String status, String approval, Date added_date) {
 
     this.name = name;
     this.quantity = quantity;
     this.status = status;
     this.approval = approval;
     this.added_date = added_date;
  }

   public Items(int id, String name, int quantity, String status, String approval, Date added_date) {
     this.id = id;
     this.name = name;
     this.quantity = quantity;
     this.status = status;
     this.approval = approval;
     this.added_date = added_date;
  }

     public Items(int id, String name, int quantity, String status, String approval, Date added_date, String category) {
     this.id = id;
     this.name = name;
     this.quantity = quantity;
     this.status = status;
     this.approval = approval;
     this.added_date = added_date;
     this.category = category;
  }

  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public int getQuantity() {
    return quantity;
  }
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public String getApproval() {
    return approval;
  }
  public void setApproval(String approval) {
    this.approval = approval;
  }
  public Date getAdded_date() {
    return added_date;
  }
  public void setAdded_date(Date added_date) {
    this.added_date = added_date;
  }

  public String getCategory() {
    return category;
  }
  public void setCategory(String category) {
    this.category = category;
  }
}
