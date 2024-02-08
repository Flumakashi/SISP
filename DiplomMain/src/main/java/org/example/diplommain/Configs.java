package org.example.diplommain;

public class Configs {
    protected String dbHost = "localhost";
    protected String dbPort = "5432";
    protected String dbUser = "postgres";
    protected String dbPass = "1209qwpo";
    protected String dbName = "SISP";

    String url = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;
}
