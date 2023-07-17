package com.heroku.java.SERVICES;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.io.IOException;
import java.sql.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.heroku.java.HELPER.SQLEx;
import com.heroku.java.MODEL.Accounts;
import com.heroku.java.MODEL.Items;
import com.heroku.java.MODEL.ItemsDry;
import com.heroku.java.MODEL.ItemsFurniture;
import com.heroku.java.MODEL.ItemsStuff;
import com.heroku.java.MODEL.ItemsWet;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpSession;

@Service
public class ItemServices {
  private final DataSource dataSource;
  private final SQLEx database;
  private final HttpSession session;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @Autowired
  public ItemServices(DataSource dataSource, SQLEx database, HttpSession session) {
    this.dataSource = dataSource;
    this.database = database;
    this.session = session;
  }

  private final String SELECT_ALL_ITEMS = "SELECT * FROM items i LEFT OUTER JOIN dry_ingredients d ON (i.itemsid = d.itemsid) LEFT JOIN wet_ingredients w ON (i.itemsid = w.itemsid) LEFT JOIN furniture fi ON (i.itemsid = fi.itemsid) WHERE i.approval = 'approved' ORDER BY i.itemsid";
  private final String SELECT_ALL_ITEMS_PENDING = "SELECT * FROM items i LEFT OUTER JOIN dry_ingredients d ON (i.itemsid = d.itemsid) LEFT JOIN wet_ingredients w ON (i.itemsid = w.itemsid) LEFT JOIN furniture fi ON (i.itemsid = fi.itemsid) WHERE i.approval = 'pending' OR i.approval ='rejected' ORDER BY i.itemsid";
  private final String SELECT_ALL_FOOD = "SELECT * FROM items i LEFT JOIN dry_ingredients di ON (i.itemsid = di.itemsid) LEFT JOIN wet_ingredients wi ON (i.itemsid = wi.itemsid) LEFT JOIN furniture fi ON (i.itemsid = fi.itemsid) WHERE i.itemsid NOT IN (SELECT itemsid FROM furniture) AND i.approval = 'approved' ORDER BY i.itemsid";
  private final String SELECT_ALL_FURNITURE = "SELECT * FROM items i JOIN furniture fi ON (i.itemsid = fi.itemsid) AND i.approval = 'approved' ORDER BY i.itemsid";
  private final String SELECT_ITEMS = "SELECT * FROM items i LEFT OUTER JOIN dry_ingredients d ON (i.itemsid = d.itemsid) "
      +
      "LEFT JOIN wet_ingredients w ON (i.itemsid = w.itemsid) " +
      "LEFT JOIN furniture fi ON (i.itemsid = fi.itemsid) WHERE i.itemsid = ?;";

  private final String SELECT_ALL_DATE = "SELECT * FROM items i LEFT OUTER JOIN dry_ingredients d ON (i.itemsid = d.itemsid) LEFT JOIN wet_ingredients w ON (i.itemsid = w.itemsid) LEFT JOIN furniture fi ON (i.itemsid = fi.itemsid) WHERE i.approval = 'approved' AND ? <= added_date AND ? >= added_date ORDER BY i.itemsid";
  private final String SELECT_ALL_FOOD_DATE = "SELECT * FROM items i LEFT JOIN dry_ingredients di ON (i.itemsid = di.itemsid) LEFT JOIN wet_ingredients wi ON (i.itemsid = wi.itemsid) LEFT JOIN furniture fi ON (i.itemsid = fi.itemsid) WHERE i.itemsid NOT IN (SELECT itemsid FROM furniture) AND i.approval = 'approved' AND ? <= added_date AND ? >= added_date ORDER BY i.itemsid";
  // private final String SELECT_ALL_FOOD_DATE = "SELECT * FROM items i " +
  //       "LEFT JOIN dry_ingredients di ON (i.itemsid = di.itemsid) " +
  //       "LEFT JOIN wet_ingredients wi ON (i.itemsid = wi.itemsid) " +
  //       "LEFT JOIN furniture fi ON (i.itemsid = fi.itemsid) " +
  //       "WHERE i.itemsid NOT IN (SELECT itemsid FROM furniture) " +
  //       "AND i.approval = 'approved' " +
  //       "AND i.added_date >= '2023-07-15' AND i.added_date <= '2023-07-16' " +
  //       "ORDER BY i.itemsid";
  private final String SELECT_ALL_FURNITURE_DATE = "SELECT * FROM items i JOIN furniture fi ON (i.itemsid = fi.itemsid) WHERE i.approval = 'approved' AND ? <= added_date AND ? >= added_date ORDER BY i.itemsid";

  // private final String SELECT_ITEMS_WET_DATE = "SELECT * FROM items JOIN wet_ingredients USING (itemsid) WHERE ? <= added_date AND ? >= added_date";
  // private final String SELECT_ITEMS_FURNITURE_DATE = "SELECT * FROM items JOIN wet_ingredients USING (itemsid) WHERE ? > added_date && ? < added_date";

  // "SELECT * FROM items i LEFT OUTER JOIN dry_ingredients d ON (i.itemsid =
  // d.itemsid) LEFT JOIN wet_ingredients w ON (i.itemsid = w.itemsid) LEFT JOIN
  // furniture fi ON (i.itemsid = fi.itemsid) WHERE i.approval = 'approved' ORDER
  // BY i.itemsid";

  public ArrayList<Items> getAllItems() {
    ArrayList<Items> items = new ArrayList<>();
    try (Connection connection = dataSource.getConnection()) {

      final var statement = connection.createStatement();

      final var resultSet = statement.executeQuery(SELECT_ALL_ITEMS);

      while (resultSet.next()) {

        String category = "";
        int itemsid_i = resultSet.getInt("itemsid");
        String name = resultSet.getString("name");
        int quantity = resultSet.getInt("quantity");
        String status = resultSet.getString("status");
        String approval = resultSet.getString("approval");
        Date added_date = resultSet.getDate("added_date");
        Date expire_date = resultSet.getDate("expire_date");
        String location = resultSet.getString("location");

        if (expire_date != null) {
          category = "Dry Ingredient";
        } else if (location != null) {
          category = "Furniture";
        } else {
          category = "Wet Ingredient";
        }

        items.add(new Items(itemsid_i, name, quantity, status, approval, added_date, category));
      }
      connection.close();

    } catch (SQLException sqlex) {
      System.out.println("Error : " + sqlex.getSQLState());
      System.out.println("message : " + sqlex.getMessage());
    }

    return items;
  }

  public ArrayList<Items> getAllItemsPending() {
    ArrayList<Items> items = new ArrayList<>();
    try (Connection connection = dataSource.getConnection()) {

      final var statement = connection.createStatement();

      final var resultSet = statement.executeQuery(SELECT_ALL_ITEMS_PENDING);

      while (resultSet.next()) {

        String category = "";
        int itemsid_i = resultSet.getInt("itemsid");
        String name = resultSet.getString("name");
        int quantity = resultSet.getInt("quantity");
        String status = resultSet.getString("status");
        String approval = resultSet.getString("approval");
        Date added_date = resultSet.getDate("added_date");
        Date expire_date = resultSet.getDate("expire_date");
        String location = resultSet.getString("location");

        if (expire_date != null) {
          category = "Dry Ingredient";
        } else if (location != null) {
          category = "Furniture";
        } else {
          category = "Wet Ingredient";
        }

        items.add(new Items(itemsid_i, name, quantity, status, approval, added_date, category));
      }
      connection.close();

    } catch (SQLException sqlex) {
      System.out.println("Error : " + sqlex.getSQLState());
      System.out.println("message : " + sqlex.getMessage());
    }

    return items;
  }

  public ArrayList<ItemsDry> getAllFood() {

    ArrayList<ItemsDry> itemsfood = new ArrayList<>();

    try (Connection connection = dataSource.getConnection()) {
      final var statement = connection.createStatement();

      final var resultSet = statement.executeQuery(SELECT_ALL_FOOD);

      while (resultSet.next()) {
        String category = "";
        int itemsid_i = resultSet.getInt("itemsid");
        String name = resultSet.getString("name");
        int quantity = resultSet.getInt("quantity");
        String status = resultSet.getString("status");
        String approval = resultSet.getString("approval");
        Date added_date = resultSet.getDate("added_date");
        int itemsid_di = resultSet.getInt("itemsid");
        Date expire_date = resultSet.getDate("expire_date");

        if (expire_date != null) {
          category = "Dry Ingredient";
        } else {
          category = "Wet Ingredient";
        }

        itemsfood.add(
            new ItemsDry(itemsid_i, name, quantity, status, approval, added_date, category, itemsid_di, expire_date));
      }
      connection.close();

    } catch (SQLException sqlex) {
      System.out.println("Error : " + sqlex.getSQLState());
      System.out.println("message : " + sqlex.getMessage());
    }

    return itemsfood;
  }

  public ArrayList<ItemsFurniture> getAllFurniture() {
    ArrayList<ItemsFurniture> itemsfurniture = new ArrayList<>();

    try (Connection connection = dataSource.getConnection()) {
      final var statement = connection.createStatement();

      final var resultSet = statement.executeQuery(SELECT_ALL_FURNITURE);

      while (resultSet.next()) {
        String category = "";
        int itemsid_i = resultSet.getInt("itemsid");
        String name = resultSet.getString("name");
        int quantity = resultSet.getInt("quantity");
        String status = resultSet.getString("status");
        String approval = resultSet.getString("approval");
        Date added_date = resultSet.getDate("added_date");
        int itemsid_fi = resultSet.getInt("itemsid");
        String location = resultSet.getString("location");
        String warranty = resultSet.getString("warranty");

        if (location != null) {
          category = "Furniture";
        }

        itemsfurniture.add(new ItemsFurniture(itemsid_i, name, quantity, status, approval, added_date, category,
            itemsid_fi, location, warranty));
      }

      connection.close();

    } catch (SQLException sqlex) {
      System.out.println("Error : " + sqlex.getSQLState());
      System.out.println("message : " + sqlex.getMessage());
    }

    return itemsfurniture;
  }

  public boolean deleteItem(int id) {
    boolean status = false;
    try {
      Connection connection = dataSource.getConnection();
      // Statement stmt = connection.createStatement();
      String sql = "DELETE FROM items WHERE itemsid=?";
      final var statement = connection.prepareStatement(sql);

      // var sessionId = session.getAttribute("itemid").toString();

      statement.setInt(1, id);
      statement.executeUpdate();
      status = true;
      connection.close();

    } catch (SQLException sqe) {
      System.out.println("Error Code = " + sqe.getErrorCode());
      System.out.println("SQL state = " + sqe.getSQLState());
      System.out.println("Message = " + sqe.getMessage());
      System.out.println("printTrace /n");
      sqe.printStackTrace();

    }
    return status;
  }

  public Items getItemById(int id) {
    try (Connection connection = dataSource.getConnection()) {

      final var statement = connection.prepareStatement(SELECT_ITEMS);
      statement.setInt(1, id);
      final var resultSet = statement.executeQuery();

      if (resultSet.next()) {
        String category = "";
        int itemsid_i = resultSet.getInt("itemsid");
        String name = resultSet.getString("name");
        int quantity = resultSet.getInt("quantity");
        String status = resultSet.getString("status");
        String approval = resultSet.getString("approval");
        Date added_date = resultSet.getDate("added_date");
        Date expire_date = resultSet.getDate("expire_date");
        String location = resultSet.getString("location");
        String warranty = resultSet.getString("warranty");

        if (expire_date != null) {
          category = "Dry Ingredient";
          return new ItemsDry(itemsid_i, name, quantity, status, approval, added_date, category,
              itemsid_i, expire_date);
        } else if (location != null) {
          category = "Furniture";
          return new ItemsFurniture(itemsid_i, name, quantity, status, approval, added_date, category, itemsid_i,
              location, warranty);
        } else {
          category = "Wet Ingredient";
          return new Items(itemsid_i, name, quantity, status, approval, added_date, category);
        }
      }
      connection.close();
    } catch (Throwable t) {
      System.out.println("message : " + t.getMessage());
    }
    return null;
  }

  public boolean approveItemsStatus(int itemsid) throws SQLException {
    boolean status = false;
    try (Connection connection = dataSource.getConnection()) {
      String sqlupdate1 = "UPDATE items SET approval='approved' WHERE itemsid=?;";

      PreparedStatement statement = connection.prepareStatement(sqlupdate1);

      statement.setInt(1, itemsid);
      int rowUpdate = statement.executeUpdate();
      status = rowUpdate > 0;

      connection.close();
    } catch (SQLException sqe) {
      System.out.println("Error Code = " + sqe.getErrorCode());
      System.out.println("SQL state = " + sqe.getSQLState());
      System.out.println("Message = " + sqe.getMessage());
      System.out.println("printTrace /n");
      sqe.printStackTrace();

    }
    return status;
  }

  public boolean rejectItemsStatus(int itemsid) throws SQLException {
    boolean status = false;
    try (Connection connection = dataSource.getConnection()) {
      String sqlupdate1 = "UPDATE items SET approval='rejected' WHERE itemsid=?;";

      PreparedStatement statement = connection.prepareStatement(sqlupdate1);

      statement.setInt(1, itemsid);
      int rowUpdate = statement.executeUpdate();
      status = rowUpdate > 0;

      connection.close();
    } catch (SQLException sqe) {
      System.out.println("Error Code = " + sqe.getErrorCode());
      System.out.println("SQL state = " + sqe.getSQLState());
      System.out.println("Message = " + sqe.getMessage());
      System.out.println("printTrace /n");
      sqe.printStackTrace();

    }
    return status;
  }

  public void updateItemsDry(ItemsDry items, int id) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      String sqlupdate1 = "UPDATE items SET name=?, quantity=?, added_date=? WHERE itemsid = ?";
      String sqlupdate2 = "UPDATE dry_ingredients SET expire_date=? WHERE itemsid = ?";
      String category = "";

      String name = items.getName();
      int quantity = items.getQuantity();
      Date added_date = items.getAdded_date();
      Date expire_date = items.getExpire_date();

      PreparedStatement statement1 = connection.prepareStatement(sqlupdate1);
      PreparedStatement statement2 = connection.prepareStatement(sqlupdate2);

      statement2.setDate(1, expire_date);
      statement2.setInt(2, id);
      statement2.executeUpdate();

      statement1.setString(1, name);
      statement1.setInt(2, quantity);
      statement1.setDate(3, added_date);
      statement1.setInt(4, id);
      statement1.executeUpdate();
      connection.close();
    } catch (SQLException sqe) {
      System.out.println("Error Code = " + sqe.getErrorCode());
      System.out.println("SQL state = " + sqe.getSQLState());
      System.out.println("Message = " + sqe.getMessage());
      System.out.println("printTrace /n");
      sqe.printStackTrace();

    }
  }

  public void updateItemsFurniture(ItemsFurniture items, int id) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      String sqlupdate1 = "UPDATE items SET name=?, quantity=?, added_date=? WHERE itemsid = ?";
      String sqlupdate2 = "UPDATE furniture SET location=?, warranty=? WHERE itemsid = ?";

      String name = items.getName();
      int quantity = items.getQuantity();
      Date added_date = items.getAdded_date();
      String location = items.getLocation();
      String warranty = items.getWarranty();

      PreparedStatement statement1 = connection.prepareStatement(sqlupdate1);
      PreparedStatement statement2 = connection.prepareStatement(sqlupdate2);

      statement2.setString(1, location);
      statement2.setString(2, warranty);
      statement2.setInt(3, id);
      statement2.executeUpdate();

      statement1.setString(1, name);
      statement1.setInt(2, quantity);
      statement1.setDate(3, added_date);
      statement1.setInt(4, id);
      statement1.executeUpdate();
      connection.close();
    } catch (SQLException sqe) {
      System.out.println("Error Code = " + sqe.getErrorCode());
      System.out.println("SQL state = " + sqe.getSQLState());
      System.out.println("Message = " + sqe.getMessage());
      System.out.println("printTrace /n");
      sqe.printStackTrace();

    }
  }

  public void updateItemsWet(ItemsWet items, int id) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      String sqlupdate1 = "UPDATE items SET name=?, quantity=?, added_date=? WHERE itemsid = ?";

      String name = items.getName();
      int quantity = items.getQuantity();
      Date added_date = items.getAdded_date();

      PreparedStatement statement1 = connection.prepareStatement(sqlupdate1);

      statement1.setString(1, name);
      statement1.setInt(2, quantity);
      statement1.setDate(3, added_date);
      statement1.setInt(4, id);
      statement1.executeUpdate();
      connection.close();
    } catch (SQLException sqe) {
      System.out.println("Error Code = " + sqe.getErrorCode());
      System.out.println("SQL state = " + sqe.getSQLState());
      System.out.println("Message = " + sqe.getMessage());
      System.out.println("printTrace /n");
      sqe.printStackTrace();

    }
  }

  public boolean createItemDry(ItemsDry itemD, Accounts account) {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement1 = connection.prepareStatement(
            "INSERT INTO items(name, quantity, added_date, approval) VALUES (?,?,?,?) RETURNING itemsid AS itemsid;");
        PreparedStatement statement2 = connection
            .prepareStatement("INSERT INTO dry_ingredients(itemsid, expire_date) VALUES (?,?)")) {

      statement1.setString(1, itemD.getName());
      statement1.setInt(2, itemD.getQuantity());
      statement1.setDate(3, itemD.getAdded_date());
      statement1.setString(4, "approved");
      ResultSet items_d = statement1.executeQuery();

      int items_id = 0;
      if (items_d.next()) {
        items_id = items_d.getInt(1);
      }

      statement2.setInt(1, items_id);
      statement2.setDate(2, itemD.getExpire_date());
      statement2.executeUpdate();

      System.out
          .println(
              ">>>>Item [" + items_id + "] created by staff[" + account.getStaffid() + "] " + account.getUsername());
      connection.close();
      return true;
    } catch (SQLException sqe) {
      System.out.println("Error Code = " + sqe.getErrorCode());
      System.out.println("SQL state = " + sqe.getSQLState());
      System.out.println("Message = " + sqe.getMessage());
      sqe.printStackTrace();
      return false;
    } catch (Exception e) {
      System.out.println("E message : " + e.getMessage());
      return false;
    }
  }

  public boolean createItemStuff(ItemsStuff stuff) throws SQLException {
    boolean success = false;

    try (Connection connection = dataSource.getConnection()) {
      String sql_items = "INSERT INTO items(name, quantity, added_date,approval) VALUES (?,?,?,?) RETURNING itemsid AS itemsid;";
      final var pstatement1 = connection.prepareStatement(sql_items);
      System.out.println("Added date : " + stuff.getAdded_date());
      pstatement1.setString(1, stuff.getName());
      pstatement1.setInt(2, stuff.getQuantity());
      pstatement1.setDate(3, stuff.getAdded_date());
      pstatement1.setString(4, "approved");
      pstatement1.execute();
      ResultSet items_d = pstatement1.getResultSet();
      int items_id = 0;
      if (items_d.next()) {
        items_id = items_d.getInt(1);
      }

      System.out.println(">>>>Item [" + items_id + "] created by staff[" + session.getAttribute("staffid") + "] "
          + session.getAttribute("username"));
      String sql_stuff = "INSERT INTO furniture(itemsid, location, warranty) VALUES (?,?,?)";

      final var pstatement2 = connection.prepareStatement(sql_stuff);
      pstatement2.setInt(1, items_id);
      pstatement2.setString(2, stuff.getLocation());
      pstatement2.setString(3, stuff.getWarranty());
      pstatement2.executeUpdate();

      success = true;
      connection.close();
    } catch (SQLException sqe) {
      System.out.println("Error Code = " + sqe.getErrorCode());
      System.out.println("SQL state = " + sqe.getSQLState());
      System.out.println("Message = " + sqe.getMessage());
      System.out.println("printTrace /n");
      sqe.printStackTrace();
    } catch (Exception e) {
      System.out.println("E message : " + e.getMessage());
    }

    return success;
  }

  public void insertItemsWet(ItemsWet wet) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      String sqlItems = "INSERT INTO items(name, quantity, added_date,approval) VALUES (?,?,?,?) RETURNING itemsid AS itemsid;";
      PreparedStatement statement1 = connection.prepareStatement(sqlItems);
      statement1.setString(1, wet.getName());
      statement1.setInt(2, wet.getQuantity());
      statement1.setDate(3, wet.getAdded_date());
      statement1.setString(4, "approved");
      statement1.execute();

      ResultSet itemsResultSet = statement1.getResultSet();
      int itemsId = 0;
      if (itemsResultSet.next()) {
        itemsId = itemsResultSet.getInt("itemsid");
      }

      String sqlWet = "INSERT INTO wet_ingredients(itemsid) VALUES (?)";
      PreparedStatement statement2 = connection.prepareStatement(sqlWet);
      statement2.setInt(1, itemsId);
      statement2.executeUpdate();
      connection.close();
    }
  }

  public boolean requestItemDry(ItemsDry reqD, Accounts account) {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement1 = connection.prepareStatement(
            "INSERT INTO items(name, quantity, added_date, approval) VALUES (?,?,?,?) RETURNING itemsid AS itemsid;");
        PreparedStatement statement2 = connection
            .prepareStatement("INSERT INTO dry_ingredients(itemsid, expire_date) VALUES (?,?)")) {

      statement1.setString(1, reqD.getName());
      statement1.setInt(2, reqD.getQuantity());
      statement1.setDate(3, reqD.getAdded_date());
      statement1.setString(4, "pending");
      ResultSet items_d = statement1.executeQuery();

      int items_id = 0;
      if (items_d.next()) {
        items_id = items_d.getInt(1);
      }

      statement2.setInt(1, items_id);
      statement2.setDate(2, reqD.getExpire_date());
      statement2.executeUpdate();

      System.out
          .println(
              ">>>>Item [" + items_id + "] created by staff[" + account.getStaffid() + "] " + account.getUsername());
      connection.close();
      return true;
    } catch (SQLException sqe) {
      System.out.println("Error Code = " + sqe.getErrorCode());
      System.out.println("SQL state = " + sqe.getSQLState());
      System.out.println("Message = " + sqe.getMessage());
      sqe.printStackTrace();
      return false;
    } catch (Exception e) {
      System.out.println("E message : " + e.getMessage());
      return false;
    }
  }

  public boolean requestItemStuff(ItemsStuff reqS) throws SQLException {
    boolean success = false;

    try (Connection connection = dataSource.getConnection()) {
      String sql_items = "INSERT INTO items(name, quantity, added_date,approval) VALUES (?,?,?,?) RETURNING itemsid AS itemsid;";
      final var pstatement1 = connection.prepareStatement(sql_items);
      pstatement1.setString(1, reqS.getName());
      pstatement1.setInt(2, reqS.getQuantity());
      pstatement1.setDate(3, reqS.getAdded_date());
      pstatement1.setString(4, "pending");
      pstatement1.execute();
      ResultSet items_d = pstatement1.getResultSet();
      int items_id = 0;
      if (items_d.next()) {
        items_id = items_d.getInt(1);
      }

      System.out.println(">>>>Item [" + items_id + "] created by staff[" + session.getAttribute("staffid") + "] "
          + session.getAttribute("username"));
      String sql_stuff = "INSERT INTO furniture(itemsid, location, warranty) VALUES (?,?,?)";

      final var pstatement2 = connection.prepareStatement(sql_stuff);
      pstatement2.setInt(1, items_id);
      pstatement2.setString(2, reqS.getLocation());
      pstatement2.setString(3, reqS.getWarranty());
      pstatement2.executeUpdate();

      success = true;
      connection.close();
    } catch (SQLException sqe) {
      System.out.println("Error Code = " + sqe.getErrorCode());
      System.out.println("SQL state = " + sqe.getSQLState());
      System.out.println("Message = " + sqe.getMessage());
      System.out.println("printTrace /n");
      sqe.printStackTrace();
    } catch (Exception e) {
      System.out.println("E message : " + e.getMessage());
    }

    return success;
  }

  public boolean requestItemWet(ItemsWet reqW) throws SQLException {
    boolean success = false;

    try (Connection connection = dataSource.getConnection()) {
      String sql_items = "INSERT INTO items(name, quantity, added_date,approval) VALUES (?,?,?,?) RETURNING itemsid AS itemsid;";
      final var pstatement1 = connection.prepareStatement(sql_items);
      pstatement1.setString(1, reqW.getName());
      pstatement1.setInt(2, reqW.getQuantity());
      pstatement1.setDate(3, reqW.getAdded_date());
      pstatement1.setString(4, "pending");
      pstatement1.execute();
      ResultSet items_d = pstatement1.getResultSet();
      int items_id = 0;
      if (items_d.next()) {
        items_id = items_d.getInt(1);
      }

      System.out.println(">>>>Item [" + items_id + "] created by staff[" + session.getAttribute("staffid") + "] "
          + session.getAttribute("username"));
      String sql_stuff = "INSERT INTO wet_ingredients(itemsid) VALUES (?)";

      final var pstatement2 = connection.prepareStatement(sql_stuff);
      pstatement2.setInt(1, items_id);
      pstatement2.executeUpdate();

      success = true;
      connection.close();
    } catch (SQLException sqe) {
      System.out.println("Error Code = " + sqe.getErrorCode());
      System.out.println("SQL state = " + sqe.getSQLState());
      System.out.println("Message = " + sqe.getMessage());
      System.out.println("printTrace /n");
      sqe.printStackTrace();
    } catch (Exception e) {
      System.out.println("E message : " + e.getMessage());
    }

    return success;
  }

  public void generate(List<Items> itemsList, jakarta.servlet.http.HttpServletResponse response)
      throws DocumentException, IOException {
    // Creating the Object of Document
    Document document = new Document(PageSize.A4);
    // Getting instance of PdfWriter
    PdfWriter.getInstance(document, response.getOutputStream());
    // Opening the created document to change it
    document.open();
    // Creating font
    // Setting font style and size
    Font fontTiltle = FontFactory.getFont(FontFactory.COURIER_BOLD);
    fontTiltle.setSize(20);
    // Creating paragraph
    Paragraph paragraph1 = new Paragraph("VFIM Report", fontTiltle);
    // Aligning the paragraph in the document
    paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
    // Adding the created paragraph in the document
    document.add(paragraph1);
    // Creating a table of the 4 columns
    PdfPTable table = new PdfPTable(4);
    // Setting width of the table, its columns and spacing
    table.setWidthPercentage(100);
    table.setWidths(new int[] { 3, 3, 3, 3 });
    table.setSpacingBefore(5);
    // Create Table Cells for the table header
    PdfPCell cell = new PdfPCell();
    // Setting the background color and padding of the table cell
    cell.setBackgroundColor(CMYKColor.BLUE);
    cell.setPadding(5);
    // Creating font
    // Setting font style and size
    Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
    font.setColor(CMYKColor.WHITE);
    // Adding headings in the created table cell or header
    // Adding Cell to table
    cell.setPhrase(new Phrase("ID", font));
    table.addCell(cell);
    cell.setPhrase(new Phrase("Items Name", font));
    table.addCell(cell);
    cell.setPhrase(new Phrase("Quantity", font));
    table.addCell(cell);
    cell.setPhrase(new Phrase("Category", font));
    table.addCell(cell);
    // cell.setPhrase(new Phrase("Mobile No", font));
    // table.addCell(cell);
    // Iterating the list of students
    for (Items items : itemsList) {
      // Adding student id
      table.addCell(String.valueOf(items.getId()));
      // Adding student name
      table.addCell(items.getName());
      // Adding student email
      table.addCell(String.valueOf(items.getQuantity()));
      // Adding student mobile
      table.addCell(String.valueOf(items.getCategory()));
      // table.addCell(student.getMobileNo());
    }
    // Adding the created table to the document
    document.add(table);
    // Closing the document
    document.close();
  }

  public ArrayList<Items> getAllDate(Date date_from, Date date_to) {
    ArrayList<Items> items = new ArrayList<>();
    try (Connection connection = dataSource.getConnection()) {

      var statement = connection.prepareStatement(SELECT_ALL_DATE);
      statement.setDate(1, date_from);
      statement.setDate(2, date_to);

      final var resultSet = statement.executeQuery();

      while (resultSet.next()) {

        String category = "";
        int itemsid_i = resultSet.getInt("itemsid");
        String name = resultSet.getString("name");
        int quantity = resultSet.getInt("quantity");
        String status = resultSet.getString("status");
        String approval = resultSet.getString("approval");
        Date added_date = resultSet.getDate("added_date");
        Date expire_date = resultSet.getDate("expire_date");
        String location = resultSet.getString("location");

        if (expire_date != null) {
          category = "Dry Ingredient";
        } else if (location != null) {
          category = "Furniture";
        } else {
          category = "Wet Ingredient";
        }

        items.add(new Items(itemsid_i, name, quantity, status, approval, added_date, category));
      }
      connection.close();

    } catch (SQLException sqlex) {
      System.out.println("Error : " + sqlex.getSQLState());
      System.out.println("message : " + sqlex.getMessage());
    }
    return items;
  }

  public ArrayList<Items> getAllFoodDate(Date date_from, Date date_to) {
    ArrayList<Items> items = new ArrayList<>();
    try (Connection connection = dataSource.getConnection()) {
      System.out.println("date from dao : " + date_from);
      System.out.println("date to dao : " + date_to);
      var statement = connection.prepareStatement(SELECT_ALL_FOOD_DATE);
      statement.setDate(1, date_from);
      statement.setDate(2, date_to);

      final var resultSet = statement.executeQuery();

      while (resultSet.next()) {

        String category = "";
        int itemsid_i = resultSet.getInt("itemsid");
        String name = resultSet.getString("name");
        int quantity = resultSet.getInt("quantity");
        String status = resultSet.getString("status");
        String approval = resultSet.getString("approval");
        Date added_date = resultSet.getDate("added_date");
        Date expire_date = resultSet.getDate("expire_date");
        String location = resultSet.getString("location");

        if (expire_date != null) {
          category = "Dry Ingredient";
        } else if (location != null) {
          category = "Furniture";
        } else {
          category = "Wet Ingredient";
        }

        items.add(new Items(itemsid_i, name, quantity, status, approval, added_date, category));
        System.out.println("--->>> Data found....");
      }
      connection.close();

    } catch (SQLException sqlex) {
      System.out.println("Error : " + sqlex.getSQLState());
      System.out.println("message : " + sqlex.getMessage());
    }
    return items;
  }

  public ArrayList<Items> getAllFurnitureDate(Date date_from, Date date_to) {
    ArrayList<Items> items = new ArrayList<>();
    try (Connection connection = dataSource.getConnection()) {

      var statement = connection.prepareStatement(SELECT_ALL_FURNITURE_DATE);
      statement.setDate(1, date_from);
      statement.setDate(2, date_to);

      final var resultSet = statement.executeQuery();

      while (resultSet.next()) {

        String category = "";
        int itemsid_i = resultSet.getInt("itemsid");
        String name = resultSet.getString("name");
        int quantity = resultSet.getInt("quantity");
        String status = resultSet.getString("status");
        String approval = resultSet.getString("approval");
        Date added_date = resultSet.getDate("added_date");
        Date expire_date = resultSet.getDate("expire_date");
        String location = resultSet.getString("location");

        if (expire_date != null) {
          category = "Dry Ingredient";
        } else if (location != null) {
          category = "Furniture";
        } else {
          category = "Wet Ingredient";
        }

        items.add(new Items(itemsid_i, name, quantity, status, approval, added_date, category));
      }
      connection.close();

    } catch (SQLException sqlex) {
      System.out.println("Error : " + sqlex.getSQLState());
      System.out.println("message : " + sqlex.getMessage());
    }
    return items;
  }
}