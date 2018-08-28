package com;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Application {
    private static BasicDataSource dataSource;
    public static void main(String[] args) {
        initializeDatabase();
        SpringApplication.run(Application.class, args);
        System.out.println("\n\n\n");
    }
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
    private static void initializeDatabase() {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        String username = System.getenv("JDBC_DATABASE_USERNAME");
        String password = System.getenv("JDBC_DATABASE_PASSWORD");

        dataSource = new BasicDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaxWaitMillis(10000);
    }
    public static BasicDataSource getDataSource() {
        return dataSource;
    }
    public static List<String[]> query(String query) throws SQLException {
        System.out.println(query);
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            List<String[]> myList = new ArrayList<>();
            while(result.next()) {
                String[] arr = new String[result.getMetaData().getColumnCount()];
                for(int i = 0; i < arr.length; i++) {
                    arr[i] = result.getString(i+1);
                }
                myList.add(arr);
            }
            return myList;
        }
        catch(SQLException exception) {
            throw exception;
        }
        finally {
            if(statement != null)
                statement.close();
            if(connection != null)
                connection.close();
        }
    }
    public static void queryNoResults(String query) throws SQLException {
        System.out.println(query);
        dataSource.getConnection().createStatement().execute(query);
    }
    public static String getJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}