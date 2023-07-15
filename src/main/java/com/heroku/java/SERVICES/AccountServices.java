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
public class AccountServices {
  private final DataSource dataSource;
  private final SQLEx database;
  private final HttpSession session;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @Autowired
  public AccountServices(DataSource dataSource, SQLEx database, HttpSession session) {
    this.dataSource = dataSource;
    this.database = database;
    this.session = session;
  }

  public ArrayList<Accounts> getAllAccount() {
    ArrayList<Accounts> accounts = new ArrayList<>();

    try (Connection connection = dataSource.getConnection()) {
      final var statement = connection.createStatement();

      final var resultSet = statement
          .executeQuery("SELECT staffid, fullname, username, password, roles FROM staff ORDER BY staffid");

      int row = 0;

      while (resultSet.next()) {
        int staffid = resultSet.getInt("staffid");
        String fullname = resultSet.getString("fullname");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        String roles = resultSet.getString("roles");
        row++;
        accounts.add(new Accounts(staffid, fullname, username, password, roles));
      }

      connection.close();

    } catch (Throwable t) {
      System.out.println("message : " + t.getMessage());
    }
    return accounts;
  }

  public Accounts getAccountById(int id) {
    Accounts account = null;

    try {
      Connection connection = dataSource.getConnection();
      String sql = "SELECT * FROM staff WHERE staffid = ?";
      final var statement = connection.prepareStatement(sql);
      statement.setInt(1, id);

      final var resultSet = statement.executeQuery();
      // Process the result set
      while (resultSet.next()) {
        // Retrieve the values from the result set
        int staffId = resultSet.getInt("staffid");
        String fullname = resultSet.getString("fullname");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        String roles = resultSet.getString("roles");
        // Retrieve other columns as needed
        account = new Accounts(staffId, fullname, username, "password", roles);
      }

    } catch (Throwable t) {
      System.out.println("message : " + t.getMessage());
    }
    return account;
  }

  public String deleteAccountById(int id) {
    String url = "";
    try {
      Connection connection = dataSource.getConnection();
      // Statement stmt = connection.createStatement();
      String sql = "DELETE FROM staff WHERE staffid=? AND managerid IS NOT NULL";
      final var statement = connection.prepareStatement(sql);
      var sessionId = session.getAttribute("staffid").toString();
      // step4 execute query with logical
      if (id != 1) {
        if (id != Integer.parseInt(sessionId)) {
          statement.setInt(1, id);
          statement.executeUpdate();
          connection.close();
          System.out.println(">>>>>!! Supervisor [" + session.getAttribute("staffid") + "] delete account staff ["
              + id + "] !! <<<<<");
          url = "redirect:/accounts?delete_success=true";
        } else {
          url = "redirect:/accounts?error_code=102";
        }
      } else {
        url = "redirect:/accounts?error_code=101";
      }

    } catch (SQLException sqe) {
      System.out.println("Error Code = " + sqe.getErrorCode());
      System.out.println("SQL state = " + sqe.getSQLState());
      System.out.println("Message = " + sqe.getMessage());
      System.out.println("printTrace /n");
      sqe.printStackTrace();

      url = "redirect:/accounts/update-account?success=false";
    } catch (Exception e) {
      System.out.println("E message : " + e.getMessage());
      url = "redirect:/accounts/update-account?success=false";
    }
    return url;
  }

  public String updateAccount(int id, Accounts accounts) {
    String url = "";
    try {
      Connection connection = dataSource.getConnection();
      String sql = "UPDATE staff SET fullname=?, username=?, password=?, roles=? WHERE staffid=?";
      String sql_nopass = "UPDATE staff SET fullname=?, username=?, roles=? WHERE staffid=?";
      final var statement = connection.prepareStatement(sql);
      final var statement2 = connection.prepareStatement(sql_nopass);

      int staffid = id;
      String fullname = accounts.getFullname();
      String username = accounts.getUsername();
      String password = accounts.getPassword();
      String roles = accounts.getRoles();
      System.out.println("roles value : " + roles);
      // Check if password didnt change === "password"
      if (password.equals("password")) {
        statement2.setString(1, fullname);
        statement2.setString(2, username);
        statement2.setString(3, roles);
        statement2.setInt(4, staffid);
        statement2.executeUpdate();
      } else {
        statement.setString(1, fullname);
        statement.setString(2, username);
        statement.setString(3, passwordEncoder.encode(password));
        statement.setString(4, roles);
        statement.setInt(5, staffid);
        statement.executeUpdate();
      }
      System.out
          .println(">>>>> Supervisor [" + session.getAttribute("staffid") + "] update account staff [" + id + "]");
      connection.close();

      url = "redirect:/accounts?update_success=true";

    } catch (SQLException sqe) {
      System.out.println("Error Code = " + sqe.getErrorCode());
      System.out.println("SQL state = " + sqe.getSQLState());
      System.out.println("Message = " + sqe.getMessage());
      System.out.println("printTrace /n");
      sqe.printStackTrace();

      url = "redirect:/accounts/update-account?success=false";
    } catch (Exception e) {
      System.out.println("E message : " + e.getMessage());
      url = "redirect:/accounts/update-account?success=false";
    }

    return url;
  }

  public boolean insertAccount(Accounts acc) {
    boolean status = false;
    try {
      Connection connection = dataSource.getConnection();
      String sql = "INSERT INTO staff(fullname, username, password,roles,managerid) VALUES (?,?,?,?,?)";
      final var statement = connection.prepareStatement(sql);

      String fullname = acc.getFullname();
      String username = acc.getUsername();
      String password = acc.getPassword();
      String roles = acc.getRoles();

      // if (fullname.equals("") && username.equals("") && password.equals("")) {
      // connection.close();
      // System.out.println("First failed..");
      // return "redirect:/accounts/create-account?success=false";
      // } else {
      statement.setString(1, fullname);
      statement.setString(2, username);
      statement.setString(3, passwordEncoder.encode(password));
      statement.setString(4, roles);
      statement.setInt(5, (int) session.getAttribute("staffid"));
      // statement.setInt(5, 1);
      int rowsAffected = statement.executeUpdate();
      status = rowsAffected > 0;
      System.out.println(
          ">>>>> Supervisor [" + session.getAttribute("staffid") + "] create account staff [" + username + "]");
      connection.close();

      // }

    } catch (SQLException sqe) {
      System.out.println("Error Code = " + sqe.getErrorCode());
      System.out.println("SQL state = " + sqe.getSQLState());
      System.out.println("Message = " + sqe.getMessage());
      System.out.println("printTrace /n");
      sqe.printStackTrace();
      status = false;
    }
    return status;
  }

  public String loginHandler(Users user) {
    String returnPage = "";

    try (Connection connection = dataSource.getConnection()) {
      final var statement = connection.createStatement();

      final var resultSet = statement.executeQuery("SELECT staffid, username, password,roles FROM staff");

      while (resultSet.next()) {
        String username = resultSet.getString("username");
        String pwd = resultSet.getString("password");
        String roles = resultSet.getString("roles");
        int staffid = resultSet.getInt("staffid");
        if (user.getUsername() != "" && user.getPassword() != "") {
          if (username.equals(user.getUsername()) && passwordEncoder.matches(user.getPassword(), pwd)) {
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", roles);
            session.setAttribute("staffid", staffid);
            session.setMaxInactiveInterval(1440 * 60);
            System.out.println(">>>>  (" + staffid + ")[" + user.getUsername() + "] logged in...");
            returnPage = "redirect:/dashboard";
            break;
          } else {
            returnPage = "redirect:/login?error=true";
          }

        } else {
          returnPage = "redirect:/login?error=true";

        }
      }
      connection.close();
      

    } catch (Throwable t) {
      System.out.println("message : " + t.getMessage());
      returnPage = "error";
    }
    
    return returnPage;
  }

}
