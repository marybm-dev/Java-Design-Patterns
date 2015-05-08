
/* Maria L Martinez - mlmartinez85@gmail.com
 * CS 4311 - Week 4 Assignment - Ex 6a
 * Winter 2015
 * 02/04/2015
 *
 * Supplier.java
 * extends Observable - this means objects can observe instances of this class
 */

//import java.util.Observable;
//import java.util.Observer;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Supplier extends Observable {
    
    private String number;
    private String name;
    private String status;
    private String city;
    private DataBase db;
    
    
  /** Constructor 
    * @param aNumber - a string representation of an ID #
    */ 
    public Supplier(String aNumber) {
        number = aNumber;
        db = new DataBase("SuppDB");
        loadSupplier();
    }
    
    // Accessor methods
    public String getNumber() {
        return number;
    }
    
    public String getName() {
        return name;
    }
    
    public String getStatus() {
        return status;
    }
    
    public String getCity() {
        return city;
    }
    
  /** updateSupplier() method 
    * Updates member variable and notifies Observers of changes
    * @param aNumber - new string representation of supplier's ID number
    * @param aName - new string representation of supplier's name
    * @param aStatus - new string representation of supplier's status
    * @param aCity - new string representation of supplier's city
    */ 
    public void updateSupplier(String aNumber, String aName, String aStatus, String aCity) {
        number = aNumber;
        name = aName;
        status = aStatus;
        city = aCity;
        
        setChanged();
        notifyObservers();
        storeSupplier();
    }
    
  /** loadSupplier() method 
    * Gets data from database to store in an instance of Supplier
    */ 
    private void loadSupplier() {
        ResultSet rs = db.execute("select * from S where S_NO='" + number + "'");
        try {
            while(rs.next()) {
                name = rs.getString("SNAME");
                status = rs.getString("STATUS");
                city = rs.getString("CITY");
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
  /** storeSupplier() method 
    * Saves changes to data in datadabase
    */ 
    private void storeSupplier() {
        db.execute("update S set SNAME='" + name + "'" +
                              ", STATUS='" + status + "'" +
                              ", CITY='" + city + "'" +
                              "  where S_NO='" + number + "'"
        );
    }
}
