/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package farmacia;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.*;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author: Jessenia Toapanta
 */
public class Medicamentos extends javax.swing.JInternalFrame {

    /**
     * Creates new form Producto
     */
    public Medicamentos() throws ParseException {
        initComponents();
        this.setTitle("Medicamentos");
        txtCodigoLaboratorio.setEnabled(false);
        bloquear();
        tblMedicamentos.setAutoResizeMode(tblMedicamentos.AUTO_RESIZE_OFF); //scroll horizontal y aparezcan un tamañao adecuado para los datos
        this.setBackground(Color.white);
        cargarTabla("");
        jrbCodigo.setSelected(true);

        tblMedicamentos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (tblMedicamentos.getSelectedRow() != -1) {//condicion para verificar si algo esta seleccionado
                    modificar();
                    btnModificar.setEnabled(true);
                    btnEliminar.setEnabled(true);
                    btnGuardar.setEnabled(false);
                    btnNuevo.setEnabled(true);
                    btnCancelar.setEnabled(false);
                }
            }
        });

    }

    public void modificar() {
        desbloquear();
        int fila = tblMedicamentos.getSelectedRow();
        txtCodigo.setText(tblMedicamentos.getValueAt(fila, 0).toString().trim());
        txtNombre.setText(tblMedicamentos.getValueAt(fila, 1).toString().trim());
        txtComp_Princ.setText(tblMedicamentos.getValueAt(fila, 2).toString().trim());
        String tipo = String.valueOf(tblMedicamentos.getValueAt(fila, 3).toString().trim());
        if (tipo.equals("GENERICO")) {
            jrbGenerico.setSelected(true);
        } else {
            jrbEspecifico.setSelected(true);
        }
        ((JTextField) jdcFecha.getDateEditor().getUiComponent()).setText(tblMedicamentos.getValueAt(fila, 4).toString().trim());

        txtMiligramos.setText(tblMedicamentos.getValueAt(fila, 5).toString().trim());
        txtLote.setText(tblMedicamentos.getValueAt(fila, 6).toString().trim());
        txtStock.setText(tblMedicamentos.getValueAt(fila, 7).toString().trim());

        String presentacion = String.valueOf(tblMedicamentos.getValueAt(fila, 8).toString().trim());
        if (presentacion.equals("CAPSULA")) {
            jcbPresentacion.setSelectedIndex(1);
        } else if (presentacion.equals("INYECION")) {
            jcbPresentacion.setSelectedIndex(2);
        } else if (presentacion.equals("GEL")) {
            jcbPresentacion.setSelectedIndex(3);
        } else if (presentacion.equals("JERABE")) {
            jcbPresentacion.setSelectedIndex(4);
        } else if (presentacion.equals("HUNGUENTO")) {
            jcbPresentacion.setSelectedIndex(5);
        } else if (presentacion.equals("CREMA")) {
            jcbPresentacion.setSelectedIndex(6);
        } else if (presentacion.equals("POLVO")) {
            jcbPresentacion.setSelectedIndex(7);
        } else if (presentacion.equals("LIQUIDO")) {
            jcbPresentacion.setSelectedIndex(8);
        } else if (presentacion.equals("SPRAY")) {
            jcbPresentacion.setSelectedIndex(9);
        } else if (presentacion.equals("GAS")) {
            jcbPresentacion.setSelectedIndex(10);
        } else if (presentacion.equals("OTROS")) {
            jcbPresentacion.setSelectedIndex(11);
        }

        String viaAdministracion = String.valueOf(tblMedicamentos.getValueAt(fila, 9).toString().trim());
        if (viaAdministracion.equals("ORAL")) {
            jcbViaAdministracion.setSelectedIndex(1);
        } else if (viaAdministracion.equals("INTRAVENOSA")) {
            jcbViaAdministracion.setSelectedIndex(2);
        } else if (viaAdministracion.equals("INTRAMUSCULAR")) {
            jcbViaAdministracion.setSelectedIndex(3);
        } else if (viaAdministracion.equals("TOPICO")) {
            jcbViaAdministracion.setSelectedIndex(4);
        } else if (viaAdministracion.equals("SUBCUTANEA")) {
            jcbViaAdministracion.setSelectedIndex(5);
        } else if (viaAdministracion.equals("INHALATORIA")) {
            jcbViaAdministracion.setSelectedIndex(6);
        }

        String tipoVenta = String.valueOf(tblMedicamentos.getValueAt(fila, 10).toString().trim());
        if (tipoVenta.equals("LIBRE")) {
            jrbLibre.setSelected(true);
        } else {
            jrbReceta.setSelected(true);
        }

        jfPrecioVenta.setText(tblMedicamentos.getValueAt(fila, 11).toString().trim());

        jfPrecioCompra.setText(tblMedicamentos.getValueAt(fila, 12).toString().trim());

        txtCodigoLaboratorio.setText(tblMedicamentos.getValueAt(fila, 13).toString().trim());

        txtCodigo.setEnabled(false);
        txtCodigo.setEditable(false);

    }
    DefaultTableModel modelo;

    public void cargarTabla(String dato) {
        String[] titulos = {"CÓDIGO", "NOMBRE", "COMPNT", "TIPO", "FECHA CADUCIDAD", "MILIGRAMOS", "LOTE", "STOCK", "PRESENTACIÓN", "VÍA DE ADMINISTRACIÓN", "TIPO DE VENTA", "COSTO VENTA", "COSTO COMPRA", "LABORATORIO"};
        String[] registros = new String[14];
        modelo = new DefaultTableModel(null, titulos);
        tblMedicamentos.setModel(modelo);
        conexion cc = new conexion();
        Connection cn = cc.conectar();
        String sql = "";
        if (jrbCodigo.isSelected()) {
            sql = "select * from medicamentos where COD_MED LIKE '%" + dato + "%'order by COD_MED ";
        } else if (jRadioButton1.isSelected()) {
            sql = "select * from medicamentos where TIPO_MED LIKE '%" + dato + "%'order by COD_MED ";
        } else {
            sql = "select * from medicamentos where NOM_COM_MED LIKE '%" + dato + "%' order by COD_MED ";
        }

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
                modelo.addRow(registros);

            }

        } catch (SQLException eX) {
            JOptionPane.showMessageDialog(this, eX);
        }
//

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jtpProductos = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMedicamentos = new javax.swing.JTable();
        jrbCodigo = new javax.swing.JRadioButton();
        jrbNombre = new javax.swing.JRadioButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtComp_Princ = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jdcFecha = new com.toedter.calendar.JDateChooser();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jrbGenerico = new javax.swing.JRadioButton();
        jrbEspecifico = new javax.swing.JRadioButton();
        txtMiligramos = new javax.swing.JTextField();
        txtLote = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jcbPresentacion = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jcbViaAdministracion = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jrbLibre = new javax.swing.JRadioButton();
        jrbReceta = new javax.swing.JRadioButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtCodigoLaboratorio = new javax.swing.JTextField();
        btnBuscarLaboratorio = new javax.swing.JButton();
        jfPrecioCompra = new miscomponentes.ucTextBoxNumerosDecimales();
        jfPrecioVenta = new miscomponentes.ucTextBoxNumerosDecimales();
        jPanel11 = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Criterío de Búsqueda: ");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel5.setBackground(new java.awt.Color(153, 153, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Buscar:");

        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
        });

        jButton2.setText("Reporte");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(35, 35, 35))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jButton2))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tblMedicamentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblMedicamentos.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setViewportView(tblMedicamentos);
        tblMedicamentos.getAccessibleContext().setAccessibleDescription("");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jrbCodigo.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup3.add(jrbCodigo);
        jrbCodigo.setText("Código");

        jrbNombre.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup3.add(jrbNombre);
        jrbNombre.setText("Nombre");

        jRadioButton1.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup3.add(jRadioButton1);
        jRadioButton1.setText("Tipo");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(8, 8, 8)
                        .addComponent(jrbCodigo)
                        .addGap(26, 26, 26)
                        .addComponent(jrbNombre)
                        .addGap(30, 30, 30)
                        .addComponent(jRadioButton1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jrbCodigo)
                    .addComponent(jrbNombre)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadioButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jtpProductos.addTab("Buscar", jPanel2);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Datos del Producto:");

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setForeground(new java.awt.Color(0, 0, 255));
        jLabel4.setText("Los campos marcados con un asterisco (*) son obligatorios...");

        jLabel5.setText("Código del Producto:");

        txtCodigo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCodigoFocusLost(evt);
            }
        });
        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoKeyTyped(evt);
            }
        });

        jLabel6.setText("*");

        jLabel7.setText("Nombre: ");

        jLabel8.setText("*");

        jLabel9.setText("Componente Principal:");

        jLabel10.setText("Tipo Medicamento:");

        jLabel11.setText("Fecha de Caducidad:");

        jdcFecha.setDateFormatString("yyyy/MM/dd");

        jLabel12.setText("Milimagramos:");

        jLabel13.setText("Lote:");

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        buttonGroup1.add(jrbGenerico);
        jrbGenerico.setText("GENERICO");

        buttonGroup1.add(jrbEspecifico);
        jrbEspecifico.setText("ESPECIFICO");
        jrbEspecifico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbEspecificoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jrbGenerico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jrbEspecifico, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jrbGenerico)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jrbEspecifico)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtMiligramos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMiligramosKeyTyped(evt);
            }
        });

        jLabel18.setText("Stock: ");

        jLabel20.setText("Presentación:");

        jcbPresentacion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "CAPSULA", "INYECION", "GEL", "JERABE", "HUNGUENTO", "CREMA", "POLVO", "LIQUIDO", "SPRAY", "GAS", "OTROS" }));
        jcbPresentacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbPresentacionActionPerformed(evt);
            }
        });

        jLabel14.setText("Vía de Administración:");

        jcbViaAdministracion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "ORAL", "INTRAVENOSA", "INTRAMUSCULAR", "TOPICO", "SUBCUTANEA", "INHALATORIA" }));

        jLabel15.setText("Tipo de Venta:");

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        buttonGroup2.add(jrbLibre);
        jrbLibre.setText("LIBRE");

        buttonGroup2.add(jrbReceta);
        jrbReceta.setText("RECETA");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jrbLibre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jrbReceta, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jrbLibre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jrbReceta)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel16.setText("Precio de Compra:");

        jLabel17.setText("Precio de Venta:");

        jLabel21.setText("Código Laboratorio:");

        btnBuscarLaboratorio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buscar.png"))); // NOI18N
        btnBuscarLaboratorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarLaboratorioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel21)
                            .addComponent(jLabel7))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel12)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel20)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel16)))
                            .addGap(3, 3, 3)))
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNombre, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtComp_Princ, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addComponent(txtCodigoLaboratorio, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBuscarLaboratorio)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addGap(10, 10, 10)
                        .addComponent(txtCodigo))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jdcFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(53, 53, 53))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                        .addComponent(txtMiligramos, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel13))
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(jcbPresentacion, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                                        .addComponent(jLabel14))
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(txtStock)
                                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jfPrecioCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING))))))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(txtLote, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jcbViaAdministracion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jfPrecioVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 71, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(153, 153, 153)
                .addComponent(jLabel4)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5))
                        .addGap(15, 15, 15))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnBuscarLaboratorio)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtCodigoLaboratorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel21)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtComp_Princ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jdcFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtMiligramos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(txtLote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jcbPresentacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jcbViaAdministracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel17)
                        .addComponent(jfPrecioCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jfPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        jtpProductos.addTab("Nuevo/Modificar/Eliminar", jPanel3);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1470118332_document-new.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1470118611_floppy_disk_save.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1470118723_Modify.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1470118840_editor-trash-delete-recycle-bin-glyph.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
            .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnModificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jtpProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 638, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtpProductos))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void desbloquear() {
        limpiar();

        txtCodigo.setEnabled(true);
        txtCodigo.setEditable(true);
        txtCodigoLaboratorio.setEnabled(false);
        txtNombre.setEnabled(true);
        txtComp_Princ.setEnabled(true);
        jdcFecha.setEnabled(true);
        txtLote.setEnabled(true);
        txtMiligramos.setEnabled(true);
        jcbPresentacion.setEnabled(true);
        jcbViaAdministracion.setEnabled(true);
        jrbLibre.setEnabled(true);
        jrbReceta.setEnabled(true);
        jrbEspecifico.setEnabled(true);
        jrbGenerico.setEnabled(true);
        txtStock.setEnabled(true);

        jfPrecioCompra.setEnabled(true);
        jfPrecioVenta.setEnabled(true);
    }

    private void bloquear() {
        btnBuscarLaboratorio.setEnabled(false);
        btnNuevo.setEnabled(true);
        btnSalir.setEnabled(true);
        btnEliminar.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnModificar.setEnabled(false);
        btnCancelar.setEnabled(false);
        txtCodigo.setEnabled(false);
        txtCodigoLaboratorio.setEnabled(false);
        txtNombre.setEnabled(false);
        txtComp_Princ.setEnabled(false);
        jdcFecha.setEnabled(false);
        txtLote.setEnabled(false);
        txtMiligramos.setEnabled(false);
        jcbPresentacion.setEnabled(false);
        jcbViaAdministracion.setEnabled(false);
        jrbLibre.setEnabled(false);
        jrbReceta.setEnabled(false);
        jrbEspecifico.setEnabled(false);
        jrbGenerico.setEnabled(false);
        txtStock.setEnabled(false);

        jfPrecioCompra.setEnabled(false);
        jfPrecioVenta.setEnabled(false);
    }

    public void controlnoduplicados() {
        conexion cc = new conexion();
        Connection cn = cc.conectar();
        String sql;
        sql = "select COUNT(*) AS contar from medicamentos where COD_MED='" + txtCodigo.getText() + "'";
        try {
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                int contar1 = rs.getInt("contar");
                if (contar1 > 0) {
                    JOptionPane.showMessageDialog(null, "CÓDIGO YA EXISTE...");
                    txtCodigo.requestFocus();
                    jtpProductos.setSelectedIndex(1);
                } else {
                    guardar();
                }

            }
        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void limpiar() {
        txtCodigoLaboratorio.setText("");
        txtCodigo.setText("");
        txtNombre.setText("");
        txtComp_Princ.setText("");
        jdcFecha.setDate(null);
        txtLote.setText("");
        txtMiligramos.setText("");
        jcbPresentacion.setSelectedIndex(0);
        jcbViaAdministracion.setSelectedIndex(0);
        jrbLibre.setSelected(false);
        jrbReceta.setSelected(false);
        jrbEspecifico.setSelected(false);
        jrbGenerico.setSelected(false);
        txtStock.setText("");
        jfPrecioCompra.setText("");
        jfPrecioVenta.setText("");
    }

    private void guardar() {

        if (txtCodigoLaboratorio.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese  Proveedor!");
            txtCodigoLaboratorio.requestFocus();
        } else if (txtCodigo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese  Código!");
            txtCodigo.requestFocus();
        } else if (txtNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese  Nombre!");
            txtNombre.requestFocus();
        } else if (txtComp_Princ.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese Componente Principal !");
            txtComp_Princ.requestFocus();
        } else if (String.valueOf(jdcFecha.getDate()).isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese Fecha de Caducidad!");
            jdcFecha.requestFocus();
        } else if (txtMiligramos.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese el Miligramaje!");
            txtMiligramos.requestFocus();
        } else if (txtLote.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingresar Lote!");
            txtLote.requestFocus();
        } else if (jcbPresentacion.getSelectedIndex() == 0 || jcbPresentacion.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Escoja la Presentación!");
            jcbPresentacion.requestFocus();
        } else if (jcbViaAdministracion.getSelectedIndex() == 0 || jcbViaAdministracion.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Escoja la Vía de Administración!");
            jcbViaAdministracion.requestFocus();
        } else if (txtStock.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese el Stock!");
            txtStock.requestFocus();
        } else if (jfPrecioCompra.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese Precio de Compra!");
            txtStock.requestFocus();
        } else if (jfPrecioVenta.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese Precio de Venta!");
            jfPrecioVenta.requestFocus();
        } else {
            conexion cc = new conexion();
            Connection cn = cc.conectar();
            String sql = "";
            Integer MG_MED, STOCK;
            String COD_MED, COD_LAB_P, NOM_COM_MED, LOTE_MED, COMP_PRINC, TIPO_MED = null, FEC_CAD_MED, PRES_MED, VIA_ADM_MED, TIP_VENT_MED = null;
            Float COST_COMP_MED, COST_VENT_MED;

            COD_MED = String.valueOf(txtCodigo.getText());

            if (COD_MED.equals(tblMedicamentos)) {
                JOptionPane.showMessageDialog(null, "Código Ya existe!");
            }
            NOM_COM_MED = txtNombre.getText();
            COMP_PRINC = txtComp_Princ.getText();
            if (jrbGenerico.isSelected()) {
                TIPO_MED = jrbGenerico.getText();
            }
            if (jrbEspecifico.isSelected()) {
                TIPO_MED = jrbEspecifico.getText();
            }
            FEC_CAD_MED = ((JTextField) jdcFecha.getDateEditor().getUiComponent()).getText();
            MG_MED = Integer.valueOf(txtMiligramos.getText());
            LOTE_MED = txtLote.getText();
            STOCK = Integer.valueOf(txtStock.getText());
            PRES_MED = String.valueOf(jcbPresentacion.getSelectedItem());
            VIA_ADM_MED = String.valueOf(jcbViaAdministracion.getSelectedItem());
            if (jrbLibre.isSelected()) {
                TIP_VENT_MED = jrbLibre.getText();
            }
            if (jrbReceta.isSelected()) {
                TIP_VENT_MED = jrbReceta.getText();
            }
            COST_COMP_MED = Float.valueOf(jfPrecioCompra.getText());
            COST_VENT_MED = Float.valueOf(jfPrecioVenta.getText());
            COD_LAB_P = txtCodigoLaboratorio.getText();
            sql = "insert into medicamentos(COD_MED,NOM_COM_MED,COMP_PRINC,TIPO_MED,FEC_CAD_MED,MG_MED,LOTE_MED,STOCK,PRES_MED,VIA_ADM_MED,TIP_VENT_MED,COST_COMP_MED,COST_VENT_MED,COD_LAB_P ) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            try {
                PreparedStatement psd = cn.prepareStatement(sql);
                psd.setString(1, COD_MED);
                psd.setString(2, NOM_COM_MED);
                psd.setString(3, COMP_PRINC);
                psd.setString(4, TIPO_MED);
                psd.setString(5, FEC_CAD_MED);
                psd.setInt(6, MG_MED);
                psd.setString(7, LOTE_MED);
                psd.setInt(8, STOCK);
                psd.setString(9, PRES_MED);
                psd.setString(10, VIA_ADM_MED);
                psd.setString(11, TIP_VENT_MED);
                psd.setFloat(12, COST_COMP_MED);
                psd.setFloat(13, COST_VENT_MED);
                psd.setString(14, COD_LAB_P);
                int n = psd.executeUpdate();
                if (n > 0) {
                    JOptionPane.showMessageDialog(null, "Se inserto una fila correctamente");
                    limpiar();
                    bloquear();
                    cargarTabla("");
                    jtpProductos.setSelectedIndex(0);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    public void botonNuevo() {
        btnBuscarLaboratorio.setEnabled(true);
        btnNuevo.setEnabled(false);
        btnSalir.setEnabled(true);
        btnEliminar.setEnabled(false);
        btnGuardar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnCancelar.setEnabled(true);

    }

    public void actualizar() {
        if (txtCodigoLaboratorio.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese  Proveedor!");
            txtCodigoLaboratorio.requestFocus();
        } else if (txtNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese  Nombre!");
            txtNombre.requestFocus();
        } else if (txtComp_Princ.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese Componente Principal !");
            txtComp_Princ.requestFocus();
        } else if (String.valueOf(jdcFecha.getDate()).isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese Fecha de Caducidad!");
            jdcFecha.requestFocus();
        } else if (txtMiligramos.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese el Miligramaje!");
            txtMiligramos.requestFocus();
        } else if (txtLote.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingresar Lote!");
            txtLote.requestFocus();
        } else if (jcbPresentacion.getSelectedIndex() == 0 || jcbPresentacion.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Escoja la Presentación!");
            jcbPresentacion.requestFocus();
        } else if (jcbViaAdministracion.getSelectedIndex() == 0 || jcbViaAdministracion.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Escoja la Vía de Administración!");
            jcbViaAdministracion.requestFocus();
        } else if (txtStock.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese el Stock!");
            txtStock.requestFocus();
        } else if (jfPrecioCompra.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese Precio de Compra!");
            txtStock.requestFocus();
        } else if (jfPrecioVenta.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese Precio de Venta!");
            jfPrecioVenta.requestFocus();
        } else {

            conexion cc = new conexion();
            Connection cn = cc.conectar();
            String sql = "";
            String tipo, venta;
            double num1 = .0f;
            double num2 = .50f;
            if (jrbEspecifico.isSelected()) {
                tipo = "ESPECIFICO";
            } else {
                tipo = "GENERICO";
            }
            if (jrbLibre.isSelected()) {
                venta = "LIBRE";
            } else {
                venta = "RECETA";
            }
            float cost_Vent = Float.valueOf(jfPrecioVenta.getText());
            float cost_Com = Float.valueOf(jfPrecioCompra.getText());

            sql = "UPDATE medicamentos "
                    + "set NOM_COM_MED='" + txtNombre.getText() + "',"
                    + "COMP_PRINC='" + txtComp_Princ.getText() + "',"
                    + "TIPO_MED='" + tipo + "',"
                    + "FEC_CAD_MED='" + ((JTextField) jdcFecha.getDateEditor().getUiComponent()).getText() + "',"
                    + "MG_MED=" + Integer.valueOf(txtMiligramos.getText()) + ","
                    + "LOTE_MED='" + txtLote.getText().toString() + "',"
                    + "STOCK=" + Integer.valueOf(txtStock.getText()) + ","
                    + "PRES_MED='" + jcbPresentacion.getSelectedItem().toString() + "',"
                    + "VIA_ADM_MED='" + jcbViaAdministracion.getSelectedItem().toString() + "',"
                    + "TIP_VENT_MED='" + venta + "',"
                    + "COST_COMP_MED=" + cost_Vent + ","
                    + "COST_VENT_MED=" + cost_Com + ","
                    + "COD_LAB_P='" + txtCodigoLaboratorio.getText() + "'" + "WHERE COD_MED='" + txtCodigo.getText() + "'";

            try {
                PreparedStatement psd = cn.prepareStatement(sql);
                int n = psd.executeUpdate();
                if (n > 0) {
                    JOptionPane.showMessageDialog(this, "Se Actualizo Correctamente...");
                    cargarTabla("");
                    limpiar();
                    bloquear();
                    jtpProductos.setSelectedIndex(0);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex);
            }
        }
    }

    public void eliminar() {
        if (JOptionPane.showConfirmDialog(this, "Està seguro de eliminar el registro?") == 0) {
            conexion cc = new conexion();
            Connection cn = cc.conectar();
            String sql = "DELETE FROM medicamentos WHERE COD_MED='" + txtCodigo.getText() + "'";

            try {
                PreparedStatement psd = cn.prepareStatement(sql);
                int n = psd.executeUpdate();
                if (n > 0) {
                    JOptionPane.showMessageDialog(null, "Se Eliminó Correctamente el Registro");
                    limpiar();
                    bloquear();
                    cargarTabla("");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    public void verificarCodigoBarras() {
        if (!txtCodigo.getText().isEmpty()) {

            String codigoS = txtCodigo.getText();
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
                    txtCodigo.setText("");
                    txtCodigo.requestFocus(true);
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
                        txtCodigo.setText("");
                        txtCodigo.requestFocus(true);
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
                    txtCodigo.setText("");
                    txtCodigo.requestFocus(true);
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
                    txtCodigo.setText("");
                    txtCodigo.requestFocus(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "No corresponde a ningun estandar de "
                        + "codigo de barras",
                        "Ingrese un código de barras valido", JOptionPane.ERROR_MESSAGE);
                txtCodigo.setText("");
                txtCodigo.requestFocus(true);
            }
        }
    }
    private void jrbEspecificoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbEspecificoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jrbEspecificoActionPerformed

    private void btnBuscarLaboratorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarLaboratorioActionPerformed
        // TODO add your handling code here:

        BuscarLaboratorio l = new BuscarLaboratorio();

        l.show();
        l.setVisible(true);
    }//GEN-LAST:event_btnBuscarLaboratorioActionPerformed

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        // TODO add your handling code here:
        cargarTabla(txtBuscar.getText());
    }//GEN-LAST:event_txtBuscarKeyReleased

    private void jcbPresentacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbPresentacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbPresentacionActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        limpiar();
        jtpProductos.setSelectedIndex(0);
        bloquear();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        eliminar();
        jtpProductos.setSelectedIndex(0);

    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        actualizar();        // TODO add your handling code here:
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        controlnoduplicados();

    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        jtpProductos.setSelectedIndex(1);
        desbloquear();
        botonNuevo();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void txtCodigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyChar() < 48 || evt.getKeyChar() > 57) {
            evt.consume();
        }

    }//GEN-LAST:event_txtCodigoKeyTyped

    private void txtCodigoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodigoFocusLost
        // TODO add your handling code here:
        verificarCodigoBarras();
    }//GEN-LAST:event_txtCodigoFocusLost

    private void txtMiligramosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMiligramosKeyTyped
        // TODO add your handling code here:

        if (evt.getKeyChar() < 48 || evt.getKeyChar() > 57) {
            evt.consume();
        }
    }//GEN-LAST:event_txtMiligramosKeyTyped
    static boolean ventana10 = false;

    public void ventanaLaboratorios() {
        if (ventana10 == false) {
            Laboratorios l = new Laboratorios();
            l.show();
            l.setVisible(true);
            ventana10 = true;
        } else {
            JOptionPane.showMessageDialog(null, "La ventana ya está abierta...!", "Mensaje del Sistema", JOptionPane.WARNING_MESSAGE);
        }

    }

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
            java.util.logging.Logger.getLogger(Medicamentos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Medicamentos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Medicamentos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Medicamentos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    new Medicamentos().setVisible(true);
                } catch (ParseException ex) {
                    Logger.getLogger(Medicamentos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarLaboratorio;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox jcbPresentacion;
    private javax.swing.JComboBox jcbViaAdministracion;
    private com.toedter.calendar.JDateChooser jdcFecha;
    private miscomponentes.ucTextBoxNumerosDecimales jfPrecioCompra;
    private miscomponentes.ucTextBoxNumerosDecimales jfPrecioVenta;
    private javax.swing.JRadioButton jrbCodigo;
    private javax.swing.JRadioButton jrbEspecifico;
    private javax.swing.JRadioButton jrbGenerico;
    private javax.swing.JRadioButton jrbLibre;
    private javax.swing.JRadioButton jrbNombre;
    private javax.swing.JRadioButton jrbReceta;
    private javax.swing.JTabbedPane jtpProductos;
    private javax.swing.JTable tblMedicamentos;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCodigo;
    public static javax.swing.JTextField txtCodigoLaboratorio;
    private javax.swing.JTextField txtComp_Princ;
    private javax.swing.JTextField txtLote;
    private javax.swing.JTextField txtMiligramos;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtStock;
    // End of variables declaration//GEN-END:variables
}
