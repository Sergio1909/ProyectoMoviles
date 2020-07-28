package com.example.proyectomoviles.Entidades;

public class ComentarioDTO {
    private Comentario[] listaComentario;
    private String estado;

    public Comentario[] getListaComentario() {
        return listaComentario;
    }

    public void setListaComentario(Comentario[] listaComentario) {
        this.listaComentario = listaComentario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
