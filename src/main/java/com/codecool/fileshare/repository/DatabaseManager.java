package com.codecool.fileshare.repository;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DatabaseManager {

    public DataSource connect() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setDatabaseName(System.getenv().get("DATABASE_NAME"));
        dataSource.setUser(System.getenv().get("PSQL_USERNAME"));
        dataSource.setPassword(System.getenv().get("PSQL_PASSWORD"));


        System.out.println("Trying to connect...");
        dataSource.getConnection().close();
        System.out.println("Connection OK");
        return dataSource;
    }
}

