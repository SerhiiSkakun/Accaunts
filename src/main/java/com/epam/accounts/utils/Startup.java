package com.epam.accounts.utils;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.*;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import com.epam.accounts.dao.pgDAO.PgDataSource;

@WebListener
public class Startup implements ServletContextListener {
    private static Logger logger;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("Servlet context initialization starts");
        ServletContext servletContext = event.getServletContext();
        initLog4j(servletContext); //init log4j
        logger = Logger.getLogger(Startup.class.getName());
        logger.info("Application exploded in: " + servletContext.getRealPath(""));
        initAppConstants(servletContext); //init constants
        PgDataSource.init(servletContext); //init connection pool
        initI18N(servletContext); //init nationalisation
        logger.info(this.getClass().getName() + ". Servlet context initialization finished");
//        System.out.println(Constants.resourceBundle.getString("table"));
    }

    private void initLog4j(ServletContext servletContext) {
        System.out.println("Log4j initialization started");
        try {
            PropertyConfigurator.configure(servletContext.getRealPath(Constants.LOG4J_PROPERTIES_PATH));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Log4j initialization finished");
    }

    private void initAppConstants(ServletContext servletContext) {
        logger.info("appConstants initialization started");
        Properties properties = new Properties();
        File appPropertiesFile = new File(servletContext.getRealPath(Constants.APP_PROPERTIES_PATH));
        if (appPropertiesFile.isFile() && appPropertiesFile.canRead()) {
            try (InputStream is = new FileInputStream(appPropertiesFile)) {
                logger.info("Reading from file " + appPropertiesFile.getName() + "...");
                properties.load(is);
            } catch (FileNotFoundException e) {
                logger.error("appPropertiesFile not found at: " + appPropertiesFile, e);
            } catch (IOException e) {
                logger.error("Can't read " + appPropertiesFile, e);
            }
        } else {
            logger.error("appPropertiesFile not found at: " + appPropertiesFile);
        }
        if (!properties.isEmpty()) {
            Constants.setConstantsFromProperties(properties);
        }
        logger.info("appConstants initialization finished");
    }

    private void initI18N(ServletContext servletContext) {
        logger.info("I18N subsystem initialization started");
        Constants.resourceBundle = ResourceBundle.getBundle("locale", new Locale(Constants.langCode));
        servletContext.setAttribute("locales", Constants.langCodes);
        logger.info("I18N subsystem initialization finished");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        PgDataSource.deactivate();
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }
}
