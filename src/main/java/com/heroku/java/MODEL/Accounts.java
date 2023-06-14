package com.heroku.java.MODEL;

public class Accounts {
  private int staffid;
  private String fullname;
  private String username;
  private String roles;

  
  public Accounts(int staffid, String fullname, String username, String roles) {
    this.staffid = staffid;
    this.fullname = fullname;
    this.username = username;
    this.roles = roles;
  }
  
  public int getStaffid() {
    return staffid;
  }
  public void setStaffid(int staffid) {
    this.staffid = staffid;
  }
  public String getFullname() {
    return fullname;
  }
  public void setFullname(String fullname) {
    this.fullname = fullname;
  }
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  public String getRoles() {
    return roles;
  }
  public void setRoles(String roles) {
    this.roles = roles;
  }

  
}
