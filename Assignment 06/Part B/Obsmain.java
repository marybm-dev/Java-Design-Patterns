
/* Maria L Martinez - mlmartinez85@gmail.com
 * CS 4311 - Week 5 Assignment - Ex 6b
 * Winter 2015
 * 02/11/2015
 *
 * Obsmain.java
 */

import static java.lang.Integer.parseInt;

public class Obsmain {
    
   /** @param args - command line arguments
    */
    public static void main(String[] args) {
        Integer n = parseInt(args[0]);          // 1
        Supplier s1 = new Supplier("S1");       // 2
        Supplier s2 = new Supplier("S2");       // 2
        
        for (int i = 0; i < n ; i++) {
            new ObsFrame(s1);
            new ObsFrame(s2);
        }
    }
    
}
