package com.example.proyectomoviles.Usuarios;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectomoviles.Entidades.Comentario;
import com.example.proyectomoviles.Entidades.Incidencia;
import com.example.proyectomoviles.R;

public class ListaIncidenciasAdapter extends RecyclerView.Adapter<ListaIncidenciasAdapter.IncidenciaViewHolder> {

    Incidencia[] listaIncidencias;
    private Context contexto;

    public ListaIncidenciasAdapter(Incidencia[] lista, Context contexto){
        this.listaIncidencias = lista;
        this.contexto = contexto; }

        public static class IncidenciaViewHolder extends RecyclerView.ViewHolder {
            public TextView  nombreIncidencia;
            public TextView  autorIncidencia;
            public ImageView foto;
            public TextView  fechaPublicacion;
            public TextView  descripcion;
            public TextView  estado;
            public TextView  ubicacion; //  HACER EL USO DE GPS AL FINAL

            public IncidenciaViewHolder(@NonNull View itemView) {
                super(itemView);
                this.nombreIncidencia = itemView.findViewById(R.id.textViewNombre);
                // this.autorIncidencia = itemView.findViewById(R.id.textViewAutor);
                this.foto = itemView.findViewById(R.id.imageViewFoto);
                this.fechaPublicacion = itemView.findViewById(R.id.textViewFecha);
                // this.descripcion = itemView.findViewById(R.id.textViewDescripcion);
                this.estado = itemView.findViewById(R.id.textViewEstado);
                this.ubicacion = itemView.findViewById(R.id.textViewLugar); } }


    @NonNull
    @Override
    public IncidenciaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(contexto).inflate(R.layout.incidencia_usuario_rv,parent,false);
        IncidenciaViewHolder incidenciaViewHolder = new IncidenciaViewHolder(itemView);
        return incidenciaViewHolder; }

    @Override
    public void onBindViewHolder(IncidenciaViewHolder holder, int position) {
        Incidencia incidencia = listaIncidencias[position];
        String nombreIncidencia = incidencia.getNombre(); holder.nombreIncidencia.setText(nombreIncidencia);
        String estadoIncidencia = incidencia.getEstado(); holder.estado.setText(estadoIncidencia);
        String fechaPublicacion = incidencia.getFecha(); holder.fechaPublicacion.setText(fechaPublicacion);
        String ubicacionIncidencia = incidencia.getLugar(); holder.ubicacion.setText(ubicacionIncidencia);
        // String fotografia = incidencia.getFoto(); holder.foto.setImageDrawable(fotografia);
    }

    @Override
    public int getItemCount() {
        return listaIncidencias.length; }


}
