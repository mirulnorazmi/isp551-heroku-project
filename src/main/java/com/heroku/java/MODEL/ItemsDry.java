package com.heroku.java.MODEL;
import java.sql.Date;

public class ItemsDry extends Items {
  private int id2;
  private Date expire_date;

  public ItemsDry(int id, String name, int quantity, String status, String approval, Date added_date, int id2, Date expire_date) {
    super(id, name, quantity, status, approval, added_date);
    this.id2 = id2;
    this.expire_date = expire_date;
  }

    public ItemsDry(int id, String name, int quantity, String status, String approval, Date added_date, String category, int id2, Date expire_date) {
    super(id, name, quantity, status, approval, added_date, category);
    this.id2 = id2;
    this.expire_date = expire_date;
  }

    public int getId2() {
    return id2;
  }

  public void setId2(int id2) {
    this.id2 = id2;
  }

  public Date getExpire_date() {
    return expire_date;
  }

  public void setExpire_date(Date expire_date) {
    this.expire_date = expire_date;
  }

}
