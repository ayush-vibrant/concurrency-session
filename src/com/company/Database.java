package com.company;
import java.util.Objects;

public final class Database {
    private static Database database;

    private Database() {}

    // not thread safe
//    public static Database getInstance(){
//        if(database == null){
//            database = new Database();
//            database.connect("/usr/local/data/users.db");
//        } return database;
//    }

    public static Database getInstance() {
        if (database == null) {
            synchronized (Database.class) {
                if (database == null) {
                    database = new Database();
                    database.connect("/usr/local/data/users.db");
                }
            }
        }
        return database;
    }

    // Connects to the remote database.
    private void connect(String url) {
        Objects.requireNonNull(url);
    }

    public static void main(String[] args) {
        Database a = Database.getInstance();
        Database b = Database.getInstance();

        System.out.println(a == b);
    }
}

