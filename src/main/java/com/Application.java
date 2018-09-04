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
    public static String[][] query(PreparedStatement statement) throws SQLException {
        String[][] resultArray = null;
        SQLException exception = null;
        ResultSet result = null;
        try {
            String pattern = "text\\(digest\\('.*'\\)\\)";
            String printableQuery = statement.toString().replaceAll(pattern, "'*******'");
            pattern = "digest\\('.*'\\)";
            printableQuery = printableQuery.replaceAll(pattern, "'*******'");
            System.out.println(printableQuery);
            result = statement.executeQuery();
            List<String[]> myList = new ArrayList<>();
            while(result.next()) {
                String[] arr = new String[result.getMetaData().getColumnCount()];
                for(int i = 0; i < arr.length; i++) {
                    arr[i] = result.getString(i+1);
                }
                myList.add(arr);
            }
            resultArray = myList.toArray(new String[myList.size()][result.getMetaData().getColumnCount()]);
        }
        catch(SQLException e) {
            exception = e;
        }
        finally {
            if(result != null && !result.isClosed())
                result.close();
            if(statement != null && !statement.isClosed())
                statement.close();
        }
        if(exception != null)
            throw exception;
        return resultArray;
    }
    public static void queryNoResults(PreparedStatement statement) throws SQLException {
        String pattern = "text\\(digest\\('.*'\\)\\)";
        String printableQuery = statement.toString().replaceAll(pattern, "'*******'");
        pattern = "digest\\('.*'\\)";
        printableQuery = printableQuery.replaceAll(pattern, "'*******'");
        System.out.println(printableQuery);
        statement.execute();
        statement.close();
    }
    public static String getJson(Object object, boolean pretty) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
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