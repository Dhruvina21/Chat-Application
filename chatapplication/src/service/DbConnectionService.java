package service;

import java.sql.Connection;
import java.sql.DriverManager;

import static constants.DbConstants.*;

public class DbConnectionService {

    protected static Connection dbConnection;
    private static Connection dbConn(){
//      https://jdbc.postgresql.org/download/
        Connection c = null;
        try {
            Class.forName(DB_DRIVER);
            c = DriverManager
                    .getConnection(DB,
                            USER, PASSWORD);
            dbConnection=c;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return c;
    }

    public static void connectToDB(){
        System.out.println("Starting chat application .....");
        System.out.println("Connecting to Database .... ");
        //Connect to Db
        if(DbConnectionService.dbConn()==null){
            //Stopping if db connection failed
            System.exit(0);
        }
        System.out.println("Successfully connected to db");
    }


}
