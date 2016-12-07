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
public class Puesto {
    private int id;
    private String descripcion;
    
    public Puesto(){
        this.id = 0;
        this.descripcion = "";
    }
    
    public Puesto(int id,String descripcion){
        this.id = id;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public static Puesto getPuesto(Connection con, int id){
        Puesto puesto = new Puesto();
        try {
            String query = "SELECT * FROM puesto WHERE id=?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                puesto.setId(rs.getInt("id"));
                puesto.setDescripcion(rs.getString("descripcion"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return puesto;
    }
    
    public String toString(){
        return this.getDescripcion();
    }
}
