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
import com.heroku.java.MODEL.Stock;
import com.heroku.java.MODEL.Users;

import jakarta.servlet.http.HttpSession;

@Service
public class QuantityItemServices {
  private final DataSource dataSource;
  private final SQLEx database;
  private final HttpSession session;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @Autowired
  public QuantityItemServices(DataSource dataSource, SQLEx database, HttpSession session) {
    this.dataSource = dataSource;
    this.database = database;
    this.session = session;
  }

  private final String INSERT_STOCK = "INSERT INTO stock_movement (staffid, itemsid, quantity, status, invdate) " +
      "VALUES (?, ?, ?, 'good', ?) " +
      "ON CONFLICT (staffid, itemsid) " +
      "DO UPDATE SET quantity = ?, status = 'good', invdate = ? " +
      "WHERE stock_movement.staffid = ? AND stock_movement.itemsid = ?;";

    private final String UPDATE_RESTOCK_QUANTITY = "UPDATE items SET quantity = quantity + ? WHERE itemsid=?;";
    private final String UPDATE_RELEASE_QUANTITY = "UPDATE items SET quantity = quantity - ? WHERE itemsid=?";

  public boolean insertStockMovement(Stock stock) {
    boolean status = false;
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(INSERT_STOCK)) {
      statement.setInt(1, stock.getStaffid());
      statement.setInt(2, stock.getItemsid());
      statement.setInt(3, stock.getQuantity());
      statement.setDate(4, stock.getInvdate());
      statement.setInt(5, stock.getQuantity());
      statement.setDate(6, stock.getInvdate());
      statement.setInt(7, stock.getStaffid());
      statement.setInt(8, stock.getItemsid());

      int rowUpdate = statement.executeUpdate();
      status = rowUpdate > 0;

      if(status){
        updateRestockQuantityItem(stock.getItemsid(), stock.getQuantity());
      }

    } catch (SQLException ex) {
      // return status;
      database.printSQLException(ex);
    }
    return status;
  }

  public boolean updateRestockQuantityItem(int itemsid,int quantity){
    boolean status = false;
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(UPDATE_RESTOCK_QUANTITY)) {
      statement.setInt(1, quantity);
      statement.setInt(2, itemsid);

      int rowUpdate = statement.executeUpdate();
      status = rowUpdate > 0;
      System.out.println("Update restock quantity : " + status);

    } catch (SQLException ex) {
      // return status;
      database.printSQLException(ex);
    }
    return status;
  }

  public boolean updateReleaseQuantityItem(int itemsid,int quantity){
    boolean status = false;
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(UPDATE_RELEASE_QUANTITY)) {
      statement.setInt(1, quantity);
      statement.setInt(2, itemsid);

      int rowUpdate = statement.executeUpdate();
      status = rowUpdate > 0;
      System.out.println("Update release quantity : " + status);

    } catch (SQLException ex) {
      // return status;
      database.printSQLException(ex);
    }
    return status;
  }
}
