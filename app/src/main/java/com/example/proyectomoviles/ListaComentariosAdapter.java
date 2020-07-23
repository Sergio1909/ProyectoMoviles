package com.example.proyectomoviles;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectomoviles.Entidades.Comentario;
import com.example.proyectomoviles.Usuarios.ListaIncidenciasAdapter;

public class ListaComentariosAdapter extends RecyclerView.Adapter<ListaComentariosAdapter.ComentarioViewHolder> {

    Comentario[] listaComentarios;
    private Context contexto;

    public ListaComentariosAdapter(Comentario[] lista, Context contexto){
        this.listaComentarios = lista;
        this.contexto = contexto; }

    public static class ComentarioViewHolder extends RecyclerView.ViewHolder {

        public TextView autor;
        public TextView fechaSubida;
        public TextView descripcion;

        public ComentarioViewHolder(@NonNull View itemView) {
            super(itemView);
            this.autor = itemView.findViewById(R.id.textViewFecha);
            this.fechaSubida = itemView.findViewById(R.id.textViewFecha);
            this.descripcion = itemView.findViewById(R.id.textViewDescripcion); } }

    @NonNull
    @Override
    public ComentarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(contexto).inflate(R.layout.comentario_admin_rv,parent,false);
        ComentarioViewHolder comentarioViewHolder = new ComentarioViewHolder(itemView);
        return comentarioViewHolder; }

    @Override
    public void onBindViewHolder(ComentarioViewHolder holder, int position) {
        Comentario comentario = listaComentarios[position];
        String autor = comentario.getAutorComentario(); holder.autor.setText(autor);
        String fecha = comentario.getFechaComentario(); holder.fechaSubida.setText(fecha);
        String descripcion = comentario.getAutorComentario(); holder.descripcion.setText(descripcion);
    }

    @Override
    public int getItemCount() {
        return listaComentarios.length; }
}
