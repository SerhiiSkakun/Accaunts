package com.epam.accounts.dao.pgDAO;

import com.epam.accounts.utils.Constants;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class PgDataSource {
    private static final Logger logger = Logger.getLogger(PgDataSource.class.getName());

    public static HikariDataSource hikariDataSource;
//    public static Context context;

    private PgDataSource() {}

    public static void init(ServletContext servletContext) {
        logger.info("DB pool initialization started");
        HikariConfig config = new HikariConfig();
        try {
            Class.forName("org.postgresql.Driver");
            config.setMaximumPoolSize(10);
            config.setJdbcUrl("jdbc:postgresql://" + Constants.DB_CONNECTION_HOST + ":" + Constants.DB_CONNECTION_PORT + "/accounts");
            config.setUsername(Constants.DB_USER_NAME);
            config.setPassword(Constants.DB_USER_PASSWORD);
            config.addDataSourceProperty( "cachePrepStmts" , "true" );
            config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
            config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
            hikariDataSource = new HikariDataSource(config);
//            try {
//                Properties props = new Properties();
//                props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory" );
//                props.put ("java:comp/env/jdbc/datasource", hikariDataSource);
//                context = new InitialContext (props);
//                context.bind("java:comp/env/jdbc/datasource", hikariDataSource);
//            } catch (NamingException e) {
//                e.printStackTrace();
//            }
        } catch (ClassNotFoundException e) {
            logger.error("Can't get sql driver instance", e);
        }
        logger.info("DB pool initialization finished");
    }

    public static void deactivate() {
        if (hikariDataSource != null) {
            hikariDataSource.close();
        }
    }

    public static Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }
}

