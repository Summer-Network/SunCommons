package com.vulcanth.commons.storage.tables;

import com.vulcanth.commons.storage.Database;

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
