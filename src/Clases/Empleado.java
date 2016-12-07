/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author OSORIO
 */
public class Empleado {
    private int id;
    private String nombre;
    private String apellidos;
    private int cedula;
    private String fechaNacimiento;
    private int sexo;
    private String estadoCivil;
    private String profesion;
    private String seguroSocial;
    private String cartillaMilitar;
    private String rfc;
    private String fechaIngreso;
    private String fechaSalida;
    private String curp;
    private String email;
    private byte[] huellaDigital;
    private int templateSize;
    private String foto;
    private String status;
    private int idDireccion;
    private int idPuesto;
    private int idTipoEmpleado;
    private int idDepartamento;
    private int cct_plantel;

    public Empleado() {
        this.id = 0;
        this.nombre = "";
        this.apellidos = "";
        this.cedula = 0;
        this.fechaNacimiento = "";
        this.sexo = 0;
        this.estadoCivil = "";
        this.profesion = "";
        this.seguroSocial = "";
        this.cartillaMilitar = "";
        this.rfc = "";
        this.fechaIngreso = "";
        this.fechaSalida = "";
        this.curp = "";
        this.email = "";
        this.huellaDigital = null;
        this.templateSize = 0;
        this.foto = "";
        this.status = "";
        this.idDireccion = 0;
        this.idPuesto = 0;
        this.idTipoEmpleado = 0;
        this.idDepartamento = 0;
        this.cct_plantel = 0;
    }
    
    public Empleado(int id, String nombre, String apellidos, int cedula, String fechaNacimiento
            , int sexo, String estadoCivil, String profesion, String seguroSocial
            , String cartillaMilitar, String rfc, String fechaIngreso, String fechaSalida
            , String curp, String email, byte[] huellaDigital, String foto, String status
            , int idDireccion, int idPuesto, int idTipoEmpleado, int idDepartamento
            , int cct_plantel,int templateSize) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.cedula = cedula;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.estadoCivil = estadoCivil;
        this.profesion = profesion;
        this.seguroSocial = seguroSocial;
        this.cartillaMilitar = cartillaMilitar;
        this.rfc = rfc;
        this.fechaIngreso = fechaIngreso;
        this.fechaSalida = fechaSalida;
        this.curp = curp;
        this.email = email;
        this.huellaDigital = huellaDigital;
        this.foto = foto;
        this.status = status;
        this.idDireccion = idDireccion;
        this.idPuesto = idPuesto;
        this.idTipoEmpleado = idTipoEmpleado;
        this.idDepartamento = idDepartamento;
        this.cct_plantel = cct_plantel;
        this.templateSize = templateSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getCedula() {
        return cedula;
    }

    public void setCedula(int cedula) {
        this.cedula = cedula;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getSexo() {
        return sexo;
    }

    public void setSexo(int sexo) {
        this.sexo = sexo;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getSeguroSocial() {
        return seguroSocial;
    }

    public void setSeguroSocial(String seguroSocial) {
        this.seguroSocial = seguroSocial;
    }

    public String getCartillaMilitar() {
        return cartillaMilitar;
    }

    public void setCartillaMilitar(String cartillaMilitar) {
        this.cartillaMilitar = cartillaMilitar;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getHuellaDigital() {
        return huellaDigital;
    }

    public void setHuellaDigital(byte[] huellaDigital) {
        this.huellaDigital = huellaDigital;
    }

    public int getTemplateSize() {
        return templateSize;
    }

    public void setTemplateSize(int templateSize) {
        this.templateSize = templateSize;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(int idDireccion) {
        this.idDireccion = idDireccion;
    }

    public int getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(int idPuesto) {
        this.idPuesto = idPuesto;
    }

    public int getIdTipoEmpleado() {
        return idTipoEmpleado;
    }

    public void setIdTipoEmpleado(int idTipoEmpleado) {
        this.idTipoEmpleado = idTipoEmpleado;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public int getCct_plantel() {
        return cct_plantel;
    }

    public void setCct_plantel(int cct_plantel) {
        this.cct_plantel = cct_plantel;
    }
    
    public static ArrayList<Empleado> listaEmpleados(Connection con, int plantel){
        ArrayList<Empleado> lista = new ArrayList<>();
        try {
            String query = "SELECT empleado.* FROM empleado INNER JOIN plantel ON cct = cct_plantel "
                    + "INNER JOIN departamento ON departamento.id = id_departamento "
                    + "INNER JOIN puesto ON puesto.id = id_puesto "
                    + "WHERE cct=?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, plantel);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Empleado emp = new Empleado();
                emp.setId(rs.getInt("id"));
                emp.setNombre(rs.getString("nombre"));
                emp.setApellidos(rs.getString("apellidos"));
                emp.setProfesion(rs.getString("profesion"));
                emp.setIdDepartamento(rs.getInt("id_departamento"));
                emp.setHuellaDigital(rs.getBytes("huella_digital"));
                emp.setTemplateSize(rs.getInt("template_size"));
                emp.setIdPuesto(rs.getInt("id_puesto"));
                lista.add(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public static ArrayList<Empleado> filtroEmpleados(Connection con, int plantel, String txt){
        ArrayList<Empleado> lista = new ArrayList<>();
        try {
            String texto = '%'+txt+'%';
            String query = "SELECT empleado.* FROM empleado INNER JOIN plantel ON cct = cct_plantel "
                    + "INNER JOIN departamento ON departamento.id = id_departamento "
                    + "INNER JOIN puesto ON puesto.id = id_puesto "
                    + "WHERE cct=? AND (empleado.nombre LIKE ? OR apellidos LIKE ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, plantel);
            pstmt.setString(2, texto);
            pstmt.setString(3, texto);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Empleado emp = new Empleado();
                emp.setId(rs.getInt("id"));
                emp.setNombre(rs.getString("nombre"));
                emp.setApellidos(rs.getString("apellidos"));
                emp.setProfesion(rs.getString("profesion"));
                emp.setIdDepartamento(rs.getInt("id_departamento"));
                emp.setIdPuesto(rs.getInt("id_puesto"));
                lista.add(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    
    
    public static boolean saveTemplate(Connection con, byte[] template,int templateSize, int idEmpleado){
        try {
            String query = "UPDATE empleado SET huella_digital = ?, template_size = ? WHERE id=?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setBytes(1, template);
            pstmt.setInt(2, templateSize);
            pstmt.setInt(3, idEmpleado);
            int respuesta = pstmt.executeUpdate();            
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public String toString(){
           return this.getNombre()+" "+this.getApellidos();
    }
}
