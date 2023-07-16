package com.heroku.java.MODEL;

import java.sql.Date;

public class Stock {
  private int staffid;
  private int itemsid;
  private int quantity;
  private Date invdate;

  public Stock() {
    // default constructor
  }
  
  public Stock(int staffid, int itemsid, int quantity, Date invdate) {
    this.staffid = staffid;
    this.itemsid = itemsid;
    this.quantity = quantity;
    this.invdate = invdate;
  }

  public int getStaffid() {
    return staffid;
  }
  public void setStaffid(int staffid) {
    this.staffid = staffid;
  }
  public int getItemsid() {
    return itemsid;
  }
  public void setItemsid(int itemsid) {
    this.itemsid = itemsid;
  }
  public int getQuantity() {
    return quantity;
  }
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
  public Date getInvdate() {
    return invdate;
  }
  public void setInvdate(Date invdate) {
    this.invdate = invdate;
  }

  
}
