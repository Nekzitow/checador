/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author OSORIO
 */
public class Departamento {
    private int id;
    private String nombre;
    private String descripcion;
    
    public Departamento(){
        this.id = 0;
        this.nombre = "";
        this.descripcion = "";
    }

    public Departamento(int id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public static Departamento getDepartamentos(Connection con, int idDepartamento){
        Departamento depa = new Departamento();
        try {
            String query = "SELECT * FROM departamento WHERE id=?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, idDepartamento);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                depa.setId(rs.getInt("id"));
                depa.setNombre(rs.getString("nombre"));
                depa.setDescripcion(rs.getString("descripcion"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return depa;
    }
    
    public String toString(){
        return this.getNombre();
    }
}
