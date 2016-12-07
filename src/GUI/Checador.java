/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Clases.DBConnection;
import Clases.Controladores;
import Clases.Empleado;
import Clases.Horario;
import Clases.UFMatcherClass;
import Clases.UFScannerClass;
import Clases.Utils;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author OSORIO
 */
public class Checador extends javax.swing.JFrame {

    /**
     * Creamos el modelo
     */
    javax.swing.table.DefaultTableModel Modelo = new javax.swing.table.DefaultTableModel() {
        public boolean isCellEditable(int fil, int col) {
            return false;
        }
    };
    private static int BUFFER_SIZE = 10000000;
    private static final long serialVersionUID = 1L;
    private int nScannerNumber = 0;
    public Connection con;
    private imagePanel imgPanel = null;
    public int nC = 0;
    private UFScannerClass libScanner = null;

    private UFMatcherClass libMatcher = null;

    private int nInitFlag = 0;

    private int nCaptureFlag = 0;

    private byte[][] byteTemplateArray = null;

    private int[] intTemplateSizeArray = null;

    private int nTemplateCnt = 0;

    private int nLogListCnt = 0;

    private Pointer hMatcher = null;

    private PointerByReference refTemplateArray = null;  //  @jve:decl-index=0:

    Pointer[] pArray = null;

    private String[] strTemplateArray = null;

    private int nSecurityLevel = 0;

    private int nDetectFake = 2;

    private int nFastMode = 0;

    public final int MAX_TEMPLATE_SIZE = 384;
    Thread relo;

    /**
     * Creates new form Checador
     */
    public Checador() {
        initComponents();
        DBConnection db = DBConnection.getInstance();
        this.con = db.getConnection();
        if (this.con==null) {
            db.destroy(null);
            GuiConnection gui = new GuiConnection(this, rootPaneCheckingEnabled);
            gui.setVisible(true);
            this.con = gui.getCon();
        }
        relo = runnable();
        relo.start();
        iniciarLector();
        setStatusMsg("FAVOR INGRESE SU HUELLA");
        Thread hilo2 = hiloChecador();
        hilo2.start();
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                //Cuando cierra el frame quitamos el lector
                unInit();
            }
        });
        setModeloP();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println(dim.width);
        if (dim.width>1366) {
            System.out.println("Entro");
            Dimension dim2 = new Dimension(1366, 768);
            setMaximumSize(dim2);
            setPreferredSize(dim2);
        }
        
        //this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        setLocationRelativeTo(null);
        setTitle("Checador UM");
    }

    public void setModeloP() {
        jTable1.setModel(Modelo);
        Modelo.addColumn("HORA");
        Modelo.addColumn("OBSERVACIÓN");
        jTable1.getTableHeader().setReorderingAllowed(false);
                        
    }

    public void iniciarLector() {
        String lCurDirTemp = System.getProperty("java.io.tmpdir");
        System.out.println(lCurDirTemp);
        lCurDirTemp = lCurDirTemp + "\\BioMiniLib";
        File directorio = new File(lCurDirTemp);
        directorio.mkdir();
        boolean lValDll = Controladores.descomprime(lCurDirTemp);
        try {
            Controladores.AgregarRuta(lCurDirTemp);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if (nInitFlag != 0) {

            MsgBox("already init..");

            return;
        }

        nCaptureFlag = 0;

        try {

            libScanner = (UFScannerClass) Native.loadLibrary("UFScanner", UFScannerClass.class);
            libMatcher = (UFMatcherClass) Native.loadLibrary("UFMatcher", UFMatcherClass.class);
        } catch (Exception ex) {

            setStatusMsg("loadLlibrary : UFScanner,UFMatcher fail!!");
            MsgBox("loadLlibrary : UFScanner,UFMatcher fail!!");

            return;
        }
        int nRes = 0;

        nRes = libScanner.UFS_Init();

        if (nRes == 0) {

            System.out.println("UFS_Init() success!!");

            nInitFlag = 1;

            //MsgBox("Scanner Init success!!");
            nRes = testCallScanProcCallback();

            if (nRes == 0) {

                setStatusMsg("==>UFS_SetScannerCallback pScanProc ...");

                IntByReference refNumber = new IntByReference();
                nRes = libScanner.UFS_GetScannerNumber(refNumber);

                if (nRes == 0) {
                    nScannerNumber = refNumber.getValue();
                    setStatusMsg("UFS_GetScannerNumber() scanner number :" + nScannerNumber);

                    PointerByReference refMatcher = new PointerByReference();

                    nRes = libMatcher.UFM_Create(refMatcher);

                    if (nRes == 0) {

                        UpdateScannerList(); //list upate ==> getcurrentscannerhandle�� list�� ������//
                        System.out.println("after upadtelist");
                        initVariable(1);
                        System.out.println("after initVariable");
                        initArray(100, 1024); //array size,template size

                        hMatcher = refMatcher.getValue();

                        IntByReference refValue = new IntByReference();
                        IntByReference refFastMode = new IntByReference();

                        //security level (1~7)
                        nRes = libMatcher.UFM_GetParameter(hMatcher, 302, refValue); //302 : security level :UFM_

                        if (nRes == 0) {
                            nSecurityLevel = refValue.getValue();//
                            //setStatusMsg("get security level,302(security) value is " + refValue.getValue());
                        } else {
                            setStatusMsg("get security level fail! code: " + nRes);
                            MsgBox("get security level fail! code: " + nRes);
                        }

                        //fast mode
                        nRes = libMatcher.UFM_SetParameter(hMatcher, libMatcher.UFM_PARAM_FAST_MODE, refFastMode);
                        if (nRes == 0) {
                            nFastMode = refFastMode.getValue();
                            //setStatusMsg("get fastmode,301(fastmode) value is " + refFastMode.getValue());
                            //MsgBox("get fastmode,301(fastmode) value is "+refFastMode.getValue());
                        } else {
                            setStatusMsg("get fastmode value fail! code: " + nRes);
                            MsgBox("get fastmode value fail! code: " + nRes);
                        }
                        if (nFastMode == 1) {

                        }

                        int nSelectedIdx = 0;

                        if (hMatcher != null) {
                            switch (nSelectedIdx) {

                                case 0:
                                    nRes = libMatcher.UFM_SetTemplateType(hMatcher, libMatcher.UFM_TEMPLATE_TYPE_SUPREMA); //2001 Suprema type

                                    break;
                                case 1:
                                    nRes = libMatcher.UFM_SetTemplateType(hMatcher, libMatcher.UFM_TEMPLATE_TYPE_ISO19794_2); //2002 iso type

                                    break;
                                case 2:
                                    nRes = libMatcher.UFM_SetTemplateType(hMatcher, libMatcher.UFM_TEMPLATE_TYPE_ANSI378); //2003 ansi type

                                    break;
                            }
                        }

                    } else {

                        setStatusMsg("UFM_Create fail!! code :" + nRes);
                        MsgBoxWar("LECTOR NO ENCONTRADO");
                        System.exit(0);
                        return;
                    }

                } else {
                    MsgBox("GetScannerNumber fail!! code :" + nRes);
                    setStatusMsg("GetScannerNumber fail!! code :" + nRes);
                    return;

                }

            } else {

                setStatusMsg("UFS_SetScannerCallback() fail,code :" + nRes);

            }

        }

        if (nRes != 0) {
            System.out.println("Init() fail!!");
            setStatusMsg("Init fail!! return code:" + nRes);
            MsgBox("Scanner Init fail!!");
        } else {
            setStatusMsg("");
        }

    }

    public void initVariable(int nFlag) {

        if (nFlag == 1) { //UFS_Init\//
            nInitFlag = 1;
        } else {
            nInitFlag = 0;
        }

        nCaptureFlag = 0;

        nLogListCnt = 0;
        Pointer hScanner = null;
        hScanner = GetCurrentScannerHandle();

        IntByReference pValue = new IntByReference();

        pValue.setValue(1000);

        int nRes = libScanner.UFS_SetParameter(hScanner, libScanner.UFS_PARAM_TIMEOUT, pValue);
        if (nRes == 0) {
            setStatusMsg("Change combox-timeout,201(timeout) value is " + pValue.getValue());
        } else {
            setStatusMsg("Change combox-timeout,change parameter value fail! code: " + nRes);
        }

        pValue.setValue(100);
        nRes = libScanner.UFS_SetParameter(hScanner, libScanner.UFS_PARAM_BRIGHTNESS, pValue);
        if (nRes == 0) {
            setStatusMsg("Change combox-brightness,202 value is " + pValue.getValue());
        } else {
            setStatusMsg("Change combox-brightness,change parameter value fail! code: " + nRes);
        }

        pValue.setValue(7);
        nRes = libScanner.UFS_SetParameter(hScanner, libScanner.UFS_PARAM_SENSITIVITY, pValue);
        if (nRes == 0) {
            setStatusMsg("Change combox-detect_fake,312(fake detect) value is " + pValue.getValue());
        } else {
            setStatusMsg("Change combox-detect_fake,change parameter value fail! code: " + nRes);
        }

        pValue.setValue(7);
        nRes = libScanner.UFS_SetParameter(hScanner, libScanner.UFS_PARAM_SENSITIVITY, pValue);
        if (nRes == 0) {
            setStatusMsg("Change combox-sensitivity,203 value is " + pValue.getValue());
        } else {
            setStatusMsg("Change combox-sensitivity,change parameter value fail! code: " + nRes);
        }

        nRes = libScanner.UFS_SetTemplateType(hScanner, libScanner.UFS_TEMPLATE_TYPE_SUPREMA); //2001 Suprema type
        if (nRes == 0) {
            setStatusMsg("Change combox-Scan TemplateType:2001");
        } else {
            setStatusMsg("Change combox-Scan TemplateType,change parameter value fail! code: " + nRes);
        }
    }

    public int testCallScanProcCallback() {
        int nRes = 0;

        PointerByReference refParam = new PointerByReference();
        refParam.getPointer().setInt(0, 1);

        nRes = libScanner.UFS_SetScannerCallback(pScanProc, refParam);
        if (nRes == 0) {
            System.out.println("==>UFS_SetScannerCallback pScanProc ..." + pScanProc);

        }
        return nRes;
    }
    UFScannerClass.UFS_SCANNER_PROC pScanProc = new UFScannerClass.UFS_SCANNER_PROC() {
        public int callback(String szScannerId, int bSensorOn, PointerByReference pParam) //interface ..must be implemeent
        {
            nC++;

            System.out.println(nC + "==========================================");  //
            System.out.println("==>ScanProc calle scannerID:" + szScannerId);  //  
            System.out.println("sensoron value is " + bSensorOn);
            System.out.println("void * pParam  value is " + pParam.getValue());
            System.out.println(nC + "==========================================");  //

            UpdateScannerList();

            return 1;
        }
    };

    public void UpdateScannerList() {

        int nSelectedIdx = 0;
        int nRes = 0;
        PointerByReference hTempScanner = new PointerByReference();
        Pointer hScanner = null;
        IntByReference refType = new IntByReference();

        byte[] bScannerId = new byte[512];

        String szListLog = null;
        int nNumber = 0;

        System.out.println("==update Scanner list==");

        IntByReference refNumber = new IntByReference();

        nRes = libScanner.UFS_GetScannerNumber(refNumber);

        if (nRes == 0) {

            System.out.println("UFS_GetScannerNumber() res value is " + nRes);

            nNumber = refNumber.getValue();
        } else {

            return;
        }

        if (nNumber <= 0) {

            return;
        }

        for (int j = 0; j < nNumber; j++) {
            nRes = libScanner.UFS_GetScannerHandle(j, hTempScanner);
            hScanner = null;
            hScanner = hTempScanner.getValue();

            if (nRes == 0 && hScanner != null) {

                nRes = libScanner.UFS_GetScannerID(hScanner, bScannerId);

                nRes = libScanner.UFS_GetScannerType(hScanner, refType);

                szListLog = "ID : " + Native.toString(bScannerId) + " Type : " + refType.getValue();

            }
        }

    }

    UFScannerClass.UFS_CAPTURE_PROC pCaptureProc = new UFScannerClass.UFS_CAPTURE_PROC() {
        public int callback(Pointer hScanner, int bFingerOn, Pointer pImage, int nWidth, int nHeight, int nResolution, PointerByReference pParam) {
            nC++;

            drawCurrentFingerImage();

            return 1;

        }
    };

    public void drawCurrentFingerImage() {
        /*test draw image*/
        IntByReference refResolution = new IntByReference();
        IntByReference refHeight = new IntByReference();
        IntByReference refWidth = new IntByReference();
        Pointer hScanner = null;

        hScanner = GetCurrentScannerHandle();

        libScanner.UFS_GetCaptureImageBufferInfo(hScanner, refWidth, refHeight, refResolution);
        byte[] pImageData = new byte[refWidth.getValue() * refHeight.getValue()];

        libScanner.UFS_GetCaptureImageBuffer(hScanner, pImageData);

        imgPanel.drawFingerImage(refWidth.getValue(), refHeight.getValue(), pImageData);
    }

    public Pointer GetCurrentScannerHandle() {
        Pointer hScanner = null;
        int nRes = 0;
        int nNumber = 0;

        PointerByReference refScanner = new PointerByReference();
        IntByReference refScannerNumber = new IntByReference();

//		�Ʒ� success!!//
        nRes = libScanner.UFS_GetScannerNumber(refScannerNumber);

        if (nRes == 0) {

            nNumber = refScannerNumber.getValue();

            if (nNumber <= 0) {

                return null;
            }

        } else {

            return null;
        }
        nRes = libScanner.UFS_GetScannerHandle(0, refScanner);

        hScanner = refScanner.getValue();

        if (nRes == 0 && hScanner != null) {
            return hScanner;
        }
        return null;
    }

    public Thread runnable() {

        Thread reloj = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    //LocalTime time = LocalTime.now();
                    Date now = new Date();
                    SimpleDateFormat fech = new SimpleDateFormat("dd MMM yyyy");
                    String dateHoy = fech.format(now);
                    //LocalDate date = LocalDate.now();
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss a");
                    String s = df.format(now);
                    lbFecha.setText(dateHoy.toUpperCase());
                    lbHora.setText(s);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }
            }
        });

        return reloj;
    }

    /**
     * Inicializa el JLabel con la clase para pintar imagen
     *
     * @return
     */
    private imagePanel getImagePanel() {
        if (imgPanel == null) {
            imgPanel = new imagePanel();
            imgPanel.setLayout(null);
            imgPanel.setBounds(new Rectangle(260, 17, 270, 310));
            imgPanel.setBackground(Color.red);
        }
        return imgPanel;
    }

    /**
     * Clase para pintar la imagen obtenida del lector esta hereda una super
     * clase JLabel
     */
    class imagePanel extends JLabel {

        //private PlanarImage image;
        private BufferedImage buffImage = null;

        /**
         * Pinta el dedo capturado en el JLabel desde canvas
         *
         * @param nWidth ancho original de la imagen
         * @param nHeight alto original de la imagen
         * @param buff bytes del la imagen obtenida
         */
        private void drawFingerImage(int nWidth, int nHeight, byte[] buff) {
            buffImage = new BufferedImage(nWidth, nHeight, BufferedImage.TYPE_BYTE_GRAY);
            buffImage.getRaster().setDataElements(0, 0, nWidth, nHeight, buff);
            Graphics g = buffImage.createGraphics();
            g.dispose();
            repaint();
        }

        /**
         * Sirve para poner tranparente el JLabel en el caso que no tenga datos
         */
        private void drawBlank() {
            buffImage = new BufferedImage(210, 200, BufferedImage.TRANSLUCENT);
            repaint();
        }

        /**
         * pinta la imagen circular
         *
         * @param g
         */
        public void paintComponent(Graphics g) {
            g.setClip(new java.awt.geom.Ellipse2D.Float(0f, 0f, 210, 200));
            g.drawImage(buffImage, 0, 0, 210, 200, this);

        }
    }

    /**
     * Muetra mensaje en pantalla
     *
     * @param log
     */
    public void setStatusMsg(String log) {
        mensajes.setText("");
        mensajes.setText(log);
    }

    /**
     * Manda modal de informacion pantalla
     *
     * @param log
     */
    public void MsgBox(String log) {
        JOptionPane.showMessageDialog(null, log);
    }
    public void MsgBoxWar(String log){
        JOptionPane.showMessageDialog(null, log,"ADVERTENCIA",JOptionPane.WARNING_MESSAGE);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbHora = new javax.swing.JLabel();
        lbFecha = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = getImagePanel();
        mensajes = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(exitOnclose());
        setIconImage(new ImageIcon(getClass().getResource("/Resources/img/ICONOCHECADOR.png")).getImage());
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbHora.setFont(new java.awt.Font("Arial", 1, 65)); // NOI18N
        lbHora.setForeground(new java.awt.Color(8, 85, 163));
        lbHora.setText("jLabel2");
        getContentPane().add(lbHora, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 130, 430, 80));

        lbFecha.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        lbFecha.setForeground(new java.awt.Color(8, 85, 163));
        lbFecha.setText("jLabel3");
        getContentPane().add(lbFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 200, 320, 60));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 30)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(8, 85, 163));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 310, 620, 40));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("jLabel2");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 120, 210, 200));

        mensajes.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        mensajes.setForeground(new java.awt.Color(94, 173, 56));
        mensajes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mensajes.setText("jLabel3");
        getContentPane().add(mensajes, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 370, 700, -1));

        jLabel3.setPreferredSize(new java.awt.Dimension(50, 50));
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 420, -1, -1));

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 470, 1030, 50));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 540, 600, 90));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/img/FONDOCHECA.jpg"))); // NOI18N
        jLabel1.setText("jLabel1");
        jLabel1.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -4, 1390, 750));

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /**
     * Inicia el arreglo de Bytes
     *
     *
     * @param nArrayCnt el numero de posicion
     * @param nMaxTemplateSize numero maximo del arreglo para bytes
     */
    public void initArray(int nArrayCnt, int nMaxTemplateSize) {
        if (byteTemplateArray != null) {
            byteTemplateArray = null;

        }

        if (intTemplateSizeArray != null) {
            intTemplateSizeArray = null;

        }

        byteTemplateArray = new byte[nArrayCnt][MAX_TEMPLATE_SIZE];

        intTemplateSizeArray = new int[nArrayCnt];

        refTemplateArray = new PointerByReference();

    }

    /**
     * Método
     *
     * @return Thread
     */
    public Thread hiloChecador() {
        Thread reloj = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (nInitFlag == 0) {
                        MsgBox("Lector no inicado!");
                        return;
                    }

                    Pointer hScanner = null;

                    hScanner = GetCurrentScannerHandle();

                    if (hScanner != null) {

                        int nRes = libScanner.UFS_ClearCaptureImageBuffer(hScanner);

                        setStatusMsg("COLOQUE SU DEDO EN EL LECTOR");

                        nRes = libScanner.UFS_CaptureSingleImage(hScanner);
                        if (nRes == 0) {

                            byte[] bTemplate = new byte[512];

                            IntByReference refTemplateSize = new IntByReference();

                            IntByReference refTemplateQuality = new IntByReference();

                            IntByReference refVerify = new IntByReference();
                            try {
                                nRes = libScanner.UFS_Extract(hScanner, bTemplate, refTemplateSize, refTemplateQuality);

                                if (nRes == 0) {
                                    //setStatusMsg("save template file template size:" + refTemplateSize.getValue() + " quality:" + refTemplateQuality.getValue());
                                    int tempsize = refTemplateSize.getValue();
                                    System.arraycopy(bTemplate, 0, byteTemplateArray[0], 0, refTemplateSize.getValue());//byte[][]
                                    intTemplateSizeArray[0] = refTemplateSize.getValue();
                                    //se compara la huella
                                    ArrayList<Empleado> empleado = Empleado.listaEmpleados(con, 1);
                                    boolean find = false;
                                    for (Empleado emp : empleado) {
                                        try {
                                            nRes = libMatcher.UFM_Verify(hMatcher, bTemplate, refTemplateSize.getValue(), emp.getHuellaDigital(), emp.getTemplateSize(), refVerify);//byte[][]
                                            if (nRes == 0) {
                                                if (refVerify.getValue() == 1) {
                                                    ArrayList<Horario> horario = Horario.horarioDia(con, emp.getId());
                                                    if (horario.size() > 0) {
                                                        validarHora(horario, emp.getId());
                                                        jLabel4.setText("");
                                                        jLabel4.setText(emp.getNombre().toUpperCase() + " " + emp.getApellidos().toUpperCase());
                                                        mensajes.setForeground(new java.awt.Color(94, 173, 56));
                                                        setStatusMsg("Correcto");
                                                    } else {
                                                        jLabel4.setText("");
                                                        jLabel4.setText(emp.getNombre().toUpperCase() + " " + emp.getApellidos().toUpperCase());
                                                        mensajes.setForeground(new java.awt.Color(94, 173, 56));
                                                        setStatusMsg("NO CUENTAS CON UN HORARIO ASIGNADO");
                                                    }
                                                    
                                                    
                                                    jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/img/palomita.png")));
                                                    find = true;
                                                    break;
                                                } else {
                                                    //NO COINCIDE PARA EL PERSONAL
                                                    find = false;
                                                }
                                            } else {
                                                //setStatusMsg("verify fail!! " + nRes);

                                                byte[] refErr = new byte[512];
                                                nRes = libMatcher.UFM_GetErrorString(nRes, refErr);
                                                if (nRes == 0) {
                                                    //setStatusMsg("==>UFM_GetErrorString err is " + Native.toString(refErr));
                                                    //MsgBox("==>UFM_GetErrorString err is " + Native.toString(refErr));
                                                }

                                            }
                                        } catch (Exception ex) {

                                        }
                                    }
                                    if (!find) {
                                        mensajes.setForeground(Color.red);
                                        setStatusMsg("Huella no registrada");
                                        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/img/tacha.png")));

                                    }
                                    //nRes = libMatcher.UFM_Verify(hMatcher, bTemplate, refTemplateSize.getValue(), byteTemplateArray[nSelectedIdx], intTemplateSizeArray[nSelectedIdx], refVerify);//byte[][]
                                    //setStatusMsg("eroll template array idx:" + nTemplateCnt + " template size:" + intTemplateSizeArray[nTemplateCnt]);
                                    //setStatusMsg("Huella tomada");
                                    nTemplateCnt++;
                                    //se pinta la huella en el jpanel
                                    drawCurrentFingerImage();
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {

                                    }
                                    jLabel4.setText("");
                                    int rows = jTable1.getRowCount();
                                    for (int i = rows - 1; i >= 0; i--) {
                                        Modelo.removeRow(i);
                                    }
                                    mensajes.setForeground(new java.awt.Color(94, 173, 56));
                                    setStatusMsg("COLOQUE SU DEDO EN EL LECTOR");
                                    jLabel3.setIcon(null);
                                    jLabel3.revalidate();
                                    imgPanel.drawBlank();
                                    nCaptureFlag = 1;

                                } else {

                                }
                            } catch (Exception ex) {

                                MsgBox("exception err:" + ex.getMessage());
                            }
                        } else {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {

                            }
                            //jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/img/tacha.png")));
                        }

                    } else {
                        // scanner pointer  null	
                    }

                }
            }
        });

        return reloj;
    }

    public void validarHora(ArrayList<Horario> horas, int idEmpleado) {
        try {
            Date now = new Date();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            //obtenemos la hora de chequeo
            String s = df.format(now);
            //Realizamos la validacion
            Date comparar1, comparar2, comparar3,comparar4, comparar5;
            //hora actual
            comparar1 = df.parse(s);
            boolean valor = false;
            boolean docenteBool = false;
            for (Horario hora : horas) {
                //hora1
                comparar2 = df.parse(hora.getHoraEntrada());
                //hora2
                comparar3 = df.parse(hora.getHoraSalida());
                int response = Utils.comparaHorario(comparar2, comparar3);
                switch (response) {
                    case 0:
                        valor = Utils.evaluarDesayuno(this.con, comparar1, comparar2, comparar3, hora, idEmpleado, s);
                        break;
                    case 1:
                        //obtenemos la lista de horarios reales del docente
                        docenteBool = true;
                        ArrayList<Horario> horarioD = Horario.horarioDiaDocente(con, idEmpleado);
                        for (Horario horaD : horarioD) {
                            //hora1
                            comparar4 = df.parse(horaD.getHoraEntrada());
                            //hora2
                            comparar5 = df.parse(horaD.getHoraSalida());
                            int response2 = Utils.comparaHorario(comparar4, comparar5);
                            int valorD = Utils.evaluarDocente(this.con, comparar1, comparar4, comparar5, horaD, idEmpleado, s);
                            switch (valorD){
                                case 1:
                                    Modelo.addRow(new Object[]{s, "A TIEMPO"});
                                    valor = true;
                                    break;
                                case 2:
                                    Modelo.addRow(new Object[]{s, "RETARDO"});
                                    valor = true;
                                    break;
                                case 3:
                                    Modelo.addRow(new Object[]{s, "FUERA DE TIEMPO"});
                                    valor = true;
                                    break;
                                case 4:
                                    Modelo.addRow(new Object[]{s, "YA REGISTRADO"});
                                    valor = true;
                                    break;
                            }
                            
                        }
                        
                        break;
                    default:
                        valor = Utils.evaluarAdmon(this.con, comparar1, comparar2, comparar3, hora, idEmpleado, s);
                        break;
                }
                if (valor) {
                    break;
                }

            }
            if (valor && !docenteBool) {
                Modelo.addRow(new Object[]{s, "A TIEMPO"});
            } else if(!docenteBool){
                Modelo.addRow(new Object[]{s, "FUERA DE TIEMPO"});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int exitOnclose() {
        return javax.swing.WindowConstants.EXIT_ON_CLOSE;
    }

    /**
     * Metodo que desinicializa el lecto
     */
    public void unInit() {
        System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()

        int nRes = libScanner.UFS_Uninit();

        if (nRes == 0) {

            //setStatusMsg("UFS_Uninit sucess!!");
            nRes = libMatcher.UFM_Delete(hMatcher);

            nInitFlag = 0;

        } else {
            setStatusMsg("No se pudo quitar el lector");
        }
    }

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
            java.util.logging.Logger.getLogger(Checador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Checador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Checador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Checador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Checador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lbFecha;
    private javax.swing.JLabel lbHora;
    private javax.swing.JLabel mensajes;
    // End of variables declaration//GEN-END:variables
}
