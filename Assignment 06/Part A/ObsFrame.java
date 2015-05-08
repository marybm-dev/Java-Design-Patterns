
/* Maria L Martinez - mlmartinez85@gmail.com
 * CS 4311 - Week 4 Assignment - Ex 6a
 * Winter 2015
 * 02/04/2015
 *
 * ObsFrame.java
 */

//import java.util.Observable;
//import java.util.Observer;

import java.awt.*;
import java.awt.event.*;
import static java.awt.FlowLayout.CENTER;

public class ObsFrame extends Frame implements ActionListener, Observer {

    private Supplier supplier = null;
    private TextField num;
    private TextField name;
    private TextField status;
    private TextField city;
    
  /** Constructor 
    * @param supplier - instance of Supplier that is being observed
    */ 
    public ObsFrame(Supplier supplier) {
        this.supplier = supplier;
        this.supplier.addObserver(this);
        
        // GUI objects
        num = new TextField(10);
        name = new TextField(10);
        status = new TextField(10);
        city = new TextField(10);
        Button btnUpdate = new Button("Update");
        Button btnExit = new Button("Exit");
        btnUpdate.addActionListener(this);
        btnExit.addActionListener(this);
        
        // Add GUI objects
        setLayout(new FlowLayout(CENTER,20,25));
        add(new Label("Supplier #: "));
        add(num);
        add(new Label("Name: "));
        add(name);
        add(new Label("Status: "));
        add(status);
        add(new Label("City: "));
        add(city);
        add(btnUpdate);
        add(btnExit);
        setSize(250,350);
        show();
        displaySupplier();
    }
    
   /** updates GUI objects with existing or new values
    */
    private void displaySupplier() {
        num.setText(supplier.getNumber());
        name.setText(supplier.getName());
        status.setText(supplier.getStatus());
        city.setText(supplier.getCity());
    }

   /* ActionListener's actionPerformed() method
    * @param event - ActionEvent called when button is selected
    */
    @Override
    public void actionPerformed(ActionEvent evt) {
        String arg = evt.getActionCommand();
        if (arg.equals("Exit"))
            System.exit(0);
        supplier.updateSupplier(num.getText(), name.getText(), status.getText(), city.getText());
    }

   /* Observer's update() method
    * @param Observable object - an object that is observed (Supplier in this particular case)
    */
    @Override
    public void update(Observable o) {
        displaySupplier();
    }
}
