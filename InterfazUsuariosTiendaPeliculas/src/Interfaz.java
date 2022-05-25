
import java.awt.Color;
import javax.swing.JOptionPane;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;


public class Interfaz extends javax.swing.JFrame {

   private int xMouse, yMouse;
   private PreparedStatement ps;
   private ResultSet rs;
   private ResultSetMetaData rsmd;
   private Statement st;
   
   private boolean modificar=false;
   private int C_P;
   
    public Interfaz() {
        initComponents();
        cargarTabla();
        txtUsuario.setForeground(Color.white);
        txtContraseña.setForeground(Color.gray);
        this.setLocationRelativeTo(null);
        frmMenu.setLocationRelativeTo(null);
        frmMenu.setSize(780, 580);
        frmTablas.setLocationRelativeTo(null);
        frmTablas.setSize(820, 600);
        frmRegistrar.setLocationRelativeTo(null);
        frmRegistrar.setSize(820, 600);

        txtUsuario.setFocusTraversalKeysEnabled(false);
        txtContraseña.setFocusTraversalKeysEnabled(false);
        btnAsignar.setEnabled(false);
        btnBorrar.setEnabled(false);
                   
       
      
        

    }

    private void verificar() {

        Datos user = new Datos();
        user.setUsuario(txtUsuario.getText());
        Query usuario = new Query();
        usuario.query(user);

        if (txtUsuario.getText().equals(user.getUsuario()) && String.valueOf(txtContraseña.getPassword()).equals(user.getContraseña())) {
            user.setValidacion(true);
        }

        if (user.isValidacion()) {
            txtUsuario.setText("");
            txtUsuario.requestFocus();
            txtContraseña.setForeground(Color.gray);
            txtContraseña.setText("**********");

            frmMenu.setVisible(true);
            frmMenu.setLocationRelativeTo(null);


        } else {
            JOptionPane.showMessageDialog(null, "Usuario y/o contraseña incorrecto");
        }

    }

    
    private void guardarPelicula(){
        String titulo = txtTitulo.getText();
        String actor = txtActor.getText();
        String categoria = txtCategoria.getText();
        String formato = txtFormato.getText();
        
      Connection connect = null;
       
        
        try {
            connect = Conexion.getInstance().getConnection();

            if (connect != null) {
               
                ps = connect.prepareStatement("INSERT INTO PELICULAS (titulo,actor,categoria,formato) VALUES(?,?,?,?);");
                ps.setString(1, titulo);
                ps.setString(2, actor);
                ps.setString(3, categoria);
                ps.setString(4, formato);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "La pelicula ha sido registrada ");
                limpiar();
                cargarTabla();
               
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
   
    private void cargarTabla(){
        DefaultTableModel tabla =(DefaultTableModel) tblPeliculas.getModel();
        tabla.setRowCount(0);
        
        int columnas;
        Connection connect = null;

         try {
            connect = Conexion.getInstance().getConnection();

            if (connect != null) {
               
                ps = connect.prepareStatement("SELECT TITULO FROM PELICULAS;");
                rs = ps.executeQuery();
                rsmd = rs.getMetaData();
                columnas = rsmd.getColumnCount();
                
                while(rs.next()){
                    Object[] fila = new Object[columnas];
                    for(int indice=0; indice<columnas; indice++){
                    fila[indice]=rs.getObject(indice+1);
                    }
                    tabla.addRow(fila);
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

    private void modificar(){
    
        String titulo = txtTitulo.getText();
        String actor = txtActor.getText();
        String categoria = txtCategoria.getText();
        String formato = txtFormato.getText();
        
      Connection connect = null;
      
   
       
                try {
            connect = Conexion.getInstance().getConnection();

            if (connect != null) {
              
                ps = connect.prepareStatement("UPDATE PELICULAS SET TITULO=?, ACTOR=?, CATEGORIA=?, FORMATO=? WHERE C_P="+C_P+";");
                ps.setString(1, titulo);
                ps.setString(2, actor);
                ps.setString(3, categoria);
                ps.setString(4, formato);
                ps.executeUpdate();
                
                limpiar();
                cargarTabla();
               
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
    
    private void regresar(javax.swing.JFrame F) {
        F.setVisible(false);
    }

    private void borrar(){

      Connection connect = null;
    
   
       
                try {
            connect = Conexion.getInstance().getConnection();

            if (connect != null) {
              
                ps = connect.prepareStatement("DELETE FROM PELICULAS WHERE C_P="+C_P+";");
                
                ps.execute();
                
                limpiar();
                cargarTabla();
               modificar=false;
               btnRegistrarP.setText("REGISTRAR");
               JOptionPane.showMessageDialog(null, "Se ha eliminado el registro de la pelicula");
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
    
    private void buscar(){
        
        DefaultTableModel tabla =(DefaultTableModel) tblPeliculas.getModel();
        tabla.setRowCount(0);
        
        int columnas;
        Connection connect = null;

         try {
            connect = Conexion.getInstance().getConnection();

            if (connect != null) {
               
                ps = connect.prepareStatement("SELECT TITULO FROM PELICULAS WHERE "+cmbBuscar.getSelectedItem()+" LIKE '"+txtBuscar.getText()+"%' ;");
                rs = ps.executeQuery();
                rsmd = rs.getMetaData();
                columnas = rsmd.getColumnCount();
                
                while(rs.next()){
                    Object[] fila = new Object[columnas];
                    for(int indice=0; indice<columnas; indice++){
                    fila[indice]=rs.getObject(indice+1);
                    }
                    tabla.addRow(fila);
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
    
    private void asignar(){
    
        String miembro_id=txtAsignar.getText();
        
        Connection connect = null;
      
        
        try {
            connect = Conexion.getInstance().getConnection();

            if (connect != null) {
               
                ps = connect.prepareStatement("INSERT INTO ENVIOS (C_P, MIEMBRO_ID) VALUES(?,?);");
                ps.setInt(1, C_P);
                ps.setString(2, miembro_id);
              
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Se le asigno el codigo de pelicula: " + C_P + "con el ID del miembro: " + miembro_id);
                limpiar();
                cargarTabla();
               
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
    
    private void mostrarTablas(){
    
        DefaultTableModel tabla1 =(DefaultTableModel) tblPeliculasID.getModel();
        DefaultTableModel tabla3 =(DefaultTableModel) tblEnvios.getModel();
        DefaultTableModel tabla2=(DefaultTableModel) tblMiembros.getModel();
        
        tabla1.setRowCount(0);
        tabla2.setRowCount(0);
        tabla3.setRowCount(0);
        
        int columnas;
        Connection connect = null;

         try {
            connect = Conexion.getInstance().getConnection();

            if (connect != null) {
               
                ps = connect.prepareStatement("SELECT C_P, TITULO FROM PELICULAS;");
                rs = ps.executeQuery();
                rsmd = rs.getMetaData();
                columnas = rsmd.getColumnCount();
                
                while(rs.next()){
                    Object[] fila = new Object[columnas];
                    for(int indice=0; indice<columnas; indice++){
                    fila[indice]=rs.getObject(indice+1);
                    }
                    tabla1.addRow(fila);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Conexion fallida");
            }
            
            if (connect != null) {
               
                ps = connect.prepareStatement("SELECT MIEMBRO_ID, NOMBRE FROM MIEMBROS;");
                rs = ps.executeQuery();
                rsmd = rs.getMetaData();
                columnas = rsmd.getColumnCount();
                
                while(rs.next()){
                    Object[] fila = new Object[columnas];
                    for(int indice=0; indice<columnas; indice++){
                    fila[indice]=rs.getObject(indice+1);
                    }
                    tabla2.addRow(fila);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Conexion fallida");
            }
            if (connect != null) {
               
                ps = connect.prepareStatement("SELECT MIEMBRO_ID, C_P FROM ENVIOS;");
                rs = ps.executeQuery();
                rsmd = rs.getMetaData();
                columnas = rsmd.getColumnCount();
                
                while(rs.next()){
                    Object[] fila = new Object[columnas];
                    for(int indice=0; indice<columnas; indice++){
                    fila[indice]=rs.getObject(indice+1);
                    }
                    tabla3.addRow(fila);
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
    
    private void registrarMiembro(){
     
     String nombre = txtNombre.getText();
     String correo = txtCorreo.getText();
     String telefono = txtTelefono.getText();
     String direccion = txtDireccion.getText();
     Connection connect = null;
      
        
        try {
            connect = Conexion.getInstance().getConnection();

            if (connect != null) {
               
                ps = connect.prepareStatement("INSERT INTO MIEMBROS (NOMBRE, CORRREO, TELEFONO, DIRECCION) VALUES(?,?,?,?);");
                ps.setString(1, nombre);
                ps.setString(2, correo);
                ps.setString(3, telefono);
                ps.setString(4,direccion );
              
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Se registro el nuevo miembro");
                limpiar();
                cargarTabla();
               
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
    
    private void buscarID(){
        String ID = txtID.getText();
         Connection connect = null;
        try{
        
       
             connect = Conexion.getInstance().getConnection();

            if (connect != null) {
               
                ps = connect.prepareStatement("SELECT NOMBRE,TELEFONO,CORREO,DIRECCION FROM MIEMBROS WHERE MIEMBRO_ID=?;");
                ps.setString(1,ID );
                rs = ps.executeQuery();
                
                while(rs.next()){
                
                    txtNombre.setText(rs.getString("NOMBRE"));
                    txtTelefono.setText(rs.getString("TELEFONO"));
                    txtCorreo.setText(rs.getString("CORREO"));
                    txtDireccion.setText(rs.getString("DIRECCION"));                    
                  
                }
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.toString());
        }
        
}
    
   private void limpiar(){
   
       txtTitulo.setText("");
       txtActor.setText("");
       txtCategoria.setText("");
       txtFormato.setText("");
       
   }
   

   



   

    private void cambiarFrame(javax.swing.JFrame C, javax.swing.JFrame A) {
        C.setVisible(false);
        A.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        frmMenu = new javax.swing.JFrame();
        jPanel5 = new javax.swing.JPanel();
        panDatos1 = new javax.swing.JPanel();
        etiUsuario1 = new javax.swing.JLabel();
        etiUsuario3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel8 = new javax.swing.JPanel();
        etiUsuario19 = new javax.swing.JLabel();
        cmbBuscar = new javax.swing.JComboBox<>();
        etiUsuario2 = new javax.swing.JLabel();
        etiUsuario4 = new javax.swing.JLabel();
        etiUsuario5 = new javax.swing.JLabel();
        etiUsuario6 = new javax.swing.JLabel();
        etiUsuario21 = new javax.swing.JLabel();
        etiUsuario22 = new javax.swing.JLabel();
        panUsuario2 = new javax.swing.JPanel();
        btnRegistrarP = new javax.swing.JLabel();
        panUsuario3 = new javax.swing.JPanel();
        btnRegistrarP1 = new javax.swing.JLabel();
        panUsuario24 = new javax.swing.JPanel();
        btnBorrar = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jSeparator9 = new javax.swing.JSeparator();
        jSeparator10 = new javax.swing.JSeparator();
        etiUsuario7 = new javax.swing.JLabel();
        jSeparator13 = new javax.swing.JSeparator();
        panUsuario25 = new javax.swing.JPanel();
        btnMostrarId = new javax.swing.JLabel();
        panUsuario26 = new javax.swing.JPanel();
        btnAsignar = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        txtAsignar = new javax.swing.JTextField();
        txtTitulo = new javax.swing.JTextField();
        txtCategoria = new javax.swing.JTextField();
        txtActor = new javax.swing.JTextField();
        txtFormato = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPeliculas = new javax.swing.JTable();
        panMinimizar2 = new javax.swing.JPanel();
        etiMinimizar2 = new javax.swing.JLabel();
        panCerrar2 = new javax.swing.JPanel();
        etiCerrar2 = new javax.swing.JLabel();
        etiBarra3 = new javax.swing.JLabel();
        frmTablas = new javax.swing.JFrame();
        jPanel10 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        panMinimizar1 = new javax.swing.JPanel();
        etiMinimizar1 = new javax.swing.JLabel();
        panCerrar1 = new javax.swing.JPanel();
        etiCerrar1 = new javax.swing.JLabel();
        etiBarra2 = new javax.swing.JLabel();
        panDatos2 = new javax.swing.JPanel();
        etiUsuario11 = new javax.swing.JLabel();
        Miembros = new javax.swing.JLabel();
        etiUsuario12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEnvios = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblMiembros = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblPeliculasID = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        frmRegistrar = new javax.swing.JFrame();
        jPanel11 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        panMinimizar3 = new javax.swing.JPanel();
        etiMinimizar3 = new javax.swing.JLabel();
        panCerrar3 = new javax.swing.JPanel();
        etiCerrar3 = new javax.swing.JLabel();
        etiBarra4 = new javax.swing.JLabel();
        panDatos3 = new javax.swing.JPanel();
        panEntrar3 = new javax.swing.JPanel();
        etiRegistrarMiembro = new javax.swing.JLabel();
        etiUsuario15 = new javax.swing.JLabel();
        etiUsuario17 = new javax.swing.JLabel();
        etiUsuario20 = new javax.swing.JLabel();
        etiUsuario23 = new javax.swing.JLabel();
        jSeparator14 = new javax.swing.JSeparator();
        jSeparator15 = new javax.swing.JSeparator();
        jSeparator16 = new javax.swing.JSeparator();
        jPanel12 = new javax.swing.JPanel();
        etiUsuario24 = new javax.swing.JLabel();
        etiUsuario25 = new javax.swing.JLabel();
        jSeparator18 = new javax.swing.JSeparator();
        etiBuscar1 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtCorreo = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        etiUsuario26 = new javax.swing.JLabel();
        jSeparator19 = new javax.swing.JSeparator();
        txtID = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        panInterfaz = new javax.swing.JPanel();
        panDatos = new javax.swing.JPanel();
        panEntrar = new javax.swing.JPanel();
        etiEntrar = new javax.swing.JLabel();
        etiContraseña = new javax.swing.JLabel();
        etiUsuario = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        txtUsuario = new javax.swing.JTextField();
        txtContraseña = new javax.swing.JPasswordField();
        panCerrar = new javax.swing.JPanel();
        etiCerrar = new javax.swing.JLabel();
        panMinimizar = new javax.swing.JPanel();
        etiMinimizar = new javax.swing.JLabel();
        etiBarra = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        panRegistrar = new javax.swing.JPanel();
        btnRegistrar = new javax.swing.JLabel();
        etiFondoI = new javax.swing.JLabel();

        frmMenu.setUndecorated(true);

        jPanel5.setBackground(new java.awt.Color(0, 37, 57));
        jPanel5.setForeground(new java.awt.Color(51, 51, 51));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panDatos1.setBackground(new java.awt.Color(0, 0, 0));
        panDatos1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        etiUsuario1.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        etiUsuario1.setForeground(new java.awt.Color(255, 255, 255));
        etiUsuario1.setText("FORMATO");
        panDatos1.add(etiUsuario1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 280, 70, 20));

        etiUsuario3.setBackground(new java.awt.Color(0, 0, 0));
        etiUsuario3.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        etiUsuario3.setForeground(new java.awt.Color(255, 255, 255));
        etiUsuario3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiUsuario3.setText("Registrar pelicula");
        panDatos1.add(etiUsuario3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 200, 410, 29));
        panDatos1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 630, 10));

        jPanel8.setBackground(new java.awt.Color(226, 8, 19));
        jPanel8.setToolTipText("");

        etiUsuario19.setBackground(new java.awt.Color(255, 102, 102));
        etiUsuario19.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        etiUsuario19.setForeground(new java.awt.Color(255, 255, 255));
        etiUsuario19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiUsuario19.setText("REGRESAR");
        etiUsuario19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                etiUsuario19MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(etiUsuario19, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(etiUsuario19, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        panDatos1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 390, 110, 50));

        cmbBuscar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Actor", "Titulo", "Categoria", "Formato" }));
        cmbBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbBuscarActionPerformed(evt);
            }
        });
        panDatos1.add(cmbBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 100, 90, -1));

        etiUsuario2.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        etiUsuario2.setForeground(new java.awt.Color(255, 255, 255));
        etiUsuario2.setText("Buscar por...");
        panDatos1.add(etiUsuario2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 79, 110, 20));

        etiUsuario4.setBackground(new java.awt.Color(0, 0, 0));
        etiUsuario4.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        etiUsuario4.setForeground(new java.awt.Color(255, 255, 255));
        etiUsuario4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiUsuario4.setText("Peliculas disponibles");
        panDatos1.add(etiUsuario4, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 19, 410, 29));

        etiUsuario5.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        etiUsuario5.setForeground(new java.awt.Color(255, 255, 255));
        etiUsuario5.setText("ASIGNAR POR ID");
        panDatos1.add(etiUsuario5, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 80, 130, 20));

        etiUsuario6.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        etiUsuario6.setForeground(new java.awt.Color(255, 255, 255));
        etiUsuario6.setText("TÍTULO");
        panDatos1.add(etiUsuario6, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 250, 50, 20));

        etiUsuario21.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        etiUsuario21.setForeground(new java.awt.Color(255, 255, 255));
        etiUsuario21.setText("ACTOR");
        panDatos1.add(etiUsuario21, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 280, 50, 20));

        etiUsuario22.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        etiUsuario22.setForeground(new java.awt.Color(255, 255, 255));
        etiUsuario22.setText("CATEGORÍA");
        panDatos1.add(etiUsuario22, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 250, 90, 20));

        panUsuario2.setToolTipText("");

        btnRegistrarP.setBackground(new java.awt.Color(255, 102, 102));
        btnRegistrarP.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        btnRegistrarP.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrarP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnRegistrarP.setText("REGISTRAR");
        btnRegistrarP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRegistrarPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRegistrarPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRegistrarPMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panUsuario2Layout = new javax.swing.GroupLayout(panUsuario2);
        panUsuario2.setLayout(panUsuario2Layout);
        panUsuario2Layout.setHorizontalGroup(
            panUsuario2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
            .addGroup(panUsuario2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(btnRegistrarP, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
        );
        panUsuario2Layout.setVerticalGroup(
            panUsuario2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
            .addGroup(panUsuario2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(btnRegistrarP, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
        );

        panDatos1.add(panUsuario2, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 330, 180, 40));

        panUsuario3.setToolTipText("");

        btnRegistrarP1.setBackground(new java.awt.Color(255, 102, 102));
        btnRegistrarP1.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        btnRegistrarP1.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrarP1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnRegistrarP1.setText("LIMPIAR");
        btnRegistrarP1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRegistrarP1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRegistrarP1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRegistrarP1MouseExited(evt);
            }
        });

        javax.swing.GroupLayout panUsuario3Layout = new javax.swing.GroupLayout(panUsuario3);
        panUsuario3.setLayout(panUsuario3Layout);
        panUsuario3Layout.setHorizontalGroup(
            panUsuario3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnRegistrarP1, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
        );
        panUsuario3Layout.setVerticalGroup(
            panUsuario3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnRegistrarP1, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        panDatos1.add(panUsuario3, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 330, 130, 40));

        panUsuario24.setToolTipText("");

        btnBorrar.setBackground(new java.awt.Color(255, 102, 102));
        btnBorrar.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        btnBorrar.setForeground(new java.awt.Color(255, 255, 255));
        btnBorrar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnBorrar.setText("BORRAR DEL CATALOGO");
        btnBorrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBorrarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBorrarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBorrarMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panUsuario24Layout = new javax.swing.GroupLayout(panUsuario24);
        panUsuario24.setLayout(panUsuario24Layout);
        panUsuario24Layout.setHorizontalGroup(
            panUsuario24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnBorrar, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
        );
        panUsuario24Layout.setVerticalGroup(
            panUsuario24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panUsuario24Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panDatos1.add(panUsuario24, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 220, 40));
        panDatos1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 140, 10));
        panDatos1.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 270, 160, 10));
        panDatos1.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 300, 160, 10));
        panDatos1.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 270, 160, 10));
        panDatos1.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 300, 160, 10));

        etiUsuario7.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        etiUsuario7.setForeground(new java.awt.Color(255, 255, 255));
        etiUsuario7.setText("BUSCAR PELICULAS");
        panDatos1.add(etiUsuario7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 130, -1));
        panDatos1.add(jSeparator13, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 120, 130, 10));

        panUsuario25.setToolTipText("");

        btnMostrarId.setBackground(new java.awt.Color(255, 102, 102));
        btnMostrarId.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        btnMostrarId.setForeground(new java.awt.Color(255, 255, 255));
        btnMostrarId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnMostrarId.setText("MOSTRAR ID's");
        btnMostrarId.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMostrarIdMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMostrarIdMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMostrarIdMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panUsuario25Layout = new javax.swing.GroupLayout(panUsuario25);
        panUsuario25.setLayout(panUsuario25Layout);
        panUsuario25Layout.setHorizontalGroup(
            panUsuario25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 130, Short.MAX_VALUE)
            .addGroup(panUsuario25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(btnMostrarId, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
        );
        panUsuario25Layout.setVerticalGroup(
            panUsuario25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
            .addGroup(panUsuario25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(btnMostrarId, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
        );

        panDatos1.add(panUsuario25, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 30, 130, 30));

        panUsuario26.setToolTipText("");

        btnAsignar.setBackground(new java.awt.Color(255, 102, 102));
        btnAsignar.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        btnAsignar.setForeground(new java.awt.Color(255, 255, 255));
        btnAsignar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnAsignar.setText("ASIGNAR");
        btnAsignar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAsignarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAsignarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAsignarMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panUsuario26Layout = new javax.swing.GroupLayout(panUsuario26);
        panUsuario26.setLayout(panUsuario26Layout);
        panUsuario26Layout.setHorizontalGroup(
            panUsuario26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnAsignar, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
        );
        panUsuario26Layout.setVerticalGroup(
            panUsuario26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panUsuario26Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnAsignar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panDatos1.add(panUsuario26, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 140, 130, 40));

        txtBuscar.setBackground(new java.awt.Color(0, 0, 0));
        txtBuscar.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        txtBuscar.setForeground(new java.awt.Color(204, 204, 204));
        txtBuscar.setBorder(null);
        txtBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtBuscarMousePressed(evt);
            }
        });
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarKeyTyped(evt);
            }
        });
        panDatos1.add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 140, -1));

        txtAsignar.setBackground(new java.awt.Color(0, 0, 0));
        txtAsignar.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        txtAsignar.setForeground(new java.awt.Color(204, 204, 204));
        txtAsignar.setBorder(null);
        txtAsignar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtAsignarMousePressed(evt);
            }
        });
        txtAsignar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAsignarKeyPressed(evt);
            }
        });
        panDatos1.add(txtAsignar, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 100, 130, -1));

        txtTitulo.setBackground(new java.awt.Color(0, 0, 0));
        txtTitulo.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        txtTitulo.setForeground(new java.awt.Color(204, 204, 204));
        txtTitulo.setBorder(null);
        txtTitulo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtTituloMousePressed(evt);
            }
        });
        txtTitulo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTituloKeyPressed(evt);
            }
        });
        panDatos1.add(txtTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 250, 160, -1));

        txtCategoria.setBackground(new java.awt.Color(0, 0, 0));
        txtCategoria.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        txtCategoria.setForeground(new java.awt.Color(204, 204, 204));
        txtCategoria.setBorder(null);
        txtCategoria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtCategoriaMousePressed(evt);
            }
        });
        txtCategoria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCategoriaKeyPressed(evt);
            }
        });
        panDatos1.add(txtCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 250, 160, -1));

        txtActor.setBackground(new java.awt.Color(0, 0, 0));
        txtActor.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        txtActor.setForeground(new java.awt.Color(204, 204, 204));
        txtActor.setBorder(null);
        txtActor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtActorMousePressed(evt);
            }
        });
        txtActor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtActorKeyPressed(evt);
            }
        });
        panDatos1.add(txtActor, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 280, 160, -1));

        txtFormato.setBackground(new java.awt.Color(0, 0, 0));
        txtFormato.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        txtFormato.setForeground(new java.awt.Color(204, 204, 204));
        txtFormato.setBorder(null);
        txtFormato.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtFormatoMousePressed(evt);
            }
        });
        txtFormato.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFormatoKeyPressed(evt);
            }
        });
        panDatos1.add(txtFormato, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 280, 160, -1));

        tblPeliculas.setBackground(new java.awt.Color(0, 0, 0));
        tblPeliculas.setForeground(new java.awt.Color(255, 255, 255));
        tblPeliculas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Peliculas"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPeliculas.setSelectionBackground(new java.awt.Color(0, 0, 0));
        tblPeliculas.setSelectionForeground(new java.awt.Color(255, 255, 255));
        tblPeliculas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPeliculasMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblPeliculas);

        panDatos1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 70, 210, 110));

        jPanel5.add(panDatos1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 690, 460));

        etiMinimizar2.setFont(new java.awt.Font("Haettenschweiler", 1, 48)); // NOI18N
        etiMinimizar2.setForeground(new java.awt.Color(255, 255, 255));
        etiMinimizar2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiMinimizar2.setText("-");
        etiMinimizar2.setToolTipText("");
        etiMinimizar2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        etiMinimizar2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                etiMinimizar2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                etiMinimizar2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                etiMinimizar2MouseExited(evt);
            }
        });

        javax.swing.GroupLayout panMinimizar2Layout = new javax.swing.GroupLayout(panMinimizar2);
        panMinimizar2.setLayout(panMinimizar2Layout);
        panMinimizar2Layout.setHorizontalGroup(
            panMinimizar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMinimizar2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(etiMinimizar2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panMinimizar2Layout.setVerticalGroup(
            panMinimizar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMinimizar2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(etiMinimizar2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel5.add(panMinimizar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        etiCerrar2.setBackground(new java.awt.Color(255, 255, 255));
        etiCerrar2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        etiCerrar2.setForeground(new java.awt.Color(255, 255, 255));
        etiCerrar2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiCerrar2.setText("X");
        etiCerrar2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                etiCerrar2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                etiCerrar2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                etiCerrar2MouseExited(evt);
            }
        });

        javax.swing.GroupLayout panCerrar2Layout = new javax.swing.GroupLayout(panCerrar2);
        panCerrar2.setLayout(panCerrar2Layout);
        panCerrar2Layout.setHorizontalGroup(
            panCerrar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
            .addGroup(panCerrar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panCerrar2Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(etiCerrar2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        panCerrar2Layout.setVerticalGroup(
            panCerrar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
            .addGroup(panCerrar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panCerrar2Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(etiCerrar2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel5.add(panCerrar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 0, 40, 40));

        etiBarra3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pelicula.png"))); // NOI18N
        etiBarra3.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                etiBarra3MouseDragged(evt);
            }
        });
        etiBarra3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                etiBarra3MousePressed(evt);
            }
        });
        jPanel5.add(etiBarra3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 580));

        javax.swing.GroupLayout frmMenuLayout = new javax.swing.GroupLayout(frmMenu.getContentPane());
        frmMenu.getContentPane().setLayout(frmMenuLayout);
        frmMenuLayout.setHorizontalGroup(
            frmMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        frmMenuLayout.setVerticalGroup(
            frmMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        frmTablas.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        frmTablas.setAlwaysOnTop(true);
        frmTablas.setUndecorated(true);
        frmTablas.setResizable(false);
        frmTablas.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(0, 37, 57));
        jPanel6.setForeground(new java.awt.Color(51, 51, 51));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        etiMinimizar1.setFont(new java.awt.Font("Haettenschweiler", 1, 48)); // NOI18N
        etiMinimizar1.setForeground(new java.awt.Color(255, 255, 255));
        etiMinimizar1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiMinimizar1.setText("-");
        etiMinimizar1.setToolTipText("");
        etiMinimizar1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        etiMinimizar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                etiMinimizar1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                etiMinimizar1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                etiMinimizar1MouseExited(evt);
            }
        });

        javax.swing.GroupLayout panMinimizar1Layout = new javax.swing.GroupLayout(panMinimizar1);
        panMinimizar1.setLayout(panMinimizar1Layout);
        panMinimizar1Layout.setHorizontalGroup(
            panMinimizar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
            .addGroup(panMinimizar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panMinimizar1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(etiMinimizar1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        panMinimizar1Layout.setVerticalGroup(
            panMinimizar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
            .addGroup(panMinimizar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panMinimizar1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(etiMinimizar1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel6.add(panMinimizar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        etiCerrar1.setBackground(new java.awt.Color(255, 255, 255));
        etiCerrar1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        etiCerrar1.setForeground(new java.awt.Color(255, 255, 255));
        etiCerrar1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiCerrar1.setText("X");
        etiCerrar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                etiCerrar1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                etiCerrar1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                etiCerrar1MouseExited(evt);
            }
        });

        javax.swing.GroupLayout panCerrar1Layout = new javax.swing.GroupLayout(panCerrar1);
        panCerrar1.setLayout(panCerrar1Layout);
        panCerrar1Layout.setHorizontalGroup(
            panCerrar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
            .addGroup(panCerrar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panCerrar1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(etiCerrar1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        panCerrar1Layout.setVerticalGroup(
            panCerrar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
            .addGroup(panCerrar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panCerrar1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(etiCerrar1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel6.add(panCerrar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 0, 40, 40));

        etiBarra2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                etiBarra2MouseDragged(evt);
            }
        });
        etiBarra2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                etiBarra2MousePressed(evt);
            }
        });
        jPanel6.add(etiBarra2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 820, 40));

        panDatos2.setBackground(new java.awt.Color(0, 0, 0));
        panDatos2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        etiUsuario11.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        etiUsuario11.setForeground(new java.awt.Color(255, 255, 255));
        etiUsuario11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiUsuario11.setText("Envios");
        panDatos2.add(etiUsuario11, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 270, 270, 29));

        Miembros.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        Miembros.setForeground(new java.awt.Color(255, 255, 255));
        Miembros.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Miembros.setText("Miembros");
        panDatos2.add(Miembros, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 260, 29));

        etiUsuario12.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        etiUsuario12.setForeground(new java.awt.Color(255, 255, 255));
        etiUsuario12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiUsuario12.setText("ID de peliculas");
        panDatos2.add(etiUsuario12, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, 270, 29));

        tblEnvios.setBackground(new java.awt.Color(0, 0, 0));
        tblEnvios.setForeground(new java.awt.Color(255, 255, 255));
        tblEnvios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID del miembro", "ID de la pelicula"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEnvios.setSelectionBackground(new java.awt.Color(0, 0, 0));
        tblEnvios.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(tblEnvios);
        if (tblEnvios.getColumnModel().getColumnCount() > 0) {
            tblEnvios.getColumnModel().getColumn(0).setResizable(false);
            tblEnvios.getColumnModel().getColumn(1).setResizable(false);
        }

        panDatos2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 310, 310, 180));

        tblMiembros.setBackground(new java.awt.Color(0, 0, 0));
        tblMiembros.setForeground(new java.awt.Color(255, 255, 255));
        tblMiembros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMiembros.setSelectionBackground(new java.awt.Color(0, 0, 0));
        tblMiembros.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jScrollPane3.setViewportView(tblMiembros);
        if (tblMiembros.getColumnModel().getColumnCount() > 0) {
            tblMiembros.getColumnModel().getColumn(0).setResizable(false);
            tblMiembros.getColumnModel().getColumn(1).setResizable(false);
        }

        panDatos2.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 340, 180));

        tblPeliculasID.setBackground(new java.awt.Color(0, 0, 0));
        tblPeliculasID.setForeground(new java.awt.Color(255, 255, 255));
        tblPeliculasID.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Titulo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPeliculasID.setSelectionBackground(new java.awt.Color(0, 0, 0));
        tblPeliculasID.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jScrollPane4.setViewportView(tblPeliculasID);
        if (tblPeliculasID.getColumnModel().getColumnCount() > 0) {
            tblPeliculasID.getColumnModel().getColumn(0).setResizable(false);
            tblPeliculasID.getColumnModel().getColumn(1).setResizable(false);
        }

        panDatos2.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 60, 310, 180));

        jPanel6.add(panDatos2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 690, 500));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pelicula.png"))); // NOI18N
        jPanel6.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 820, 610));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 820, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 620, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        frmTablas.getContentPane().add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -5, 820, 620));

        frmRegistrar.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        frmRegistrar.setUndecorated(true);
        frmRegistrar.setResizable(false);
        frmRegistrar.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBackground(new java.awt.Color(0, 37, 57));
        jPanel9.setForeground(new java.awt.Color(51, 51, 51));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        etiMinimizar3.setFont(new java.awt.Font("Haettenschweiler", 1, 48)); // NOI18N
        etiMinimizar3.setForeground(new java.awt.Color(255, 255, 255));
        etiMinimizar3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiMinimizar3.setText("-");
        etiMinimizar3.setToolTipText("");
        etiMinimizar3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        etiMinimizar3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                etiMinimizar3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                etiMinimizar3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                etiMinimizar3MouseExited(evt);
            }
        });

        javax.swing.GroupLayout panMinimizar3Layout = new javax.swing.GroupLayout(panMinimizar3);
        panMinimizar3.setLayout(panMinimizar3Layout);
        panMinimizar3Layout.setHorizontalGroup(
            panMinimizar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
            .addGroup(panMinimizar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panMinimizar3Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(etiMinimizar3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        panMinimizar3Layout.setVerticalGroup(
            panMinimizar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
            .addGroup(panMinimizar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panMinimizar3Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(etiMinimizar3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel9.add(panMinimizar3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        etiCerrar3.setBackground(new java.awt.Color(255, 255, 255));
        etiCerrar3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        etiCerrar3.setForeground(new java.awt.Color(255, 255, 255));
        etiCerrar3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiCerrar3.setText("X");
        etiCerrar3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                etiCerrar3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                etiCerrar3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                etiCerrar3MouseExited(evt);
            }
        });

        javax.swing.GroupLayout panCerrar3Layout = new javax.swing.GroupLayout(panCerrar3);
        panCerrar3.setLayout(panCerrar3Layout);
        panCerrar3Layout.setHorizontalGroup(
            panCerrar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
            .addGroup(panCerrar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panCerrar3Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(etiCerrar3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        panCerrar3Layout.setVerticalGroup(
            panCerrar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
            .addGroup(panCerrar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panCerrar3Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(etiCerrar3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel9.add(panCerrar3, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 0, 40, 40));

        etiBarra4.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                etiBarra4MouseDragged(evt);
            }
        });
        etiBarra4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                etiBarra4MousePressed(evt);
            }
        });
        jPanel9.add(etiBarra4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 820, 40));

        panDatos3.setBackground(new java.awt.Color(0, 0, 0));
        panDatos3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        etiRegistrarMiembro.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        etiRegistrarMiembro.setForeground(new java.awt.Color(255, 255, 255));
        etiRegistrarMiembro.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiRegistrarMiembro.setText("REGRISTRAR");
        etiRegistrarMiembro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        etiRegistrarMiembro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                etiRegistrarMiembroMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                etiRegistrarMiembroMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                etiRegistrarMiembroMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panEntrar3Layout = new javax.swing.GroupLayout(panEntrar3);
        panEntrar3.setLayout(panEntrar3Layout);
        panEntrar3Layout.setHorizontalGroup(
            panEntrar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
            .addGroup(panEntrar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panEntrar3Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(etiRegistrarMiembro, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        panEntrar3Layout.setVerticalGroup(
            panEntrar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(panEntrar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panEntrar3Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(etiRegistrarMiembro, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        panDatos3.add(panEntrar3, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 360, 220, 50));

        etiUsuario15.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        etiUsuario15.setForeground(new java.awt.Color(255, 255, 255));
        etiUsuario15.setText("ID");
        panDatos3.add(etiUsuario15, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 80, 80, 20));

        etiUsuario17.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        etiUsuario17.setForeground(new java.awt.Color(255, 255, 255));
        etiUsuario17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiUsuario17.setText("REGISTRAR MIEMBRO");
        panDatos3.add(etiUsuario17, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 19, 410, 29));

        etiUsuario20.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        etiUsuario20.setForeground(new java.awt.Color(255, 255, 255));
        etiUsuario20.setText("CORREO");
        panDatos3.add(etiUsuario20, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 139, 80, 30));

        etiUsuario23.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        etiUsuario23.setForeground(new java.awt.Color(255, 255, 255));
        etiUsuario23.setText("TELÉFONO");
        panDatos3.add(etiUsuario23, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 199, 80, 40));
        panDatos3.add(jSeparator14, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 120, 250, 10));
        panDatos3.add(jSeparator15, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 192, 250, 10));
        panDatos3.add(jSeparator16, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 250, 250, 10));

        jPanel12.setBackground(new java.awt.Color(226, 8, 19));
        jPanel12.setToolTipText("");

        etiUsuario24.setBackground(new java.awt.Color(255, 102, 102));
        etiUsuario24.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        etiUsuario24.setForeground(new java.awt.Color(255, 255, 255));
        etiUsuario24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiUsuario24.setText("REGRESAR");
        etiUsuario24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                etiUsuario24MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(etiUsuario24, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(etiUsuario24, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        panDatos3.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 360, 120, 50));

        etiUsuario25.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        etiUsuario25.setForeground(new java.awt.Color(255, 255, 255));
        etiUsuario25.setText("DIRECCIÓN");
        panDatos3.add(etiUsuario25, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 269, 80, 40));
        panDatos3.add(jSeparator18, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 322, 250, 10));

        etiBuscar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar.png"))); // NOI18N
        etiBuscar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                etiBuscar1MouseClicked(evt);
            }
        });
        panDatos3.add(etiBuscar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 80, 60, 40));

        txtNombre.setBackground(new java.awt.Color(0, 0, 0));
        txtNombre.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        txtNombre.setForeground(new java.awt.Color(204, 204, 204));
        txtNombre.setBorder(null);
        txtNombre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtNombreMousePressed(evt);
            }
        });
        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreKeyPressed(evt);
            }
        });
        panDatos3.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, 250, -1));

        txtCorreo.setBackground(new java.awt.Color(0, 0, 0));
        txtCorreo.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        txtCorreo.setForeground(new java.awt.Color(204, 204, 204));
        txtCorreo.setBorder(null);
        txtCorreo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtCorreoMousePressed(evt);
            }
        });
        txtCorreo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCorreoKeyPressed(evt);
            }
        });
        panDatos3.add(txtCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 170, 250, -1));

        txtTelefono.setBackground(new java.awt.Color(0, 0, 0));
        txtTelefono.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        txtTelefono.setForeground(new java.awt.Color(204, 204, 204));
        txtTelefono.setBorder(null);
        txtTelefono.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtTelefonoMousePressed(evt);
            }
        });
        txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelefonoKeyPressed(evt);
            }
        });
        panDatos3.add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 230, 250, -1));

        txtDireccion.setBackground(new java.awt.Color(0, 0, 0));
        txtDireccion.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        txtDireccion.setForeground(new java.awt.Color(204, 204, 204));
        txtDireccion.setBorder(null);
        txtDireccion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtDireccionMousePressed(evt);
            }
        });
        txtDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDireccionKeyPressed(evt);
            }
        });
        panDatos3.add(txtDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 300, 250, -1));

        etiUsuario26.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        etiUsuario26.setForeground(new java.awt.Color(255, 255, 255));
        etiUsuario26.setText("NOMBRE");
        panDatos3.add(etiUsuario26, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 79, 80, 20));
        panDatos3.add(jSeparator19, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 120, 150, 10));

        txtID.setBackground(new java.awt.Color(0, 0, 0));
        txtID.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        txtID.setForeground(new java.awt.Color(204, 204, 204));
        txtID.setBorder(null);
        txtID.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtIDMousePressed(evt);
            }
        });
        txtID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIDKeyPressed(evt);
            }
        });
        panDatos3.add(txtID, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 100, 150, -1));

        jPanel9.add(panDatos3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 660, 490));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pelicula.png"))); // NOI18N
        jPanel9.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 820, 610));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 820, Short.MAX_VALUE)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 620, Short.MAX_VALUE)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        frmRegistrar.getContentPane().add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -5, 820, 620));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setUndecorated(true);
        setResizable(false);

        panInterfaz.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panDatos.setBackground(new java.awt.Color(0, 0, 0));
        panDatos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        etiEntrar.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        etiEntrar.setForeground(new java.awt.Color(255, 255, 255));
        etiEntrar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiEntrar.setText("ENTRAR");
        etiEntrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        etiEntrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                etiEntrarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                etiEntrarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                etiEntrarMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panEntrarLayout = new javax.swing.GroupLayout(panEntrar);
        panEntrar.setLayout(panEntrarLayout);
        panEntrarLayout.setHorizontalGroup(
            panEntrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
            .addGroup(panEntrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(etiEntrar, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
        );
        panEntrarLayout.setVerticalGroup(
            panEntrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
            .addGroup(panEntrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(etiEntrar, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
        );

        panDatos.add(panEntrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 70, 40));

        etiContraseña.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        etiContraseña.setForeground(new java.awt.Color(255, 255, 255));
        etiContraseña.setText("CONTRASEÑA");
        panDatos.add(etiContraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 130, 29));

        etiUsuario.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        etiUsuario.setForeground(new java.awt.Color(255, 255, 255));
        etiUsuario.setText("USUARIO");
        panDatos.add(etiUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 19, 130, 29));
        panDatos.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 120, 10));
        panDatos.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 120, 10));

        txtUsuario.setBackground(new java.awt.Color(0, 0, 0));
        txtUsuario.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        txtUsuario.setForeground(new java.awt.Color(204, 204, 204));
        txtUsuario.setBorder(null);
        txtUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtUsuarioMousePressed(evt);
            }
        });
        txtUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUsuarioKeyPressed(evt);
            }
        });
        panDatos.add(txtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 120, -1));

        txtContraseña.setBackground(new java.awt.Color(0, 0, 0));
        txtContraseña.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        txtContraseña.setForeground(new java.awt.Color(204, 204, 204));
        txtContraseña.setText("**********");
        txtContraseña.setBorder(null);
        txtContraseña.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtContraseñaMousePressed(evt);
            }
        });
        txtContraseña.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtContraseñaKeyPressed(evt);
            }
        });
        panDatos.add(txtContraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 120, -1));

        panInterfaz.add(panDatos, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 150, 150, 230));

        etiCerrar.setBackground(new java.awt.Color(255, 255, 255));
        etiCerrar.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        etiCerrar.setForeground(new java.awt.Color(255, 255, 255));
        etiCerrar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiCerrar.setText("X");
        etiCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                etiCerrarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                etiCerrarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                etiCerrarMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panCerrarLayout = new javax.swing.GroupLayout(panCerrar);
        panCerrar.setLayout(panCerrarLayout);
        panCerrarLayout.setHorizontalGroup(
            panCerrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
            .addGroup(panCerrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panCerrarLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(etiCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        panCerrarLayout.setVerticalGroup(
            panCerrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
            .addGroup(panCerrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panCerrarLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(etiCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        panInterfaz.add(panCerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 0, 40, 40));

        etiMinimizar.setFont(new java.awt.Font("Haettenschweiler", 1, 48)); // NOI18N
        etiMinimizar.setForeground(new java.awt.Color(255, 255, 255));
        etiMinimizar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiMinimizar.setText("-");
        etiMinimizar.setToolTipText("");
        etiMinimizar.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        etiMinimizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                etiMinimizarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                etiMinimizarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                etiMinimizarMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panMinimizarLayout = new javax.swing.GroupLayout(panMinimizar);
        panMinimizar.setLayout(panMinimizarLayout);
        panMinimizarLayout.setHorizontalGroup(
            panMinimizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
            .addGroup(panMinimizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panMinimizarLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(etiMinimizar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        panMinimizarLayout.setVerticalGroup(
            panMinimizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
            .addGroup(panMinimizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panMinimizarLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(etiMinimizar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        panInterfaz.add(panMinimizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        etiBarra.setBackground(new java.awt.Color(255, 255, 255));
        etiBarra.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        etiBarra.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                etiBarraMouseDragged(evt);
            }
        });
        etiBarra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                etiBarraMousePressed(evt);
            }
        });
        panInterfaz.add(etiBarra, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 620, 40));

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

        jLabel3.setBackground(new java.awt.Color(0, 0, 0));
        jLabel3.setFont(new java.awt.Font("Century Gothic", 3, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Usuarios");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 320, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        panInterfaz.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 70, 320, 50));

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));

        jLabel4.setBackground(new java.awt.Color(0, 0, 0));
        jLabel4.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Crear nuevo miembro");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panInterfaz.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 400, 210, 50));

        btnRegistrar.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        btnRegistrar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnRegistrar.setText("Registrar");
        btnRegistrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRegistrarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRegistrarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRegistrarMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panRegistrarLayout = new javax.swing.GroupLayout(panRegistrar);
        panRegistrar.setLayout(panRegistrarLayout);
        panRegistrarLayout.setHorizontalGroup(
            panRegistrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnRegistrar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        panRegistrarLayout.setVerticalGroup(
            panRegistrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnRegistrar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        panInterfaz.add(panRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 400, 100, 50));

        etiFondoI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pelicula.png"))); // NOI18N
        panInterfaz.add(etiFondoI, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 620, 510));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panInterfaz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panInterfaz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void etiBarraMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiBarraMousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_etiBarraMousePressed

    private void etiBarraMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiBarraMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_etiBarraMouseDragged

    private void etiMinimizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiMinimizarMouseClicked
        this.setExtendedState(1);
    }//GEN-LAST:event_etiMinimizarMouseClicked

    private void etiCerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiCerrarMouseClicked
        System.exit(0);
    }//GEN-LAST:event_etiCerrarMouseClicked

    private void etiCerrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiCerrarMouseEntered
        panCerrar.setBackground(Color.red);
        etiCerrar.setForeground(Color.black);
    }//GEN-LAST:event_etiCerrarMouseEntered

    private void etiMinimizarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiMinimizarMouseEntered
        panMinimizar.setBackground(Color.red);
        etiMinimizar.setForeground(Color.black);
    }//GEN-LAST:event_etiMinimizarMouseEntered

    private void etiMinimizarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiMinimizarMouseExited
        panMinimizar.setBackground(new Color(214, 217, 223));
        etiMinimizar.setForeground(Color.white);

    }//GEN-LAST:event_etiMinimizarMouseExited

    private void etiCerrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiCerrarMouseExited
        panCerrar.setBackground(new Color(214, 217, 223));
        etiCerrar.setForeground(Color.white);

    }//GEN-LAST:event_etiCerrarMouseExited

    private void txtUsuarioMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtUsuarioMousePressed

        if (txtUsuario.getText().equals("Ingresa tu usuario...")) {
            txtUsuario.setText("");
            txtUsuario.setForeground(Color.white);
        }
        if (String.valueOf(txtContraseña.getPassword()).isEmpty()) {
            txtContraseña.setText("**********");
            txtContraseña.setForeground(Color.gray);
        }
    }//GEN-LAST:event_txtUsuarioMousePressed

    private void txtContraseñaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtContraseñaMousePressed
        if (String.valueOf(txtContraseña.getPassword()).equals("**********")) {
            txtContraseña.setText("");
            txtContraseña.setForeground(Color.white);
        }
        if (txtUsuario.getText().isEmpty()) {
            txtUsuario.setText("Ingresa tu usuario...");
            txtUsuario.setForeground(Color.gray);
        }

    }//GEN-LAST:event_txtContraseñaMousePressed

    private void etiEntrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiEntrarMouseClicked
        verificar();

    }//GEN-LAST:event_etiEntrarMouseClicked

    private void txtContraseñaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContraseñaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            if (txtUsuario.getText().equals("Ingresa tu usuario...")) {
                txtUsuario.setText("");
                txtUsuario.setForeground(Color.white);
            }
            if (String.valueOf(txtContraseña.getPassword()).isEmpty()) {
                txtContraseña.setText("**********");
                txtContraseña.setForeground(Color.gray);
            }
            txtUsuario.requestFocus();
        }

        Datos v = new Datos();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            verificar();
        }
    }//GEN-LAST:event_txtContraseñaKeyPressed

    private void txtUsuarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuarioKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_TAB || evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (String.valueOf(txtContraseña.getPassword()).equals("**********")) {
                txtContraseña.setText("");
                txtContraseña.setForeground(Color.white);
            }
            if (txtUsuario.getText().isEmpty()) {
                txtUsuario.setText("Ingresa tu usuario...");
                txtUsuario.setForeground(Color.gray);
            }
            txtContraseña.requestFocus();

        }

    }//GEN-LAST:event_txtUsuarioKeyPressed

    private void cmbBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBuscarActionPerformed
       buscar();
    }//GEN-LAST:event_cmbBuscarActionPerformed

    private void btnRegistrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseClicked
        frmRegistrar.setVisible(true);
                frmRegistrar.setLocationRelativeTo(null);

    }//GEN-LAST:event_btnRegistrarMouseClicked

    private void etiBarra2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiBarra2MousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_etiBarra2MousePressed

    private void etiCerrar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiCerrar1MouseClicked
        frmTablas.dispose();
    }//GEN-LAST:event_etiCerrar1MouseClicked

    private void etiCerrar1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiCerrar1MouseEntered
        panCerrar1.setBackground(Color.red);
        etiCerrar1.setForeground(Color.black);
    }//GEN-LAST:event_etiCerrar1MouseEntered

    private void etiCerrar1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiCerrar1MouseExited
        panCerrar1.setBackground(new Color(214, 217, 223));
        etiCerrar1.setForeground(Color.white);
    }//GEN-LAST:event_etiCerrar1MouseExited

    private void etiMinimizar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiMinimizar1MouseClicked
        frmTablas.setExtendedState(1);
    }//GEN-LAST:event_etiMinimizar1MouseClicked

    private void etiMinimizar1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiMinimizar1MouseEntered
        panMinimizar1.setBackground(Color.red);
        etiMinimizar1.setForeground(Color.black);
    }//GEN-LAST:event_etiMinimizar1MouseEntered

    private void etiMinimizar1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiMinimizar1MouseExited
        panMinimizar1.setBackground(new Color(214, 217, 223));
        etiMinimizar1.setForeground(Color.white);
    }//GEN-LAST:event_etiMinimizar1MouseExited

    private void etiMinimizar2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiMinimizar2MouseClicked
        frmMenu.setExtendedState(1);
    }//GEN-LAST:event_etiMinimizar2MouseClicked

    private void etiMinimizar2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiMinimizar2MouseEntered
        panMinimizar2.setBackground(Color.red);
        etiMinimizar2.setForeground(Color.black);
    }//GEN-LAST:event_etiMinimizar2MouseEntered

    private void etiMinimizar2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiMinimizar2MouseExited
        panMinimizar2.setBackground(new Color(214, 217, 223));
        etiMinimizar2.setForeground(Color.white);
    }//GEN-LAST:event_etiMinimizar2MouseExited

    private void etiCerrar2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiCerrar2MouseClicked
        frmMenu.dispose();
    }//GEN-LAST:event_etiCerrar2MouseClicked

    private void etiCerrar2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiCerrar2MouseEntered
        panCerrar2.setBackground(Color.red);
        etiCerrar2.setForeground(Color.black);
    }//GEN-LAST:event_etiCerrar2MouseEntered

    private void etiCerrar2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiCerrar2MouseExited
        panCerrar2.setBackground(new Color(214, 217, 223));
        etiCerrar2.setForeground(Color.white);
    }//GEN-LAST:event_etiCerrar2MouseExited

    private void etiBarra3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiBarra3MousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();               
    }//GEN-LAST:event_etiBarra3MousePressed

    private void etiBarra3MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiBarra3MouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        frmMenu.setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_etiBarra3MouseDragged

    private void etiUsuario19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiUsuario19MouseClicked
        frmMenu.dispose();
    }//GEN-LAST:event_etiUsuario19MouseClicked

    private void etiBarra2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiBarra2MouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        frmTablas.setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_etiBarra2MouseDragged

    private void etiEntrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiEntrarMouseEntered
        panEntrar.setBackground(Color.red);
        etiEntrar.setForeground(Color.black);
    }//GEN-LAST:event_etiEntrarMouseEntered

    private void etiEntrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiEntrarMouseExited
        panEntrar.setBackground(new Color(51,51,55));
       etiEntrar.setForeground(Color.white);
    }//GEN-LAST:event_etiEntrarMouseExited

    private void btnRegistrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseEntered
        panRegistrar.setBackground(Color.red);
        btnRegistrar.setForeground(Color.black);
    }//GEN-LAST:event_btnRegistrarMouseEntered

    private void btnRegistrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseExited
         panRegistrar.setBackground(new Color(51,51,55));
       btnRegistrar.setForeground(Color.white);
    }//GEN-LAST:event_btnRegistrarMouseExited

    private void btnRegistrarPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarPMouseClicked
        if(txtTitulo.getText().equals("")||txtCategoria.getText().equals("")||txtActor.getText().equals("")||txtFormato.getText().equals("")){
        JOptionPane.showMessageDialog(null,"Hay campos vacios");
        }else{
            if(modificar==false){
            guardarPelicula();
            
            }else{
                modificar();
                btnRegistrarP.setText("REGISTRAR");
                modificar=false;
            }
        }
    }//GEN-LAST:event_btnRegistrarPMouseClicked

    private void btnRegistrarPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarPMouseEntered
        panUsuario2.setBackground(Color.red);
        etiUsuario2.setForeground(Color.black);
    }//GEN-LAST:event_btnRegistrarPMouseEntered

    private void btnRegistrarPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarPMouseExited
        panUsuario2.setBackground(new Color(51,51,55));
        etiUsuario2.setForeground(Color.white);
    }//GEN-LAST:event_btnRegistrarPMouseExited

    private void btnBorrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBorrarMouseClicked
        borrar();
        
    }//GEN-LAST:event_btnBorrarMouseClicked

    private void btnBorrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBorrarMouseEntered
        panUsuario24.setBackground(Color.red);
        btnBorrar.setForeground(Color.black);
    }//GEN-LAST:event_btnBorrarMouseEntered

    private void btnBorrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBorrarMouseExited
        panUsuario24.setBackground(new Color(51,51,55));
        btnBorrar.setForeground(Color.white);
    }//GEN-LAST:event_btnBorrarMouseExited

    private void btnMostrarIdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMostrarIdMouseClicked
        frmTablas.setVisible(true);
        frmTablas.setLocationRelativeTo(frmRegistrar);
        mostrarTablas();
    }//GEN-LAST:event_btnMostrarIdMouseClicked

    private void btnMostrarIdMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMostrarIdMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMostrarIdMouseEntered

    private void btnMostrarIdMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMostrarIdMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMostrarIdMouseExited

    private void txtBuscarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBuscarMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarMousePressed

    private void txtBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarKeyPressed

    private void txtAsignarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAsignarMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAsignarMousePressed

    private void txtAsignarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAsignarKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAsignarKeyPressed

    private void txtTituloMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTituloMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTituloMousePressed

    private void txtTituloKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTituloKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTituloKeyPressed

    private void txtCategoriaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCategoriaMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCategoriaMousePressed

    private void txtCategoriaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCategoriaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCategoriaKeyPressed

    private void txtActorMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtActorMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtActorMousePressed

    private void txtActorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtActorKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtActorKeyPressed

    private void txtFormatoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFormatoMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFormatoMousePressed

    private void txtFormatoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFormatoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFormatoKeyPressed

    private void tblPeliculasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPeliculasMouseClicked
        
        Connection connect = null;
        try{
        
            int fila = tblPeliculas.getSelectedRow();
            String pelicula = tblPeliculas.getValueAt(fila,0).toString();
            
             connect = Conexion.getInstance().getConnection();

            if (connect != null) {
               
                ps = connect.prepareStatement("SELECT actor,categoria,formato,C_P FROM PELICULAS WHERE titulo=?;");
                ps.setString(1,pelicula );
                rs = ps.executeQuery();
                
                while(rs.next()){
                
                    txtTitulo.setText(pelicula);
                    txtActor.setText(rs.getString("actor"));
                    txtCategoria.setText(rs.getString("categoria"));
                    txtFormato.setText(rs.getString("Formato"));
                    btnRegistrarP.setText("MODIFICAR");
                    C_P=(rs.getInt("C_P"));
                    modificar=true;
                    btnBorrar.setEnabled(true);
                    btnAsignar.setEnabled(true);
                }
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.toString());
        }
    }//GEN-LAST:event_tblPeliculasMouseClicked

    private void txtBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyTyped
        buscar();
    }//GEN-LAST:event_txtBuscarKeyTyped

    private void btnAsignarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAsignarMouseClicked
        asignar();
    }//GEN-LAST:event_btnAsignarMouseClicked

    private void btnAsignarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAsignarMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAsignarMouseEntered

    private void btnAsignarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAsignarMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAsignarMouseExited

    private void etiMinimizar3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiMinimizar3MouseClicked
        frmRegistrar.setExtendedState(1);
    }//GEN-LAST:event_etiMinimizar3MouseClicked

    private void etiMinimizar3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiMinimizar3MouseEntered
        panMinimizar3.setBackground(Color.red);
        etiMinimizar3.setForeground(Color.black);
    }//GEN-LAST:event_etiMinimizar3MouseEntered

    private void etiMinimizar3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiMinimizar3MouseExited
        panMinimizar3.setBackground(new Color(214, 217, 223));
        etiMinimizar3.setForeground(Color.white);
    }//GEN-LAST:event_etiMinimizar3MouseExited

    private void etiCerrar3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiCerrar3MouseClicked
        frmRegistrar.dispose();
    }//GEN-LAST:event_etiCerrar3MouseClicked

    private void etiCerrar3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiCerrar3MouseEntered
        panCerrar3.setBackground(Color.red);
        etiCerrar3.setForeground(Color.black);
    }//GEN-LAST:event_etiCerrar3MouseEntered

    private void etiCerrar3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiCerrar3MouseExited
        panCerrar3.setBackground(new Color(214, 217, 223));
        etiCerrar3.setForeground(Color.white);
    }//GEN-LAST:event_etiCerrar3MouseExited

    private void etiBarra4MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiBarra4MouseDragged
        // TODO add your handling code here:
    }//GEN-LAST:event_etiBarra4MouseDragged

    private void etiBarra4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiBarra4MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_etiBarra4MousePressed

    private void etiRegistrarMiembroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiRegistrarMiembroMouseClicked
        if(txtNombre.getText().equals("")||txtCorreo.getText().equals("")||txtTelefono.getText().equals("")||txtDireccion.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Faltan campos por completar");
        }else{
            registrarMiembro();
        }
    }//GEN-LAST:event_etiRegistrarMiembroMouseClicked

    private void etiRegistrarMiembroMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiRegistrarMiembroMouseEntered
        panEntrar3.setBackground(Color.red);
        panEntrar3.setForeground(Color.black);
    }//GEN-LAST:event_etiRegistrarMiembroMouseEntered

    private void etiRegistrarMiembroMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiRegistrarMiembroMouseExited
        panEntrar3.setBackground(new Color(214, 217, 223));
        panEntrar3.setForeground(Color.white);
    }//GEN-LAST:event_etiRegistrarMiembroMouseExited

    private void etiUsuario24MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiUsuario24MouseClicked
        frmRegistrar.dispose();
    }//GEN-LAST:event_etiUsuario24MouseClicked

    private void txtNombreMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNombreMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreMousePressed

    private void txtNombreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreKeyPressed

    private void txtCorreoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCorreoMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoMousePressed

    private void txtCorreoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCorreoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoKeyPressed

    private void txtTelefonoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTelefonoMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoMousePressed

    private void txtTelefonoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoKeyPressed

    private void txtDireccionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDireccionMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionMousePressed

    private void txtDireccionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionKeyPressed

    private void txtIDMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtIDMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIDMousePressed

    private void txtIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIDKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIDKeyPressed

    private void etiBuscar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_etiBuscar1MouseClicked
        if(txtID.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Ingresa el ID que quieres buscar");
    
        }else{
        buscarID();
        }
    }//GEN-LAST:event_etiBuscar1MouseClicked

    private void btnRegistrarP1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarP1MouseClicked
        limpiar();
        btnBorrar.setEnabled(false);
        btnAsignar.setEnabled(false);
        btnRegistrarP.setText("REGISTRAR");
        modificar=false;
    }//GEN-LAST:event_btnRegistrarP1MouseClicked

    private void btnRegistrarP1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarP1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRegistrarP1MouseEntered

    private void btnRegistrarP1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarP1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRegistrarP1MouseExited

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interfaz().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Miembros;
    private javax.swing.JLabel btnAsignar;
    private javax.swing.JLabel btnBorrar;
    private javax.swing.JLabel btnMostrarId;
    private javax.swing.JLabel btnRegistrar;
    private javax.swing.JLabel btnRegistrarP;
    private javax.swing.JLabel btnRegistrarP1;
    private javax.swing.JComboBox<String> cmbBuscar;
    private javax.swing.JLabel etiBarra;
    private javax.swing.JLabel etiBarra2;
    private javax.swing.JLabel etiBarra3;
    private javax.swing.JLabel etiBarra4;
    private javax.swing.JLabel etiBuscar1;
    private javax.swing.JLabel etiCerrar;
    private javax.swing.JLabel etiCerrar1;
    private javax.swing.JLabel etiCerrar2;
    private javax.swing.JLabel etiCerrar3;
    private javax.swing.JLabel etiContraseña;
    private javax.swing.JLabel etiEntrar;
    private javax.swing.JLabel etiFondoI;
    private javax.swing.JLabel etiMinimizar;
    private javax.swing.JLabel etiMinimizar1;
    private javax.swing.JLabel etiMinimizar2;
    private javax.swing.JLabel etiMinimizar3;
    private javax.swing.JLabel etiRegistrarMiembro;
    private javax.swing.JLabel etiUsuario;
    private javax.swing.JLabel etiUsuario1;
    private javax.swing.JLabel etiUsuario11;
    private javax.swing.JLabel etiUsuario12;
    private javax.swing.JLabel etiUsuario15;
    private javax.swing.JLabel etiUsuario17;
    private javax.swing.JLabel etiUsuario19;
    private javax.swing.JLabel etiUsuario2;
    private javax.swing.JLabel etiUsuario20;
    private javax.swing.JLabel etiUsuario21;
    private javax.swing.JLabel etiUsuario22;
    private javax.swing.JLabel etiUsuario23;
    private javax.swing.JLabel etiUsuario24;
    private javax.swing.JLabel etiUsuario25;
    private javax.swing.JLabel etiUsuario26;
    private javax.swing.JLabel etiUsuario3;
    private javax.swing.JLabel etiUsuario4;
    private javax.swing.JLabel etiUsuario5;
    private javax.swing.JLabel etiUsuario6;
    private javax.swing.JLabel etiUsuario7;
    private javax.swing.JFrame frmMenu;
    private javax.swing.JFrame frmRegistrar;
    private javax.swing.JFrame frmTablas;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JPanel panCerrar;
    private javax.swing.JPanel panCerrar1;
    private javax.swing.JPanel panCerrar2;
    private javax.swing.JPanel panCerrar3;
    private javax.swing.JPanel panDatos;
    private javax.swing.JPanel panDatos1;
    private javax.swing.JPanel panDatos2;
    private javax.swing.JPanel panDatos3;
    private javax.swing.JPanel panEntrar;
    private javax.swing.JPanel panEntrar3;
    private javax.swing.JPanel panInterfaz;
    private javax.swing.JPanel panMinimizar;
    private javax.swing.JPanel panMinimizar1;
    private javax.swing.JPanel panMinimizar2;
    private javax.swing.JPanel panMinimizar3;
    private javax.swing.JPanel panRegistrar;
    private javax.swing.JPanel panUsuario2;
    private javax.swing.JPanel panUsuario24;
    private javax.swing.JPanel panUsuario25;
    private javax.swing.JPanel panUsuario26;
    private javax.swing.JPanel panUsuario3;
    private javax.swing.JTable tblEnvios;
    private javax.swing.JTable tblMiembros;
    private javax.swing.JTable tblPeliculas;
    private javax.swing.JTable tblPeliculasID;
    private javax.swing.JTextField txtActor;
    private javax.swing.JTextField txtAsignar;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCategoria;
    private javax.swing.JPasswordField txtContraseña;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtFormato;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtTitulo;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
