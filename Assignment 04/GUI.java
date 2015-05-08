
/* Maria L Martinez - mlmartinez85@gmail.com
 * CS 4311 - Week 4 Assignment
 * Winter 2015
 * 01/28/2015
 *
 * GUI.java
 * Output:
 *
 * marys-mbp:desktop mlmartinez85$ javac GUI.java
 * marys-mbp:desktop mlmartinez85$ java GUI 1
 *
 * Sun Menu Select
 * Sun Button Push
 *
 * marys-mbp:desktop mlmartinez85$ java GUI 2
 *
 * MS Menu Select
 * MS Button Push
 */



/* GUI.class
 * 
 * Intereface representation class
 * @param args - command line input for Factory #
 */

public class GUI {
    
    public static void main(String[] args) {
        // get the user input to select which Factory to use
        int code = Integer.parseInt(args[0]);
        
        // get the factory
        AbstractFactory f = AbstractFactory.getFactory(code);
        
        // create the menu for this factory
        Menu m = f.createMenu();
        
        // create the button for this factory
        Button b = f.createButton();
        
        // select the menu
        m.select();
        
        // push the button
        b.push();
    }
}



/* AbstractFactory.class
 *
 * Implementation of generic Factory
 */
abstract class AbstractFactory {

    public static SunFactory sun = new SunFactory();
    public static MSFactory ms = new MSFactory();
    
    public static AbstractFactory getFactory(int code) {
        switch(code) {
        case 1 : 
            return sun;
        default :
            return ms;
        }
        
    }
    
    public abstract Menu createMenu();
    public abstract Button createButton();
}

// SunFactory implementation
class SunFactory extends AbstractFactory {
    @Override
    public Menu createMenu() {
        return new SunMenu();
    }
    
    @Override
    public Button createButton() {
        return new SunButton();
    }
}

// MSFactory implementation
class MSFactory extends AbstractFactory {
    @Override
    public Menu createMenu() {
        return new MSMenu();
    }
    
    @Override
    public Button createButton() {
        return new MSButton();
    }
}



/* Menu.class
 *
 * Implementation of generic Menu
 */

class Menu {
    
    public void select(){
        
    }
}

// SunMenu implementation
class SunMenu extends Menu {
    @Override
    public void select() {
        System.out.println("Sun Menu Select");
    }
}

// MSMenu implementation
class MSMenu extends Menu {
    @Override
    public void select() {
        System.out.println("MS Menu Select");
    }
}



/* Button.class
 *
 * Implementation of generic Button
 */

class Button {
    public void push() {
        
    }
}

// SunMenu implementation
class SunButton extends Button {
    @Override
    public void push() {
        System.out.println("Sun Button Push");
    }
}

// MSMenu implementation
class MSButton extends Button {
    @Override
    public void push() {
        System.out.println("MS Button Push");
    }
}