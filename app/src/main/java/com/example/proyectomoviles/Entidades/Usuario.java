package com.example.proyectomoviles.Entidades;

import java.util.Date;

public  class Usuario {

    //// NO ESTOY SEGURO DE NADA ESTO
    private String nombre;
    private String correoPucp;
    private String contrasena;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreoPucp() {
        return correoPucp;
    }

    public void setCorreoPucp(String correoPucp) {
        this.correoPucp = correoPucp;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Usuario() {

    }

    public Usuario(String nombre, String correoPucp, String contrasena) {
        this.nombre = nombre;
        this.correoPucp = correoPucp;
        this.contrasena = contrasena;
    }



}
