
/* Maria L Martinez - mlmartinez85@gmail.com
 * CS 4311 - Week 4 Assignment - Ex 6a
 * Winter 2015
 * 02/04/2015
 *
 * DataBase.java
 * Stores/Udates data for this application
 */

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBase {
    
   /** @param args - command line arguments
    */
    public static void main(String args[]) {
        DataBase db = new DataBase("SuppDB"); // 5 lines of JDBC
        ResultSet rs = db.execute("select * from S");
        try {
            while (rs.next())                               // 6th line of JDBC
                System.out.println(rs.getString("S_NO"));   // 7th line of JDBC
            rs.close();                                     // 8th line of JDBC
        } 
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
  
    // Member variables
    public static final String driverName = "org.sqlite.JDBC";
    private String url = "jdbc:sqlite:";
    private String dbName;

  /** Constructor
    * @param name - String name of the database
    */
    public DataBase(String name) {
        dbName = name;
        url = url + dbName;
    }
    
  /** execute() method 
    * @param query - SQL query to be executed
    * @return ResultSet - contains the data result from query call
    */
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

