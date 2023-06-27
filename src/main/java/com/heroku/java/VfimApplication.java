package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestParam;

// import com.heroku.java.MODEL.Users;

// import org.springframework.web.bind.annotation.ModelAttribute;
import org.apache.logging.log4j.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

@SpringBootApplication
@Controller
public class VfimApplication {
  private final DataSource dataSource;
  // Object logger
  Logger logger = LogManager.getLogger(VfimApplication.class);

  @Autowired
  public VfimApplication(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @GetMapping("/database")
  String database(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      final var statement = connection.createStatement();
      statement.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      statement.executeUpdate("INSERT INTO ticks VALUES (now())");

      final var resultSet = statement.executeQuery("SELECT tick FROM ticks");
      final var output = new ArrayList<>();
      while (resultSet.next()) {
        output.add("Read from DB: " + resultSet.getTimestamp("tick"));
      }

      model.put("records", output);
      return "database";

    } catch (Throwable t) {
      model.put("message", t.getMessage());
      return "error";
    }
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // @Bean
  // public MessageSource messageSource() {
  // ReloadableResourceBundleMessageSource messageSource = new
  // ReloadableResourceBundleMessageSource();
  // messageSource.setBasename("classpath:messages");
  // messageSource.setDefaultEncoding("UTF-8");
  // return messageSource;
  // }

  public static void main(String[] args) {
    SpringApplication.run(VfimApplication.class, args);
  }
}
