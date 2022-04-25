/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package farmacia;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author Personal
 */
public class conexion {
      Connection connect = null;
    public Connection conectar(){
        try {
//            Class.forName("com.mysql.jdbc.Driver");
              Class.forName("com.mysql.jdbc.Driver");
            //    Class.forName ("oracle.jdbc.driver.OracleDriver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/farmacia","root","");
          //  connect = DriverManager.getConnection("jdbc:oracle:thin:@172.21.106.37:1521:EABC","FARMACIA","FARMACIA");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Fallo en la conexion, comuniquese con el administrador");
        }
        return connect;
    }
    
    
    
}
