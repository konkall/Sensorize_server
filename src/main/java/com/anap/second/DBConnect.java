package com.anap.second;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The type Db connect.
 * Connects to Database taking the name,password and the path to database
 */
public class DBConnect {

    private static Connection conn;

    private static String url = "jdbc:mysql://localhost/sensorize?autoReconnect=true&useSSL=false";

    private static String user = "root";

    private static String pass = "overdose93";


    /**
     * Connect connection.
     *
     * @return the connection. Tries to connect to database and if there is no connection, return the error

     * @throws SQLException the sql exception
     */
    public static Connection connect() throws SQLException {

        try{

            Class.forName("com.mysql.jdbc.Driver").newInstance();

        }catch(ClassNotFoundException cnfe){

            System.err.println("Error: "+cnfe.getMessage());

        }catch(InstantiationException ie){

            System.err.println("Error: "+ie.getMessage());

        }catch(IllegalAccessException iae){

            System.err.println("Error: "+iae.getMessage());

        }



        conn = DriverManager.getConnection(url,user,pass);

        return conn;

    }



}