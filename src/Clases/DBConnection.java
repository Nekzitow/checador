/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author OSORIO
 */
public class DBConnection {
    private static DBConnection instance = null;
    private Connection connection;
    protected DBConnection(){
        this.conectaPGSQL();
    }
    
    public Connection getConnection(){
        return this.connection;
    }
    
    public void destroy(DBConnection instance){
        this.instance = instance;
    }
    public static DBConnection getInstance(){
        if(instance == null){
            System.out.println("Instancia nueva");
            instance = new DBConnection();
        }
        return instance;
    }
    public String[] LeerArchivo(String archivo){
        try{
            String[] valores=new String[]{"","","","",""};
            File Arch=new File(archivo);
            System.err.println(Arch.getPath());
            if(!Arch.exists()){
                EscribirArchivoCon("localhost", "5432", "osorio", "123qwe", "siums", Arch);
                valores = LeerArchivo(archivo);
            }else{
            FileReader lector = new FileReader(archivo);
            BufferedReader br = new BufferedReader(lector);
            String linea = br.readLine();
            
            while(linea != null)
            {
                int inicio = linea.indexOf("=");                              
                if(inicio>0)
                {
                    String izquierda = linea.substring(0, inicio);
                    String valor = linea.substring(inicio + 1);
                    if(izquierda.equalsIgnoreCase("ip")){  
                        valores[0]=valor;
                        System.err.println(valor);
                    }else if(izquierda.equalsIgnoreCase("puerto")){   
                        valores[1]=valor;
                        System.err.println(valor);
                    }else if(izquierda.equalsIgnoreCase("usuario")){          
                        valores[2]=valor;
                        System.err.println(valor);
                    }else if(izquierda.equalsIgnoreCase("contra")){   
                        valores[3]=valor;
                        System.err.println(valor);
                    }
                    else if(izquierda.equalsIgnoreCase("BD")){   
                        valores[4]=valor;
                        System.err.println(valor);
                    }                  
                }                  
                 linea = br.readLine();
            
            
        }}
            return valores;
        }//fin try
        catch(Exception e){
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        return null;
        }
    }
    private Connection conectaPGSQL(){
        try{
            String[] x=LeerArchivo("Conexion.ini");
            if(x != null){
                Class.forName("org.postgresql.Driver");
                this.connection =DriverManager.getConnection("jdbc:postgresql://"+x[0]+":"+x[1]+"/"+x[4]+"?currentSchema=sium", x[2], x[3]);
                if (this.connection != null) {
                    System.out.println("You made it, take control your database now!");
                } else {
                    JOptionPane.showMessageDialog(null, "Falló al conectar la Base de datos","Connection Error", JOptionPane.ERROR_MESSAGE);
                }
                return this.connection;
            }else{
                return null;
            }            
        }catch(Exception e){
                JOptionPane.showMessageDialog(null, e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return null;
        }
        
    }
     
    /*
     * Esta funcion sirve para guardar los datos que se llenan en el formulario GUICONEXION
     * y los escribe en un archivo conexion.ini
     * que es el archivo de configuracion de la conexion
     * */
    public static void EscribirArchivoCon(String Ip,String Puerto,String Usuario,String Password
            ,String BD,File Archivo){
        try{
            /*if(!VerificaUso()){
                Archivo.createNewFile();
            }*/
             if(!Archivo.exists()){
                Archivo.createNewFile();
            }
            FileOutputStream escritor = new FileOutputStream(Archivo);
                 String file="Parametros de configuracion"
                         + "\r\nIp="+Ip+
                         "\r\nPuerto="+Puerto+
                         "\r\nUsuario="+Usuario+
                         "\r\nContra="+Password+
                         "\r\nBD="+BD;
                 escritor.write(file.getBytes());                        
                 escritor.close();
                 //Utilerias.MensajeInformacion("Se guardo la conexion");
        }catch(Exception e){
            //Utilerias.MensajeInformacion("Ocurrio el siguiente error "+e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }
}
