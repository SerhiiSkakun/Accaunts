package com.epam.accounts.utils;

import java.util.*;

public class Constants {

    private Constants() {}

    private static final Constants instance = new Constants();

    public static Constants getInstance() {
        return instance;
    }
    public static final String APP_PROPERTIES_PATH = "WEB-INF/accounts.properties";
    public static final String LOG4J_PROPERTIES_PATH = "WEB-INF/log4j.properties";

    public static String DB_CONNECTION_HOST = "localhost";
    public static String DB_CONNECTION_PORT = "5432";
    public static String DB_USER_NAME = "user";
    public static String DB_USER_PASSWORD = "user";

    public static final List<String> langCodes = Arrays.asList("en", "ru");
    public static String langCode = "en";
    public static ResourceBundle resourceBundle;

    public static void setConstantsFromProperties(Properties properties) {
        if(properties.getProperty("db.connection.host") != null) {DB_CONNECTION_HOST = properties.getProperty("db.connection.host");}
        if(properties.getProperty("db.connection.port") != null) {DB_CONNECTION_PORT = properties.getProperty("db.connection.port");}
        if(properties.getProperty("db.user.name") != null) {DB_USER_NAME = properties.getProperty("db.user.name");}
        if(properties.getProperty("db.user.password") != null) {DB_USER_PASSWORD = properties.getProperty("db.user.password");}
        if(properties.getProperty("lang.code") != null) {langCode = properties.getProperty("lang.code");}
    }




}
