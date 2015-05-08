
/* Maria L Martinez - mlmartinez85@gmail.com
 * CS 4311 - Week 9 Assignment
 * Winter 2015
 * 02/25/2015
 *
 * DataBase.java
 * JDBC implementation to access databases
 */

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBase {
    
    public static final String driverName = "org.sqlite.JDBC";
    private String url = "jdbc:sqlite:";
    private String dbName;

    public DataBase(String name) {
        dbName = name;
        url = url + dbName;
    }

    public ResultSet execute(String query) {
        ResultSet rs = null;
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(driverName);
        } 
        catch (ClassNotFoundException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            stmt.execute(query); 
            rs = stmt.getResultSet(); 
        } 
        catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
}

