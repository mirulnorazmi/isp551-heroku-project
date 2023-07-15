package com.heroku.java.MODEL;

import java.sql.Date;

public class ItemsStuff extends Items {

    private String location;
    private String warranty;

    public ItemsStuff(int id, String name, int quantity, String status, String approval, Date added_date,
            String location, String warranty) {
        super(id, name, quantity, status, approval, added_date);
        this.location = location;
        this.warranty = warranty;
    }

    public ItemsStuff() {
      
    }

    public ItemsStuff( String name, int quantity, String status, String approval, Date added_date,
            String location, String warranty) {
        super(name, quantity, status, approval, added_date);
        this.location = location;
        this.warranty = warranty;
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