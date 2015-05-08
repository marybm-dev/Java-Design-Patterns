
/* Maria L Martinez - mlmartinez85@gmail.com
 * CS 4311 - Week 7 Assignment - Ex 7
 * Winter 2015
 * 02/21/2015
 *
 * Supp.java
 * extends Observable - this means objects can observe instances of this class
 *
 */


package RMI;

import java.io.*;

public class Supp implements Serializable {
    // Instance variables
    public String number;
    public String name;
    public String status;
    public String city;
    
    /** Constructor
     * @param aNumber - a string representation of an ID #
     */
    public Supp(String aNumber) {
        number = aNumber;
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
}
