/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.util.Date;
import javax.swing.JOptionPane;
import org.joda.time.DateTime;
import org.joda.time.Period;

/**
 *
 * @author OSORIO
 */
public class Utils {

    public static int comparaHorario(Date horaEntrada, Date horaSalida) {
        int respuesta = 0;
        DateTime horaEntra = new DateTime(horaEntrada);
        DateTime horaSal = new DateTime(horaSalida);
        Period periodo = new Period(horaEntra, horaSal);
        respuesta = periodo.getHours();
        return respuesta;
    }

    public static boolean evaluarAdmon(Connection con, Date comparar1,
            Date comparar2, Date comparar3, Horario hora, int idEmpleado, String horario) {
        int respuesta = 0;
        boolean valor = false;
        if (evaluarLimite(comparar1, comparar2, hora.getTiempoAntes() * 60) || evaluarLimite2(comparar1, comparar2)) {
            System.out.println("Fecha en el rango");
            //hacemos la comparacion de si el usuario ya checo con anterioridad, en el caso que sea cierto se mandara un mensaje en pantalla
            if (Horario.checkChequeo(con, idEmpleado, hora.getIdAsignacion(), 3)) {
                //JOptionPane.showMessageDialog(null, "YA TIENE UN REGISTRO PARA ESTE HORARIO");
                valor = false;
            } else if (evaluarLimite(comparar1, comparar2, hora.getTiempoAntes() * 60)) {
                //se hace el guardado 
                Horario.checkAsistencia(con, idEmpleado, 1, hora.getIdAsignacion(), horario, 2, 3);
                valor = true;
            } else {
                //se guarda como retardo
                //JOptionPane.showMessageDialog(null, "SE GUARDARA COMO RETARDO");
                Horario.checkAsistencia(con, idEmpleado, 1, hora.getIdAsignacion(), horario, 1, 3);
                valor = false;
            }

        } else if (evaluarLimite(comparar1, comparar3, hora.getTiempoDespues() * 60)) {
            Horario.checkAsistencia(con, idEmpleado, 2, hora.getIdAsignacion(), horario, 2, 3);
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public static int evaluarDocente(Connection con, Date comparar1,
            Date comparar2, Date comparar3, Horario hora, int idEmpleado, String horario) {
        int valor = 0;
        if (evaluarLimite(comparar1, comparar2, hora.getTiempoAntes() * 60) || evaluarLimite3(comparar1, comparar2)) {
            if (Horario.checkChequeo(con, idEmpleado, hora.getIdAsignacion(), 1)) {
                valor = 4;
            } else if (evaluarLimite(comparar1, comparar2, hora.getTiempoAntes() * 60)) {
                Horario.checkAsistencia(con, idEmpleado, 1, hora.getIdAsignacion(), horario, 2, 1);
                valor = 1;
            } else {
                Horario.checkAsistencia(con, idEmpleado, 1, hora.getIdAsignacion(), horario, 1, 1);
                valor = 2;
            }

        } else if (evaluarLimite(comparar1, comparar3, hora.getTiempoDespues() * 60)) {
            Horario.checkAsistencia(con, idEmpleado, 2, hora.getIdAsignacion(), horario, 2, 1);
            valor = 1;
        } else {
            valor = 3;
        }
        return valor;
    }

    public static boolean evaluarDesayuno(Connection con, Date comparar1,
            Date comparar2, Date comparar3, Horario hora, int idEmpleado, String horario) {
        boolean valor = false;

        if (evaluarLimite(comparar1, comparar2, hora.getTiempoAntes() * 60)) {
            //hacemos la insercion
            if (Horario.checkChequeo(con, idEmpleado, hora.getIdAsignacion(), 2)) {
                //JOptionPane.showMessageDialog(null, "YA TIENE UN REGISTRO PARA ESTE HORARIO");
                valor = false;
            } else {
                Horario.checkAsistencia(con, idEmpleado, 1, hora.getIdAsignacion(), horario, 2, 2);
                valor = true;
            }
        } else if (evaluarLimite(comparar1, comparar3, hora.getTiempoDespues() * 60)) {
            Horario.checkAsistencia(con, idEmpleado, 2, hora.getIdAsignacion(), horario, 2, 2);
            valor = true;
        } else {
            valor = false;
        }

        return valor;
    }

    public static boolean evaluarLimite(Date date1, Date date2, int tiempo) {
        boolean correcto = false;
        long diferencia = (Math.abs(date1.getTime() - date2.getTime())) / 1000;
        long limit = (tiempo * 1000) / 1000L;//limite de tiempo
        if (diferencia <= limit) {
            correcto = true;
        }else{
            System.err.println(diferencia +"---"+ limit);
        }
        return correcto;
    }

    public static boolean evaluarLimite2(Date date1, Date date2) {
        boolean correcto = false;
        long diferencia = (Math.abs(date1.getTime() - date2.getTime())) / 1000;
        long limit = (5400 * 1000) / 1000L;//limite de tiempo
        if (diferencia <= limit) {
            correcto = true;
        }else{
            System.err.println(diferencia +"---"+ limit);
        }
        return correcto;
    }

    public static boolean evaluarLimite3(Date date1, Date date2) {
        boolean correcto = false;
        long diferencia = (Math.abs(date1.getTime() - date2.getTime())) / 1000;
        long limit = (900 * 1000) / 1000L;//limite de tiempo
        if (diferencia <= limit) {
            correcto = true;
        }else{
            System.err.println(diferencia +"---"+ limit);
        }
        return correcto;
    }

    public static String leerBASEURL(String archivo) {
        try {
            String valores = "";
            File Arch = new File(archivo);
            if (!Arch.exists()) {
                Arch.createNewFile();
            } else {
                FileReader lector = new FileReader(archivo);
                BufferedReader br = new BufferedReader(lector);
                String linea = br.readLine();

                while (linea != null) {
                    int inicio = linea.indexOf("=");
                    if (inicio > 0) {
                        String izquierda = linea.substring(0, inicio);
                        String valor = linea.substring(inicio + 1);
                        if (izquierda.equalsIgnoreCase("BASE_URL")) {
                            valores = valor;
                        }
                    }
                    linea = br.readLine();

                }
            }
            return valores;
        }//fin try
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static boolean validarVacios(String[] data){
        boolean response = false;
        for(int i=0; i<data.length;i++){
            if (data[i].trim().isEmpty()) {
                response = true;
                break;
            }
        }
        return response;
        
    }
}
