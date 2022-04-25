/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package farmacia;

import static contrib.ch.randelshofer.quaqua.QuaquaIconFactory.getResource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author miguelangel
 */
public class RecepcionMed extends javax.swing.JFrame {

    /**
     * Creates new form RecepcionMed
     */
    public RecepcionMed() {
        initComponents();
        this.setTitle("RECEPCIÓN");
        bloquear();
//        cargarTabla();
        tblCompra.setDefaultRenderer(Object.class, new ImgTabla());
        
    }
    
    String[] titulos = {"Verif.", "Código", "Cantidad"};
    DefaultTableModel modelo = new DefaultTableModel(null, titulos);
    int aux = 1;
    
    private void bloquear() {
        txtNumCompra.setEnabled(false);
        txtFechaCompra.setEnabled(false);
        txtFechaEntrega.setEnabled(false);
        txtCodProveedor.setEnabled(false);
        
        txtNombrePro.setEnabled(false);
        txtCedulaBod.setEnabled(false);
        txtNombreBod.setEnabled(false);
        
        txtCodigoMed.setEnabled(false);
        txtNombreMed.setEnabled(false);
        btnBuscarMedicamento.setEnabled(false); 
        
        txtPresentacion.setEnabled(false);
        txtCantidad.setEnabled(false);
        
        btnAñadir.setEnabled(false);
        
        btnReceptar.setEnabled(true);
        btnConfirmar.setEnabled(false);
        btnReceptar1.setEnabled(true);
        btnCancelar.setEnabled(false);
        btnImprimir.setEnabled(false);
        btnSalir.setEnabled(true);
    }
    
    private void limpiar() {
        Calendar fecha = Calendar.getInstance();
        int dia = fecha.get(Calendar.DATE);
        int mes = fecha.get(Calendar.MONTH);
        int annio = fecha.get(Calendar.YEAR);
        String f;
        if (dia < 10) {
            if (mes < 10) {
                f = annio + "/0" + mes + "/0" + dia;
            } else {
                f = annio + "/" + mes + "/0" + dia;
            }
        } else {
            if (mes < 10) {
                f = annio + "/0" + mes + "/" + dia;
            } else {
                f = annio + "/" + mes + "/" + dia;
            }
        }
        
        txtNumCompra.setText("");
        txtFechaCompra.setText("");
        txtFechaEntrega.setText(f);
        txtCodProveedor.setText("");
        txtNombrePro.setText("");
        txtCedulaBod.setText("");
        txtNombreBod.setText("");
        txtCodigoMed.setText("");
        
        txtPresentacion.setText("");
        txtCantidad.setText("");
        
    }
    
    private void desbloquear() {
        
        txtNumCompra.setEnabled(true);
        txtFechaCompra.setEnabled(false);
        txtFechaEntrega.setEnabled(false);
        txtCodProveedor.setEnabled(false);
        
        txtNombrePro.setEditable(false);
        txtCedulaBod.setEnabled(false);
        txtNombreBod.setEnabled(false);
        
    }
    
    private void btnNuevo() {
        btnConfirmar.setEnabled(false);
        
        btnCancelar.setEnabled(true);
        btnImprimir.setEnabled(false);
        btnSalir.setEnabled(true);
    }
    
    private void btnLista() {
        txtNumCompra.setEditable(false);
        txtCodigoMed.setEnabled(true);
        txtNombreMed.setEnabled(true);
        txtNombreMed.setEditable(false);
        btnBuscarMedicamento.setEnabled(true);
        
        txtPresentacion.setEnabled(true);
        txtPresentacion.setEditable(false);
        txtCantidad.setEnabled(true);
        
        btnAñadir.setEnabled(true);
    }
    
    public void codigoFocus() {
        conexion cc = new conexion();
        Connection cn = cc.conectar();
        String sql = "";
        String[] registros = new String[14];
        
        sql = "select * from medicamentos where COD_MED = '" + txtCodigoMed.getText() + "' ";
        try {
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                registros[0] = rs.getString("COD_MED");
                registros[1] = rs.getString("NOM_COM_MED");
                registros[2] = rs.getString("COMP_PRINC");
                registros[3] = rs.getString("TIPO_MED");
                registros[4] = rs.getString("FEC_CAD_MED");
                registros[5] = rs.getString("MG_MED");
                registros[6] = rs.getString("LOTE_MED");
                registros[7] = rs.getString("STOCK");
                registros[8] = rs.getString("PRES_MED");
                registros[9] = rs.getString("VIA_ADM_MED");
                registros[10] = rs.getString("TIP_VENT_MED");
                registros[11] = rs.getString("COST_VENT_MED");
                registros[12] = rs.getString("COST_COMP_MED");
                registros[13] = rs.getString("COD_LAB_P");
                txtNombreMed.setText(registros[1]);
                txtPresentacion.setText(registros[8]);
                
            }
        } catch (SQLException eX) {
            JOptionPane.showMessageDialog(this, eX);
        }
    }
    
    public void verificarCodigoBarras() {
        if (!txtCodigoMed.getText().isEmpty()) {
            String codigoS = txtCodigoMed.getText();
            char[] codigo = codigoS.toCharArray();
            int pares = 0, par = 0;
            int impares = 0, impar = 0;
            int suma = 0, verificador = 0;
            if (codigo.length == 13) {
                System.out.println("Pares");
                for (int i = 1; i < codigo.length; i = i + 2) {
                    par = Integer.valueOf(String.valueOf(codigo[i])) * 3;
                    System.out.println(par);
                    pares = pares + par;

                    // System.out.print(i);
                }
                System.out.println("\nImpares");
                for (int i = 0; i < codigo.length - 1; i = i + 2) {
                    impar = Integer.valueOf(String.valueOf(codigo[i]));
                    System.out.println(impar);
                    impares = impares + impar;
                    //System.out.print(i);
                }
                
                suma = pares + impares;
                for (int i = 0; suma >= 10; i++) {
                    suma = suma - 10;
                    
                }
                System.out.println("\n\n" + suma);
                verificador = 10 - suma;
                if (verificador != Integer.valueOf(String.valueOf(codigo[12]))) {
                    JOptionPane.showMessageDialog(null, "No corresponde a ningun estandar de "
                            + "codigo de barras",
                            "Ingrese un código de barras valido", JOptionPane.ERROR_MESSAGE);
                    txtCodigoMed.setText("");
                    txtCodigoMed.requestFocus(true);
                    
                    txtPresentacion.setText("");
                    txtNombreMed.setText("");
                    
                }
            } else if (codigo.length == 8) {
                System.out.println("Pares");
                for (int i = 1; i < codigo.length - 1; i = i + 2) {
                    par = Integer.valueOf(String.valueOf(codigo[i]));
                    System.out.println(par);
                    pares = pares + par;
                }
                System.out.println("Impares");
                for (int i = 0; i < codigo.length; i = i + 2) {
                    impar = Integer.valueOf(String.valueOf(codigo[i])) * 3;
                    System.out.println(impar);
                    impares = impares + impar;
                }
                
                suma = pares + impares;
                for (int i = 0; suma >= 10; i++) {
                    suma = suma - 10;
                }
                System.out.println(suma);
                verificador = 10 - suma;
                System.out.println(verificador);
                if (verificador != Integer.valueOf(String.valueOf(codigo[7]))) {
                    if (Integer.valueOf(String.valueOf(codigo[0])) != 0 || Integer.valueOf(String.valueOf(codigo[0])) != 1) {
                        JOptionPane.showMessageDialog(null, "No corresponde a ningun estandar de "
                                + "codigo de barras",
                                "Ingrese un código de barras valido", JOptionPane.ERROR_MESSAGE);
                        txtCodigoMed.setText("");
                        txtCodigoMed.requestFocus(true);
                        
                        txtPresentacion.setText("");
                        txtNombreMed.setText("");
                    }
                    
                }
            } else if (codigo.length == 12) {
                for (int i = 1; i < codigo.length - 1; i = i + 2) {
                    par = Integer.valueOf(String.valueOf(codigo[i])) * 3;
                    pares = pares + par;
                }
                for (int i = 0; i < codigo.length; i = i + 2) {
                    impar = Integer.valueOf(String.valueOf(codigo[i]));
                    impares = impares + impar;
                }
                
                suma = pares + impares;
                for (int i = 0; suma >= 10; i++) {
                    suma = suma - 10;
                }
                verificador = 10 - suma;
                if (verificador != Integer.valueOf(String.valueOf(codigo[11]))) {
                    JOptionPane.showMessageDialog(null, "No corresponde a ningun estandar de "
                            + "codigo de barras",
                            "Ingrese un código de barras valido", JOptionPane.ERROR_MESSAGE);
                    txtCodigoMed.setText("");
                    txtCodigoMed.requestFocus(true);
                    
                    txtPresentacion.setText("");
                    txtNombreMed.setText("");
                }
            } else if (codigo.length == 14) {
                for (int i = 1; i < codigo.length - 1; i = i + 2) {
                    par = Integer.valueOf(String.valueOf(codigo[i]));
                    pares = pares + par;
                }
                for (int i = 0; i < codigo.length; i = i + 2) {
                    impar = Integer.valueOf(String.valueOf(codigo[i])) * 3;
                    impares = impares + impar;
                }
                
                suma = pares + impares;
                for (int i = 0; suma >= 10; i++) {
                    suma = suma - 10;
                }
                verificador = 10 - suma;
                if (verificador != Integer.valueOf(String.valueOf(codigo[13]))) {
                    JOptionPane.showMessageDialog(null, "No corresponde a ningun estandar de "
                            + "codigo de barras",
                            "Ingrese un código de barras valido", JOptionPane.ERROR_MESSAGE);
                    txtCodigoMed.setText("");
                    txtCodigoMed.requestFocus(true);
                    
                    txtPresentacion.setText("");
                    txtNombreMed.setText("");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No corresponde a ningun estandar de "
                        + "codigo de barras",
                        "Ingrese un código de barras valido", JOptionPane.ERROR_MESSAGE);
                txtCodigoMed.setText("");
                txtCodigoMed.requestFocus(true);
                
                txtPresentacion.setText("");
                txtNombreMed.setText("");
                
            }
        }
    }
    
    public void buscarCompra() {
        String[] registros = new String[5];
        conexion cc = new conexion();
        Connection cn = cc.conectar();
        String sql = "";
        String estado = "";
        sql = "select * from compra where NUM_COMPR ='" + txtNumCompra.getText() + "'order by NUM_COMPR";
        try {
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            if (rs.next()) {
                rs.beforeFirst();
                while (rs.next()) {
                    registros[0] = rs.getString("NUM_COMPR");
                    registros[1] = rs.getString("COD_PRO_P");
                    registros[2] = rs.getString("COD_BOD_P");
                    registros[3] = rs.getString("TOTAL");
                    registros[4] = rs.getString("FEC_COM");
                    estado = rs.getString("ENTREGADO");
                }
                if (estado.toUpperCase().compareTo("NO") == 0 || estado.toUpperCase().compareTo("N") == 0) {
                    
                    txtFechaCompra.setText(registros[4]);
                    txtCodProveedor.setText(registros[1]);
                    txtCedulaBod.setText(registros[2]);
                    
                    btnLista();

//                Object[] registros2 = new Object[3];
                    String sql2 = "";
                    
                    sql2 = "select * from compra_detalle where NUM_COMPR_P='" + txtNumCompra.getText() + "'";
                    try {
                        Statement psd2 = cn.createStatement();
                        ResultSet rs2 = psd2.executeQuery(sql2);
                        while (rs2.next()) {

//                        registros2[0] = "";
//                        registros2[1] = rs2.getString("COD_MED_P");
//                        registros2[2] = rs2.getString("CANT_MED");
//
//                        modelo.addRow(registros2);
//                        Object o=new Object[]{new JLabel(new ImageIcon(getClass().getResource("/images/1469055789_Close_Box_Red.png")))};
                            ImageIcon imagen = new ImageIcon(getClass().getResource("/images/1469055789_Close_Box_Red.png"));
                            //para redimensionar la imagen
                            ImageIcon ponerImagen = new ImageIcon(imagen.getImage().getScaledInstance(20, 20, 0));
                            modelo.addRow(new Object[]{new JLabel(ponerImagen),
                                rs2.getString("COD_MED_P"),
                                rs2.getString("CANT_MED")});
                            
                        }
                        tblCompra.setModel(modelo);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex);
                    }

                    //para habilitar el boton confirmar al momento en q todos los detalles cambian su imagen
                    aux = tblCompra.getRowCount();
                    
                    String sql3 = "";
                    
                    sql3 = "select * from BODEGUEROS where CED_BOD='" + txtCedulaBod.getText() + "'";
                    try {
                        Statement psd3 = cn.createStatement();
                        ResultSet rs3 = psd3.executeQuery(sql3);
                        while (rs3.next()) {

                        txtNombreBod.setText(rs3.getString("NOM1_BOD")+" "+rs3.getString("APE1_BOD"));
                       
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex);
                    }
                    
                    String sql4 = "";
                    
                    sql4 = "select * from PROVEEDORES where COD_PRO='" + txtCodProveedor.getText() + "'";
                    try {
                        Statement psd4 = cn.createStatement();
                        ResultSet rs4 = psd4.executeQuery(sql4);
                        while (rs4.next()) {

                        txtNombrePro.setText(rs4.getString("NOM_PRO"));
                       
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex);
                    }
                    
                    
                    
                    
                    
                } else {
                    JOptionPane.showMessageDialog(this, "La compra ya se recepto..!!!");
                    limpiar();
                    bloquear();
                }
                
            } else {
                JOptionPane.showMessageDialog(this, "No existe la Compra");
                txtNumCompra.setText("");
                txtNumCompra.requestFocus();
                
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    public void ingresar() {
        int posicion = 0;
        boolean existe = false;
        for (int i = 0; i < tblCompra.getRowCount(); i++) {
            if (tblCompra.getValueAt(i, 1).toString().trim().compareTo(txtCodigoMed.getText()) == 0) {
                if (tblCompra.getValueAt(i, 2).toString().trim().compareTo(txtCantidad.getText()) == 0) {
                    existe = true;
                    posicion = i;
                }
            }
        }
        if (existe) {
            ImageIcon imagen = new ImageIcon(getClass().getResource("/images/1469055254_button_ok.png"));
            //para redimensionar la imagen
            ImageIcon ponerImagen = new ImageIcon(imagen.getImage().getScaledInstance(20, 20, 0));
            tblCompra.setValueAt(new JLabel(ponerImagen), posicion, 0);
            aux = aux - 1;
            if (aux == 0) {
                btnConfirmar.setEnabled(true);
                
            }
        } else {
            JOptionPane.showMessageDialog(this, "El medicamento y la cantidad no constan en la compra..!!");
        }
        txtCantidad.setText("");
        txtCodigoMed.setText("");
        txtNombreMed.setText("");
        txtPresentacion.setText("");
        
    }
    
    public void confirmar() {
        
        conexion cc = new conexion();
        Connection cn = cc.conectar();
        String sql = "";
        sql = "UPDATE compra "
                + "set ENTREGADO='SI' "
                + "WHERE NUM_COMPR=" + txtNumCompra.getText();
        
        try {
            PreparedStatement psd = cn.prepareStatement(sql);
            int n = psd.executeUpdate();
            if (n > 0) {
                JOptionPane.showMessageDialog(this, "Se Receptó Correctamente...");
                
                for (int i = 0; i < tblCompra.getRowCount(); i++) {
                    for (int j = 0; j < tblCompra.getColumnCount(); j++) {
                        tblCompra.setValueAt("", i, j);
                    }
                }
                limpiar();
                bloquear();
                
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel7 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtCodigoMed = new javax.swing.JTextField();
        btnBuscarMedicamento = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        txtNombreMed = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtPresentacion = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        btnAñadir = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCompra = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNumCompra = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtCodProveedor = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        txtNombrePro = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtCedulaBod = new javax.swing.JTextField();
        txtNombreBod = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtFechaCompra = new javax.swing.JTextField();
        txtFechaEntrega = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        btnConfirmar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        btnReceptar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        btnReceptar1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(204, 204, 255));

        jPanel2.setBackground(new java.awt.Color(153, 153, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Datos del Medicamento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N

        jLabel3.setText("Código:");

        txtCodigoMed.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCodigoMedFocusLost(evt);
            }
        });
        txtCodigoMed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoMedActionPerformed(evt);
            }
        });
        txtCodigoMed.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoMedKeyTyped(evt);
            }
        });

        btnBuscarMedicamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buscar.png"))); // NOI18N
        btnBuscarMedicamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarMedicamentoActionPerformed(evt);
            }
        });

        jLabel10.setText("Nombre:");

        jLabel4.setText("Presentación:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtCodigoMed, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBuscarMedicamento)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtNombreMed, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPresentacion, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(txtCodigoMed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnBuscarMedicamento))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtNombreMed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtPresentacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(204, 204, 255));
        jPanel5.setForeground(new java.awt.Color(153, 153, 255));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel12.setText("Cantidad:");

        btnAñadir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1469055254_button_ok.png"))); // NOI18N
        btnAñadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAñadirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAñadir, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAñadir)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(204, 204, 255));

        tblCompra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Nº Compra", "Cantidad", "Código", "SUBTOTAL"
            }
        ));
        jScrollPane1.setViewportView(tblCompra);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));

        jPanel1.setBackground(new java.awt.Color(153, 153, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Datos de la Compra", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Número de Compra:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("PROVEEDOR");

        txtCodProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodProveedorActionPerformed(evt);
            }
        });

        jLabel5.setText("Código:");

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel6.setText("Nombre:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText("BODEGUERO");

        jLabel8.setText("Cédula:");

        jLabel9.setText("Nombre:");

        txtCedulaBod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedulaBodActionPerformed(evt);
            }
        });

        txtNombreBod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreBodActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel15.setText("Fecha de Compra:");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel16.setText("Fecha de Entrega:");

        txtFechaCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaCompraActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNumCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtFechaCompra)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtFechaEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 526, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel6))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtNombrePro, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCodProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(59, 59, 59)))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(55, 55, 55)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel7)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel8)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(txtCedulaBod, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(59, 59, 59))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addGap(49, 49, 49)
                                    .addComponent(jLabel9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtNombreBod, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNumCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(txtFechaCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(txtFechaEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 17, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel7))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtCodProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtNombrePro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtCedulaBod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombreBod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(129, 129, 129))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnConfirmar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1470118723_Modify.png"))); // NOI18N
        btnConfirmar.setText("Confirmar");
        btnConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1470118897_Cancel.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1470119106_sign-out.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnReceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1470118332_document-new.png"))); // NOI18N
        btnReceptar.setText("Receptar");
        btnReceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReceptarActionPerformed(evt);
            }
        });

        btnImprimir.setText("Imprimir");

        btnReceptar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1469056275_box.png"))); // NOI18N
        btnReceptar1.setText("Lista");
        btnReceptar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReceptar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnConfirmar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnReceptar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnImprimir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnReceptar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(btnReceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReceptar1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(101, 101, 101))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCodigoMedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoMedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoMedActionPerformed

    private void txtCodigoMedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodigoMedFocusLost
        // TODO add your handling code here:
        verificarCodigoBarras();
        codigoFocus();
    }//GEN-LAST:event_txtCodigoMedFocusLost

    private void txtCodigoMedKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoMedKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyChar() < 48 || evt.getKeyChar() > 57) {
            evt.consume();
        }

    }//GEN-LAST:event_txtCodigoMedKeyTyped

    private void btnBuscarMedicamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarMedicamentoActionPerformed
        // TODO add your handling code here:

        BuscarMedicamentos3 m = new BuscarMedicamentos3();
        
        m.show();
        m.setVisible(true);
    }//GEN-LAST:event_btnBuscarMedicamentoActionPerformed

    private void btnAñadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAñadirActionPerformed
        // TODO add your handling code here:

        ingresar();
        //      cargarTablaDetalle();
    }//GEN-LAST:event_btnAñadirActionPerformed

    private void btnConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarActionPerformed
        confirmar();
    }//GEN-LAST:event_btnConfirmarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        limpiar();
        bloquear();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnReceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReceptarActionPerformed
        // TODO add your handling code here:
        limpiar();
        desbloquear();
        btnNuevo();
        //        txtTotal.setText("0");
    }//GEN-LAST:event_btnReceptarActionPerformed

    private void btnReceptar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReceptar1ActionPerformed
        // TODO add your handling code here:
        buscarCompra();

    }//GEN-LAST:event_btnReceptar1ActionPerformed

    private void txtFechaCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaCompraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaCompraActionPerformed

    private void txtNombreBodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreBodActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreBodActionPerformed

    private void txtCedulaBodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaBodActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaBodActionPerformed

    private void txtCodProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodProveedorActionPerformed

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
            java.util.logging.Logger.getLogger(RecepcionMed.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RecepcionMed.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RecepcionMed.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RecepcionMed.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RecepcionMed().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAñadir;
    private javax.swing.JButton btnBuscarMedicamento;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnReceptar;
    private javax.swing.JButton btnReceptar1;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable tblCompra;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtCedulaBod;
    private javax.swing.JTextField txtCodProveedor;
    public static javax.swing.JTextField txtCodigoMed;
    private javax.swing.JTextField txtFechaCompra;
    private javax.swing.JTextField txtFechaEntrega;
    private javax.swing.JTextField txtNombreBod;
    public static javax.swing.JTextField txtNombreMed;
    private javax.swing.JTextField txtNombrePro;
    private javax.swing.JTextField txtNumCompra;
    public static javax.swing.JTextField txtPresentacion;
    // End of variables declaration//GEN-END:variables
}
