package com.heroku.java.MODEL;
import java.sql.Date;

public class ItemsFurniture extends Items{
  private int id2;
  private String location;
  private String warranty;

    public ItemsFurniture() {

  }

   public ItemsFurniture(int id, String name, int quantity, String status, String approval, Date added_date, int id2, String location, String warranty) {
    super(id, name, quantity, status, approval, added_date); 
     this.id2 = id2;
     this.location = location;
     this.warranty = warranty;
  }

  public ItemsFurniture( String name, int quantity, String status, String approval, Date added_date, String location, String warranty) {
    super( name, quantity, status, approval, added_date); 
     this.location = location;
     this.warranty = warranty;
  }

   

    public int getId2() {
      return id2;
    }

    public void setId2(int id2) {
      this.id2 = id2;
    }

    public String getLocation() {
      return location;
    }

    public void setLocation(String location) {
      this.location = location;
    }

    public String getWarranty() {
      return warranty;
    }

    public void setWarranty(String warranty) {
      this.warranty = warranty;
    }
}
