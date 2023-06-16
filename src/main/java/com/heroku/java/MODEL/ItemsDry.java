package com.heroku.java.MODEL;
import java.sql.Date;

public class ItemsDry extends Items {
  private Date expire_date;

  public ItemsDry(
      Date expire_date) {
    // super(id, name, quantity, status, approval, added_date);
    this.expire_date = expire_date;
  }

  public Date getExpire_date() {
    return expire_date;
  }

  public void setExpire_date(Date expire_date) {
    this.expire_date = expire_date;
  }

}
