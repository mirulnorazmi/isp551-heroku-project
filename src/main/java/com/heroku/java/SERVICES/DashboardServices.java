package com.heroku.java.SERVICES;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.heroku.java.HELPER.SQLEx;
import com.heroku.java.MODEL.Accounts;
import com.heroku.java.MODEL.Users;

import jakarta.servlet.http.HttpSession;

@Service
public class DashboardServices {
  private final DataSource dataSource;
  private final SQLEx database;
  private final HttpSession session;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @Autowired
  public DashboardServices(DataSource dataSource, SQLEx database, HttpSession session) {
    this.dataSource = dataSource;
    this.database = database;
    this.session = session;
  }

  private final String COUNT_STAFF = "SELECT COUNT(staffid) FROM staff WHERE roles = 'staff';";
  private final String COUNT_SUPERVISOR = "SELECT COUNT(staffid) FROM staff WHERE roles = 'supervisor';";
  private final String COUNT_ALL_USER = "SELECT COUNT(staffid) FROM staff;";
  private final String COUNT_ALL_DRY = "SELECT COUNT(itemsid) FROM items JOIN dry_ingredients using (itemsid) WHERE items.approval= 'approved';";
  private final String COUNT_ALL_WET = "SELECT COUNT(itemsid) FROM items JOIN wet_ingredients using (itemsid) WHERE items.approval= 'approved';";
  private final String COUNT_ALL_FURNITURE = "SELECT COUNT(itemsid) FROM items JOIN furniture using (itemsid) WHERE items.approval= 'approved';";
  private final String COUNT_ALL_ITEMS = "SELECT COUNT(itemsid) FROM items WHERE approval = 'approved';";
  private final String COUNT_ALL_PENDINGITEMS = "SELECT COUNT(itemsid) FROM items WHERE approval = 'pending';";

 public int getRowAllPending() {
    int count = 0;
    try {
      Connection connection = dataSource.getConnection();

      final var statement = connection.prepareStatement(COUNT_ALL_PENDINGITEMS);
      final var resultSet = statement.executeQuery();

      while (resultSet.next()) {
        count = resultSet.getInt("count");
      }
      connection.close();
    } catch (Throwable t) {
      System.out.println("message : " + t.getMessage());
    }
    return count;
  }

 public int getRowAllItems() {
    int count = 0;
    try {
      Connection connection = dataSource.getConnection();

      final var statement = connection.prepareStatement(COUNT_ALL_ITEMS);
      final var resultSet = statement.executeQuery();

      while (resultSet.next()) {
        count = resultSet.getInt("count");
      }
      connection.close();
    } catch (Throwable t) {
      System.out.println("message : " + t.getMessage());
    }
    return count;
  }
  
 public int getRowDry() {
    int count = 0;
    try {
      Connection connection = dataSource.getConnection();

      final var statement = connection.prepareStatement(COUNT_ALL_DRY);
      final var resultSet = statement.executeQuery();

      while (resultSet.next()) {
        count = resultSet.getInt("count");
      }
      connection.close();
    } catch (Throwable t) {
      System.out.println("message : " + t.getMessage());
    }
    return count;
  }

   public int getRowWet() {
    int count = 0;
    try {
      Connection connection = dataSource.getConnection();

      final var statement = connection.prepareStatement(COUNT_ALL_WET);
      final var resultSet = statement.executeQuery();

      while (resultSet.next()) {
        count = resultSet.getInt("count");
      }
      connection.close();
    } catch (Throwable t) {
      System.out.println("message : " + t.getMessage());
    }
    return count;
  }

   public int getRowFurniture() {
    int count = 0;
    try {
      Connection connection = dataSource.getConnection();

      final var statement = connection.prepareStatement(COUNT_ALL_FURNITURE);
      final var resultSet = statement.executeQuery();

      while (resultSet.next()) {
        count = resultSet.getInt("count");
      }
      connection.close();
    } catch (Throwable t) {
      System.out.println("message : " + t.getMessage());
    }
    return count;
  }

  public int getRowStaff() {
    int count = 0;
    try {
      Connection connection = dataSource.getConnection();

      final var statement = connection.prepareStatement(COUNT_STAFF);
      final var resultSet = statement.executeQuery();

      while (resultSet.next()) {
        count = resultSet.getInt("count");
      }
      connection.close();
    } catch (Throwable t) {
      System.out.println("message : " + t.getMessage());
    }
    return count;
  }

  public int getRowSupervisor() {
    int count = 0;
    try {
      Connection connection = dataSource.getConnection();

      final var statement = connection.prepareStatement(COUNT_SUPERVISOR);
      final var resultSet = statement.executeQuery();

      while (resultSet.next()) {
        count = resultSet.getInt("count");
      }
      connection.close();
    } catch (Throwable t) {
      System.out.println("message : " + t.getMessage());
    }
    return count;
  }

  public int getAllRowUser(){
    int count = 0;
    try {
      Connection connection = dataSource.getConnection();

      final var statement = connection.prepareStatement(COUNT_ALL_USER);
      final var resultSet = statement.executeQuery();

      while (resultSet.next()) {
        count = resultSet.getInt("count");
      }
      connection.close();
    } catch (Throwable t) {
      System.out.println("message : " + t.getMessage());
    }
    return count;
  }

  public ArrayList<Accounts> getSessionAccount(){
    ArrayList<Accounts> accounts = new ArrayList<>();
    try (Connection connection = dataSource.getConnection()) {
      final var statement = connection.createStatement();

      final var resultSet = statement.executeQuery("SELECT staffid, fullname, username, password, roles FROM staff");
      
      while (resultSet.next()) {
        int staffid = resultSet.getInt("staffid");
        String fullname = resultSet.getString("fullname");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        String roles = resultSet.getString("roles");

        accounts.add(new Accounts(staffid, fullname, username, password, roles));
      }
    
      connection.close();

    } catch (Throwable t) {
      System.out.println("message : " + t.getMessage());
    }
    return accounts;
  }
}
