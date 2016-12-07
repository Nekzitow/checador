/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author OSORIO
 */
public class Horario {

    private int id;
    private String horaEntrada;
    private String horaSalida;
    private int dia;
    private int idTipo;
    private int tiempoAntes;
    private int tiempoDespues;
    private int idAsignacion;

    public Horario() {
        this.id = 0;
        this.horaEntrada = "";
        this.horaSalida = "";
        this.dia = 0;
        this.idTipo = 0;
        this.tiempoAntes = 0;
        this.tiempoDespues = 0;
        this.idAsignacion = 0;
    }

    public Horario(int id, String horaEntrada, String horaSalida, int dia,
            int idTipo, int tiempoAntes, int tiempoDespues, int idAsignacion) {
        this.id = id;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.dia = dia;
        this.idTipo = idTipo;
        this.tiempoAntes = tiempoAntes;
        this.tiempoDespues = tiempoDespues;
        this.idAsignacion = idAsignacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public int getTiempoAntes() {
        return tiempoAntes;
    }

    public void setTiempoAntes(int tiempoAntes) {
        this.tiempoAntes = tiempoAntes;
    }

    public int getTiempoDespues() {
        return tiempoDespues;
    }

    public void setTiempoDespues(int tiempoDespues) {
        this.tiempoDespues = tiempoDespues;
    }

    public int getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(int idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    /**
     *
     * @param con
     * @param idEmpleado
     * @return ArrayList<Horario>
     */
    public ArrayList<Horario> HorarioEmpleado(Connection con, int idEmpleado) {
        ArrayList<Horario> lista = new ArrayList<>();
        try {
            String query = "SELECT horario.*,tiempo_antes,tiempo_despues FROM horario "
                    + "INNER JOIN asignacion_horario ON horario.id = id_horario "
                    + "INNER JOIN tipo_horario ON tipo_horario.id = id_tipo_horario "
                    + "INNER JOIN parametros ON parametros.id = id_parametros "
                    + " WHERE id_empleado = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, idEmpleado);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Horario horario = new Horario();
                horario.setId(rs.getInt("id"));
                horario.setHoraEntrada(rs.getString("hora_entrada"));
                horario.setHoraSalida(rs.getString("hora_salida"));
                horario.setDia(rs.getInt("dia"));
                horario.setIdTipo(rs.getInt("id_tipo_horario"));
                horario.setTiempoAntes(rs.getInt("tiempo_antes"));
                horario.setTiempoDespues(rs.getInt("tiempo_despues"));
                lista.add(horario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     *
     * @param con
     * @param idEmpleado
     * @return
     */
    public static ArrayList<Horario> horarioDia(Connection con, int idEmpleado) {
        Calendar cal = Calendar.getInstance();
        ArrayList<Horario> lista = new ArrayList<>();
        try {
            String query = "SELECT horario.*,tiempo_antes,tiempo_despues,asignacion_horario.id as idAsignacion FROM horario "
                    + "INNER JOIN asignacion_horario ON horario.id = id_horario "
                    + "INNER JOIN tipo_horario ON tipo_horario.id = id_tipo_horario "
                    + "INNER JOIN parametros ON parametros.id = id_parametros "
                    + " WHERE id_empleado = ? AND dia = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, idEmpleado);
            pstmt.setInt(2, getDiaActual());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Horario horario = new Horario();
                horario.setId(rs.getInt("id"));
                horario.setHoraEntrada(rs.getString("hora_entrada"));
                horario.setHoraSalida(rs.getString("hora_salida"));
                horario.setDia(rs.getInt("dia"));
                horario.setIdTipo(rs.getInt("id_tipo_horario"));
                horario.setTiempoAntes(rs.getInt("tiempo_antes"));
                horario.setTiempoDespues(rs.getInt("tiempo_despues"));
                horario.setIdAsignacion(rs.getInt("idAsignacion"));
                lista.add(horario);
            }
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public static ArrayList<Horario> horarioDiaDocente(Connection con,int idEmpleado){
        Calendar cal = Calendar.getInstance();
        ArrayList<Horario> lista = new ArrayList<>();
        try {
            String query = "SELECT MIN(horario.hora_entrada) AS  hora_entrada, MAX(hora_salida) AS hora_salida,ac.clave_materias,id_grupos,"
                    + "MIN(id_asignacion_horario) AS id_asignacion,tiempo_antes,tiempo_despues FROM horario " +
                            "INNER JOIN asignacion_horario AS asig ON  asig.id_horario = horario.id " +
                            "INNER JOIN asignacion_clase AS ac ON ac.id_asignacion_horario = asig.id " +
                            "INNER JOIN tipo_horario ON tipo_horario.id = id_tipo_horario " +
                            "INNER JOIN parametros ON parametros.id = id_parametros "+
                            "WHERE asig.id_empleado = ?  AND dia=? GROUP BY clave_materias,id_grupos,tiempo_antes,tiempo_despues";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, idEmpleado);
            pstmt.setInt(2, getDiaActual());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Horario horario = new Horario();
                horario.setHoraEntrada(rs.getString("hora_entrada"));
                horario.setHoraSalida(rs.getString("hora_salida"));
                horario.setTiempoAntes(rs.getInt("tiempo_antes"));
                horario.setTiempoDespues(rs.getInt("tiempo_despues"));
                horario.setIdAsignacion(rs.getInt("id_asignacion"));
                lista.add(horario);
            }
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     *
     * @return
     */
    public static int getDiaActual() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        int dia = 0;
        switch (day) {
            case 0:
                //sabado
                dia = 5;
                break;
            case 1:
                //domingo
                dia = 6;
                break;
            case 2:
                //lunes
                dia = 0;
                break;
            case 3:
                //martes
                dia = 1;
                break;
            case 4:
                //miercoles
                dia = 2;
                break;
            case 5:
                //jueves
                dia = 3;
                break;
            case 6:
                //viernes
                dia = 4;
                break;
            case 7:
                //viernes
                dia = 5;
                break;
        }
        return dia;
    }

    /**
     *
     * @param con
     * @param idEmpleado
     * @param tipoEntrada
     * @param idAsignacion
     * @param horario
     * @param estado
     * @param tipoHorario 1:signifca que es docente 2:desayuno 3:admon
     * @return
     */
    public static boolean saveAsistencia(Connection con, int idEmpleado,
            int tipoEntrada, int idAsignacion, String horario, int estado, int tipoHorario) {
        try {
            Date now = new Date();
            SimpleDateFormat fech = new SimpleDateFormat("yyyy-MM-dd");
            String dateHoy = fech.format(now);
            String query = "";
            switch (tipoEntrada) {
                case 1:
                    //insert hora de entrada
                    query = "INSERT INTO asistencia (hora_llegada,fecha,estado,id_empleado,id_asignacion_horario,id_estado) "
                            + "VALUES(?,?,?,?,?,?)";
                    break;
                case 2:
                    //insert Hora salida
                    query = "INSERT INTO asistencia (hora_salida,fecha,estado,id_empleado,id_asignacion_horario,id_estado) "
                            + "VALUES(?,?,?,?,?,?)";
                    break;
            }
            PreparedStatement pstStatement = con.prepareStatement(query);
            pstStatement.setTime(1, java.sql.Time.valueOf(horario));
            pstStatement.setDate(2, java.sql.Date.valueOf(dateHoy));
            pstStatement.setInt(3, tipoHorario);
            pstStatement.setInt(4, idEmpleado);
            pstStatement.setInt(5, idAsignacion);
            pstStatement.setInt(6, estado);
            pstStatement.executeUpdate();
            pstStatement.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param con
     * @param horario
     * @param idAsitencia
     * @return
     */
    public static boolean updateAsistencia(Connection con, String horario, int idAsitencia) {
        boolean check = false;
        try {
            String query = "UPDATE asistencia SET hora_salida=? WHERE id=?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setTime(1, Time.valueOf(horario));
            pstmt.setInt(2, idAsitencia);
            pstmt.executeUpdate();
            pstmt.close();
            check = true;
        } catch (Exception e) {
            e.printStackTrace();
            check = false;
        }
        return check;
    }
    
    public static boolean checkChequeo(Connection con, int idEmpleado,
             int idAsignacion, int tipoHorario){
        boolean check = false;
        try {
            Date now = new Date();
            SimpleDateFormat fech = new SimpleDateFormat("yyyy-MM-dd");
            String dateHoy = fech.format(now);
            //dateHoy = dateHoy + "%";
            String query = "SELECT * FROM asistencia WHERE fecha::text LIKE ? "
                    + "AND id_empleado=? AND id_asignacion_horario=? AND estado=?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, dateHoy);
            pstmt.setInt(2, idEmpleado);
            pstmt.setInt(3, idAsignacion);
            pstmt.setInt(4, tipoHorario);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
               check = true;
            } else{ // se hace la insercion
                check = false;}
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;
    }

    /**
     * Este metodo sirve para verificar si ya hay un regitro previo en la tabla
     * de asistencia en el caso que sea verdadero este se actualiza de lo
     * contrario este se inserta
     *
     * @param con variable de conexion a la BD
     * @param idEmpleado
     * @param tipoEntrada
     * @param idAsignacion
     * @param horario
     * @param estado
     * @param tipoHorario 1:signifca que es docente 2:desayuno 3:admon
     * @return
     */
    public static boolean checkAsistencia(Connection con, int idEmpleado,
            int tipoEntrada, int idAsignacion, String horario, int estado, int tipoHorario) {
        boolean check = false;
        try {
            Date now = new Date();
            SimpleDateFormat fech = new SimpleDateFormat("yyyy-MM-dd");
            String dateHoy = fech.format(now);
            //dateHoy = dateHoy + "%";
            String query = "SELECT * FROM asistencia WHERE fecha::text LIKE ? "
                    + "AND id_empleado=? AND id_asignacion_horario=? AND estado=?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, dateHoy);
            pstmt.setInt(2, idEmpleado);
            pstmt.setInt(3, idAsignacion);
            pstmt.setInt(4, tipoHorario);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // se hace un update
                //VERIFICAR ESTE METODO
                //CHECAR FUNCIONALIDAD
                int idAsistencia = rs.getInt("id");
                if (updateAsistencia(con, horario, idAsistencia)) {
                    check = true;
                } else {
                    check = false;
                }
            } else // se hace la insercion
            if (saveAsistencia(con, idEmpleado, tipoEntrada, idAsignacion, horario, estado, tipoHorario)) {
                check = true;
            } else {
                check = false;
            }
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            check = false;
        }
        return check;
    }
}
