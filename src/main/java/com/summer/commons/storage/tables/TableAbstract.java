package com.summer.commons.storage.tables;

import com.summer.commons.storage.Database;

import java.sql.Connection;

public abstract class TableAbstract {

    public TableAbstract() {
        setupTable();
    }

    public abstract void setupTable();

    public Connection getConnection() {
        return Database.getMySQL().openConnection();
    }
}
