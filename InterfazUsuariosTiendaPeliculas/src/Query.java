
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Andres
 */
public class Query {
     public void query(Datos user) {
       
        Connection connect = null;
        Statement st;
        ResultSet rs;
        
        try {
            connect = Conexion.getInstance().getConnection();

            if (connect != null) {
                st=connect.createStatement();
                rs=st.executeQuery("SELECT * FROM usuarios WHERE u ='"+user.getUsuario()+"'");
              
                  while(rs.next()){
                    user.setUsuario(rs.getString("u"));
                    user.setContraseña(rs.getString("c"));
                     
                  }
               
            } else {
                JOptionPane.showMessageDialog(null, "Conexion fallida");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        } finally {
            try {
                Conexion.getInstance().closeConnection(connect);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }

    }
}
