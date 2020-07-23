package com.example.proyectomoviles.Entidades;

import java.util.Date;
// Importar Correctamente el USUARIO (HACER LUEGO DE JUNTAR)

public class Incidencia {

    private String nombre;
    private String descripcion;
    private String lugar;
    private String foto; //??
    private String fecha;
    private String estado;
    private String autor;

    private Comentario[] listaComentarios;


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


    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public String  getUsuarioAutor() {
        return autor;
    }

    public void setUsuarioAutor(String usuarioAutor) {
        this.autor = usuarioAutor;
    }


    public Comentario[] getListaComentarios() {
        return listaComentarios;
    }

    public void setListaComentarios(Comentario[] listaComentarios) {
        this.listaComentarios = listaComentarios;

    }
}
