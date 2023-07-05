package com.heroku.java.MODEL;
import java.sql.Date;

public class ItemsWet extends Items{
  private int id2;

   public ItemsWet(int id, String name, int quantity, String status, String approval, Date added_date, int id2) {
    super(id, name, quantity, status, approval, added_date); 
    this.id2 = id2;
  }

     public ItemsWet(int id, String name, int quantity, String status, String approval, Date added_date, String category, int id2) {
     super(id, name, quantity, status, approval, added_date, category);
     this.id2 = id2;
  }

  public int getId2() {
    return id2;
  }
  public void setId2(int id2) {
    this.id2 = id2;
  }
}
