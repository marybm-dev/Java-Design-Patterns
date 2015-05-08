
/* Maria L Martinez - mlmartinez85@gmail.com
 * CS 4311 - Week 5 Assignment - Ex 6b
 * Winter 2015
 * 02/11/2015
 *
 * Supplier.java (Serialization)
 * extends Observable - this means objects can observe instances of this class
 * 
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Supplier extends Observable {
    public Supp supp;
    
  /** Constructor 
    * @param aNumber - a string representation of an ID #
    */ 
    public Supplier(String aNumber) {
        supp = new Supp(aNumber);
        loadSupplier();
    }
    
  /** updateSupplier() method 
    * Updates Supp member variable and notifies Observers of changes
    * @param aNumber - new string representation of supplier's ID number
    * @param aName - new string representation of supplier's name
    * @param aStatus - new string representation of supplier's status
    * @param aCity - new string representation of supplier's city
    */ 
    public void updateSupplier(String aNumber, String aName, String aStatus, String aCity) {
        supp.number = aNumber;
        supp.name = aName;
        supp.status = aStatus;
        supp.city = aCity;
        
        setChanged();
        notifyObservers();
        storeSupplier();
    }
    
  /** loadSupplier() method 
    * Reads data from disk
    */ 
    private void loadSupplier() {
        try {
            FileInputStream f = new FileInputStream(supp.number + ".dat");
            ObjectInputStream in = new ObjectInputStream(f);
            supp = (Supp) in.readObject();
            in.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
                Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
  /** storeSupplier() method 
    * Writes data to disk
    */ 
    private void storeSupplier() {
        try {
            FileOutputStream f = new FileOutputStream(supp.number + ".dat");
            ObjectOutputStream out = new ObjectOutputStream(f);
            out.writeObject(supp);
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
