/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package farmacia;

import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Personal
 */
public class Compra extends javax.swing.JInternalFrame {

    /**
     * Creates new form Compra
     */
    public Compra() {
        initComponents();
        this.setTitle("Compras");
        bloquear();
//        cargarTabla();
        txtSubtotal.setEnabled(false);
        txtTotal.setEditable(false);
        txtTotal.setText("0");

    }
    DefaultTableModel modelo;

    private void cargarTabla() {
        String[] registro = new String[5];
        registro[0] = txtCodigoMed.getText();
        registro[1] = txtNombreMed.getText();
        registro[2] = txtCantidad.getText();
        registro[3] = txtCosto.getText();
        registro[4] = txtSubtotal.getText();


    }

    private void bloquear() {
        txtNumCompra.setEnabled(false);
        jdcFecha.setEnabled(false);
        txtCodProveedor.setEnabled(false);
        btnBuscarProveedor.setEnabled(false);
        txtNombrePro.setEnabled(false);
        txtCedulaBod.setEnabled(false);
        txtNombreBod.setEnabled(false);
        btnBuscarBodeguero.setEnabled(false);
        txtCodigoMed.setEnabled(false);
        txtNombreMed.setEnabled(false);
        btnBuscarMedicamento.setEnabled(false);
        txtCosto.setEnabled(false);
        txtStock.setEnabled(false);
        txtCantidad.setEnabled(false);
        txtSubtotal.setEnabled(false);
        txtTotal.setEnabled(false);
        btnAñadir.setEnabled(false);
        btnNuevo.setEnabled(true);
        btnIngresarCompra.setEnabled(false);
        btnIngresarDetalle.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnImprimir.setEnabled(false);
        btnSalir.setEnabled(true);
    }

    private void limpiar() {
        txtNumCompra.setText("");
        jdcFecha.setDate(null);
        txtCodProveedor.setText("");
        txtNombrePro.setText("");
        txtCedulaBod.setText("");
        txtNombreBod.setText("");
        txtCodigoMed.setText("");
        txtCosto.setText("");
        txtStock.setText("");
        txtCantidad.setText("");
        txtSubtotal.setText("");
        
    }

    private void desbloquear() {
        btnBuscarProveedor.setEnabled(true);
        btnBuscarBodeguero.setEnabled(true);
        txtNumCompra.setEnabled(true);
        jdcFecha.setEnabled(true);
        txtCodProveedor.setEnabled(true);
        txtCodProveedor.setEditable(true);
        txtNombrePro.setEnabled(true);
        txtNombrePro.setEditable(false);
        txtCedulaBod.setEnabled(true);
        txtNombreBod.setEnabled(true);
        txtNombreBod.setEditable(true);
    }

    private void btnNuevo() {
        btnIngresarCompra.setEnabled(true);
        btnIngresarDetalle.setEnabled(false);
        btnCancelar.setEnabled(true);
        btnImprimir.setEnabled(true);
        btnSalir.setEnabled(true);
    }

    private void ingresarCompra() {
        if (txtNumCompra.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese  Número de Compra!");
            txtNumCompra.requestFocus();
        } else if (String.valueOf(jdcFecha.getDate()).isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese  Fecha de Compra!");
            jdcFecha.requestFocus();
        } else if (txtCodProveedor.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese  Código del Proveedor!");
            txtCodProveedor.requestFocus();
        } else if (txtCedulaBod.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese  Cédula del del Bodeguero!");
            txtCedulaBod.requestFocus();
        } else {
            conexion cc = new conexion();
            Connection cn = cc.conectar();
            String sql = "";
            
            Integer NUM_COMPR;
            String COD_PRO_P, COD_BOD_P, FEC_COM;
            Float TOTAL;
            
            NUM_COMPR = Integer.valueOf(txtNumCompra.getText());
            COD_PRO_P = txtCodProveedor.getText();
            COD_BOD_P = txtCedulaBod.getText();
            TOTAL = txtTotal.getTextDecimal();
            FEC_COM = ((JTextField) jdcFecha.getDateEditor().getUiComponent()).getText();

            sql = "insert into compra (NUM_COMPR,COD_PRO_P,COD_BOD_P,TOTAL,FEC_COM ) values(?,?,?,?,?)";
            try {
                PreparedStatement psd = cn.prepareStatement(sql);
                psd.setInt(1, NUM_COMPR);
                psd.setString(2, COD_PRO_P);
                psd.setString(3, COD_BOD_P);
                psd.setFloat(4, TOTAL);
                psd.setString(5, FEC_COM);
                int n = psd.executeUpdate();
                if (n > 0) {
                    JOptionPane.showMessageDialog(null, "Se inserto una fila correctamente");
                    btnIngresarDetalle.setEnabled(true);
                    btnIngresarCompra.setEnabled(false);
                    desbloquearDetalle();
                    btnBuscarBodeguero.setEnabled(false);
                    btnBuscarProveedor.setEnabled(false);
                    txtNumCompra.setEnabled(false);
                    jdcFecha.setEnabled(false);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }

        }

    }
    
     private void desbloquearDetalle() {
        txtCodigoMed.setEnabled(true);
        txtNombreMed.setEnabled(true);
        txtNombreMed.setEditable(false);
        btnBuscarMedicamento.setEnabled(true);
        txtStock.setEnabled(false);
        txtCosto.setEnabled(false);
        txtCantidad.setEnabled(true);
        btnAñadir.setEnabled(false);
    }


    public void ingresarCompra_Detalle() {
        conexion cc = new conexion();
        Connection cn = cc.conectar();
        String sql = "";
        String NUM_COMPR_P, COD_MED_P;
        Integer CANT_MED;
        Float SUBTOTAL;
        NUM_COMPR_P = txtNumCompra.getText();
        CANT_MED = Integer.valueOf(txtCantidad.getText());
        COD_MED_P = txtCodigoMed.getText();
        SUBTOTAL = txtCosto.getTextDecimal() * Integer.valueOf(txtCantidad.getText());
        txtSubtotal.setText(String.valueOf(SUBTOTAL));

        sql = "insert into compra_detalle(NUM_COMPR_P,CANT_MED,COD_MED_P,SUBTOTAL ) values(?,?,?,?)";
        try {
            PreparedStatement psd = cn.prepareStatement(sql);
            psd.setString(1, NUM_COMPR_P);
            psd.setInt(2, CANT_MED);
            psd.setString(3, COD_MED_P);
            psd.setFloat(4, SUBTOTAL);
            int n = psd.executeUpdate();
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "Se inserto una fila correctamente");
                btnIngresarDetalle.setEnabled(false);
                btnAñadir.setEnabled(true);
                limpiarDetalle();
                Float sumaT = total();
                txtTotal.setText(String.valueOf(sumaT));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    private void limpiarDetalle() {
        txtCodigoMed.setText("");
        txtNombreMed.setText("");
        txtStock.setText("");
        txtCosto.setText("");
        txtCantidad.setText("");
        txtSubtotal.setText("");
    }

    public float total() {
        conexion cc = new conexion();
        Connection cn = cc.conectar();
        String sql = "";

        Float TOTAL = null;

        sql = "select TOTAL FROM compra WHERE NUM_COMPR = '" + Integer.valueOf(txtNumCompra.getText()) + "'";
        try {
            Statement ps = cn.createStatement();
            ResultSet rs = ps.executeQuery(sql);
            while (rs.next()) {
                TOTAL = rs.getFloat("TOTAL");
            }


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return TOTAL;
    }
public void codigoFocus(){
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
                txtStock.setText(registros[7]);
                txtCosto.setText(registros[12]);

            }
             } catch (SQLException eX) {
            JOptionPane.showMessageDialog(this, eX);
        }
     }
public void verificarCodigoBarras() {
    if(!txtCodigoMed.getText().isEmpty()){
        String codigoS = txtCodigoMed.getText();
        char[] codigo = codigoS.toCharArray();
        int pares = 0, par = 0;
        int impares = 0, impar = 0;
        int suma=0, verificador=0;
        if (codigo.length == 13) {
               System.out.println("Pares");
            for (int i = 1; i < codigo.length; i = i + 2) {
                par = Integer.valueOf(String.valueOf(codigo[i])) * 3;
                System.out.println(par);
                pares = pares + par;
             
               // System.out.print(i);
            }
               System.out.println("\nImpares");
            for (int i = 0; i < codigo.length-1; i = i + 2) {
                impar = Integer.valueOf(String.valueOf(codigo[i]));
                System.out.println(impar);
                impares = impares + impar;
                //System.out.print(i);
            }

            suma = pares + impares;
            for (int i = 0; suma >= 10; i++) {
                suma = suma - 10;
                
            }
            System.out.println("\n\n"+suma);
            verificador=10-suma;
            if (verificador != Integer.valueOf(String.valueOf(codigo[12]))) {
                JOptionPane.showMessageDialog(null, "No corresponde a ningun estandar de "
                        + "codigo de barras",
                        "Ingrese un código de barras valido", JOptionPane.ERROR_MESSAGE);
                txtCodigoMed.setText("");
                txtCodigoMed.requestFocus(true);
                txtCosto.setText("");
            txtStock.setText("");
            txtNombreMed.setText("");
            
            }
        } else if (codigo.length == 8) {
            System.out.println("Pares");
            for (int i = 1; i < codigo.length-1; i = i + 2) {
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
            verificador=10-suma;
            System.out.println(verificador);
            if (verificador != Integer.valueOf(String.valueOf(codigo[7]))) {
                if (Integer.valueOf(String.valueOf(codigo[0])) != 0 || Integer.valueOf(String.valueOf(codigo[0])) != 1) {
                    JOptionPane.showMessageDialog(null, "No corresponde a ningun estandar de "
                            + "codigo de barras",
                            "Ingrese un código de barras valido", JOptionPane.ERROR_MESSAGE);
                    txtCodigoMed.setText("");
                    txtCodigoMed.requestFocus(true);
                    txtCosto.setText("");
            txtStock.setText("");
            txtNombreMed.setText("");
                }

            }
        } else if (codigo.length == 12) {
            for (int i = 1; i < codigo.length-1; i = i + 2) {
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
            verificador=10-suma;
            if (verificador != Integer.valueOf(String.valueOf(codigo[11]))) {
                JOptionPane.showMessageDialog(null, "No corresponde a ningun estandar de "
                        + "codigo de barras",
                        "Ingrese un código de barras valido", JOptionPane.ERROR_MESSAGE);
                txtCodigoMed.setText("");
                txtCodigoMed.requestFocus(true);
                txtCosto.setText("");
            txtStock.setText("");
            txtNombreMed.setText("");
            }
        } else if (codigo.length == 14) {
            for (int i = 1; i < codigo.length-1; i = i + 2) {
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
            verificador=10-suma;
            if (verificador != Integer.valueOf(String.valueOf(codigo[13]))) {
                JOptionPane.showMessageDialog(null, "No corresponde a ningun estandar de "
                        + "codigo de barras",
                        "Ingrese un código de barras valido", JOptionPane.ERROR_MESSAGE);
                txtCodigoMed.setText("");
                txtCodigoMed.requestFocus(true);
                txtCosto.setText("");
            txtStock.setText("");
            txtNombreMed.setText("");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No corresponde a ningun estandar de "
                    + "codigo de barras",
                    "Ingrese un código de barras valido", JOptionPane.ERROR_MESSAGE);
            txtCodigoMed.setText("");
            txtCodigoMed.requestFocus(true);
            txtCosto.setText("");
            txtStock.setText("");
            txtNombreMed.setText("");
                 
        }
    }
}
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
        txtStock = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtCosto = new miscomponentes.ucTextBoxNumerosDecimales();
        jPanel5 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        btnAñadir = new javax.swing.JButton();
        txtSubtotal = new miscomponentes.ucTextBoxNumerosDecimales();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCompra = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        txtTotal = new miscomponentes.ucTextBoxNumerosDecimales();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNumCompra = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtCodProveedor = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnBuscarProveedor = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtNombrePro = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtCedulaBod = new javax.swing.JTextField();
        txtNombreBod = new javax.swing.JTextField();
        btnBuscarBodeguero = new javax.swing.JButton();
        jdcFecha = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        btnIngresarCompra = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        btnIngresarDetalle = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(204, 204, 255));

        jPanel2.setBackground(new java.awt.Color(153, 153, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Datos del Medicamento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18), new java.awt.Color(0, 0, 0))); // NOI18N

        jLabel3.setText("Código:");

        txtCodigoMed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoMedActionPerformed(evt);
            }
        });
        txtCodigoMed.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCodigoMedFocusLost(evt);
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

        jLabel4.setText("Stock:");

        jLabel11.setText("Costo:");

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
                        .addComponent(btnBuscarMedicamento))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtNombreMed, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(41, Short.MAX_VALUE))
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
                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel13.setText("SubTotal:");

        btnAñadir.setText("+");
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
                .addComponent(jLabel12)
                .addGap(18, 18, 18)
                .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAñadir)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(btnAñadir, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel14.setText("TOTAL A PAGAR:");

        txtTotal.setBackground(new java.awt.Color(0, 0, 0));
        txtTotal.setForeground(new java.awt.Color(255, 255, 102));
        txtTotal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel14)
                        .addGap(43, 43, 43)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)))
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

        btnBuscarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buscar.png"))); // NOI18N
        btnBuscarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProveedorActionPerformed(evt);
            }
        });

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

        btnBuscarBodeguero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buscar.png"))); // NOI18N
        btnBuscarBodeguero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarBodegueroActionPerformed(evt);
            }
        });

        jdcFecha.setDateFormatString("yyyy/MM/dd");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel15.setText("Fecha:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addContainerGap(14, Short.MAX_VALUE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 526, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addContainerGap(45, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel6))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtNombrePro, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addContainerGap(116, Short.MAX_VALUE)
                                    .addComponent(txtCodProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(btnBuscarProveedor)))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(55, 55, 55)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel7)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel8)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(txtCedulaBod, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(btnBuscarBodeguero))))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addGap(49, 49, 49)
                                    .addComponent(jLabel9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtNombreBod, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(132, 132, 132)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNumCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jdcFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(txtNumCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel15)
                    .addComponent(jdcFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnBuscarProveedor)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8)
                                .addComponent(txtCedulaBod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnBuscarBodeguero, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombreBod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))))
                .addContainerGap(14, Short.MAX_VALUE))
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
                .addGap(203, 203, 203))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnIngresarCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1470118723_Modify.png"))); // NOI18N
        btnIngresarCompra.setText("Ingresar Compra");
        btnIngresarCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresarCompraActionPerformed(evt);
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

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1470118332_document-new.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnImprimir.setText("Imprimir");

        btnIngresarDetalle.setText("Ingresar Detalle");
        btnIngresarDetalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresarDetalleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnIngresarCompra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnImprimir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnIngresarDetalle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIngresarCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIngresarDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(28, 28, 28))
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
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 528, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCodProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodProveedorActionPerformed

    private void txtCedulaBodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaBodActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaBodActionPerformed

    private void txtCodigoMedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoMedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoMedActionPerformed

    public void controlnoduplicados() {
        conexion cc = new conexion();
        Connection cn = cc.conectar();
        String sql;
        sql = "select COUNT(*) AS contar from compra where NUM_COMPR='" + txtNumCompra.getText() + "'";
        try {
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                int contar1 = rs.getInt("contar");
                if (contar1 > 0) {
                    JOptionPane.showMessageDialog(null, "CÓDIGO YA EXISTE...");
                    txtNumCompra.requestFocus();
                } else {
                    ingresarCompra();
                }

            }
        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void btnIngresarCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresarCompraActionPerformed
        controlnoduplicados();
    }//GEN-LAST:event_btnIngresarCompraActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        limpiar();
        bloquear();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        limpiar();
        desbloquear();
        btnNuevo();
//        txtTotal.setText("0");

    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnAñadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAñadirActionPerformed
        // TODO add your handling code here:

        ingresarCompra_Detalle();
        total();
//      cargarTablaDetalle();

    }//GEN-LAST:event_btnAñadirActionPerformed

    public void cargarTablaDetalle(){
    String[] titulos = {"Nº Compra", "Cantidad", "Código", "SubTotal"};
        String[] registros = new String[4];
        modelo = new DefaultTableModel(null, titulos);
      
        conexion cc = new conexion();
        Connection cn = cc.conectar();
        String sql = "";
        sql = "select * from compra_detalle where NUM_COMPR_P'" + txtNumCompra.getText() + "'";
       
       
        try {
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                registros[0] = rs.getString("NUM_COMPR_P");
                registros[1] = rs.getString("CANT_MED");
                registros[2] = rs.getString("COD_MED_P");
                registros[3] = rs.getString("SUBTOTAL");
                modelo.addRow(registros);

            }
              tblCompra.setModel(modelo);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    
    private void btnBuscarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProveedorActionPerformed
        // TODO add your handling code here:

        BuscarProveedor p = new BuscarProveedor();

        p.show();
        p.setVisible(true);
    }//GEN-LAST:event_btnBuscarProveedorActionPerformed

    private void btnBuscarMedicamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarMedicamentoActionPerformed
        // TODO add your handling code here:

        BuscarMedicamentos m = new BuscarMedicamentos();

        m.show();
        m.setVisible(true);
    }//GEN-LAST:event_btnBuscarMedicamentoActionPerformed

    private void btnBuscarBodegueroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarBodegueroActionPerformed
        // TODO add your handling code here:
        BuscarBodeguero b = new BuscarBodeguero();

        b.show();
        b.setVisible(true);

    }//GEN-LAST:event_btnBuscarBodegueroActionPerformed

    private void txtNombreBodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreBodActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreBodActionPerformed

    private void btnIngresarDetalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresarDetalleActionPerformed
        // TODO add your handling code here:
        ingresarCompra_Detalle();
    }//GEN-LAST:event_btnIngresarDetalleActionPerformed

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

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Compra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Compra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Compra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Compra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Compra().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAñadir;
    private javax.swing.JButton btnBuscarBodeguero;
    private javax.swing.JButton btnBuscarMedicamento;
    private javax.swing.JButton btnBuscarProveedor;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnIngresarCompra;
    private javax.swing.JButton btnIngresarDetalle;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
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
    private com.toedter.calendar.JDateChooser jdcFecha;
    private javax.swing.JTable tblCompra;
    private javax.swing.JTextField txtCantidad;
    public static javax.swing.JTextField txtCedulaBod;
    public static javax.swing.JTextField txtCodProveedor;
    public static javax.swing.JTextField txtCodigoMed;
    public static miscomponentes.ucTextBoxNumerosDecimales txtCosto;
    public static javax.swing.JTextField txtNombreBod;
    public static javax.swing.JTextField txtNombreMed;
    public static javax.swing.JTextField txtNombrePro;
    private javax.swing.JTextField txtNumCompra;
    public static javax.swing.JTextField txtStock;
    private miscomponentes.ucTextBoxNumerosDecimales txtSubtotal;
    public static miscomponentes.ucTextBoxNumerosDecimales txtTotal;
    // End of variables declaration//GEN-END:variables
}
