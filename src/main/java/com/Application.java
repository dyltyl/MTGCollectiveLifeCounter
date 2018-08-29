package com;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

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
    public static String[][]query(String query) throws SQLException {
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
            return myList.toArray(new String[myList.size()][myList.get(0).length]);
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
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(query);
        statement.close();
        connection.close();
    }
    public static String getJson(Object object, boolean pretty) {
        ObjectMapper mapper = new ObjectMapper();
        if(pretty)
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return mapper.writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Configuration
    public class JacksonPrettyPrintConfiguration extends WebMvcConfigurationSupport {
        @Override
        protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
            for(HttpMessageConverter<?> converter : converters) {
                if(converter instanceof MappingJackson2HttpMessageConverter) {
                    MappingJackson2HttpMessageConverter jacksonConverter = (MappingJackson2HttpMessageConverter) converter;
                    jacksonConverter.setPrettyPrint(true);
                }
            }
        }
    }
}