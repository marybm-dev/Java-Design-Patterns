
/* Maria L Martinez - mlmartinez85@gmail.com
 * CS 4311 - Week 5 Assignment - Ex 6b
 * Winter 2015
 * 02/11/2015
 *
 * Supp.java
 * Implements Serializable to implement Snapshot pattern
 */

import java.io.*;

public class Supp implements Serializable {
    
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
    
}
