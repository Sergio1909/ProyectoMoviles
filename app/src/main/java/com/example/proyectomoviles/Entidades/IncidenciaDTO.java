package com.example.proyectomoviles.Entidades;

public class IncidenciaDTO {
    private Incidencia[] listaIncidencias;
    private String estado;

    public Incidencia[] getListaIncidencias() {
        return listaIncidencias;
    }

    public void setListaIncidencias(Incidencia[] listaIncidencias) {
        this.listaIncidencias = listaIncidencias;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
