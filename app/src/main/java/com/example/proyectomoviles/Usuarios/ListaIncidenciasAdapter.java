package com.example.proyectomoviles.Usuarios;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectomoviles.Entidades.Incidencia;
import com.example.proyectomoviles.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

public class ListaIncidenciasAdapter extends RecyclerView.Adapter<ListaIncidenciasAdapter.IncidenciaViewHolder> {

    Incidencia[] listaIncidencias;
    private Context contexto;
    private StorageReference storageReference;
    private int condicionDetalles;

    public ListaIncidenciasAdapter(Incidencia[] lista, Context contexto, StorageReference storageReference, int condicionDetalles){
        this.listaIncidencias = lista;
        this.contexto = contexto;
        this.storageReference = storageReference;
        this.condicionDetalles = condicionDetalles;}

        public static class IncidenciaViewHolder extends RecyclerView.ViewHolder {
            public TextView  nombreIncidencia;
            public ImageView fotoIncidencia;
            public TextView  fechaPublicacion;
            public TextView  estado;
            public TextView  ubicacion;
            public Button  buttonDetalles;

            public IncidenciaViewHolder(@NonNull View itemView) {
                super(itemView);
                this.nombreIncidencia = itemView.findViewById(R.id.textViewNombre);
                this.fotoIncidencia = itemView.findViewById(R.id.imageViewFoto);
                this.fechaPublicacion = itemView.findViewById(R.id.textViewFecha);
                this.estado = itemView.findViewById(R.id.textViewEstado);
                this.ubicacion = itemView.findViewById(R.id.textViewLugar);
                this.buttonDetalles = itemView.findViewById(R.id.buttonDetalles); } }


    @NonNull
    @Override
    public IncidenciaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(contexto).inflate(R.layout.incidencia_usuario_rv,parent,false);
        IncidenciaViewHolder incidenciaViewHolder = new IncidenciaViewHolder(itemView);
        return incidenciaViewHolder; }

    @Override
    public void onBindViewHolder(IncidenciaViewHolder holder, int position) {
        final Incidencia incidencia = listaIncidencias[position];
        final String nombreIncidencia = incidencia.getNombre(); holder.nombreIncidencia.setText(nombreIncidencia);
        String estadoIncidencia = incidencia.getEstado(); holder.estado.setText(estadoIncidencia);
        String fechaPublicacion = incidencia.getFecha(); holder.fechaPublicacion.setText(fechaPublicacion);
        String ubicacionIncidencia = incidencia.getLugar(); holder.ubicacion.setText(ubicacionIncidencia);
        publicarImagen(incidencia.getFoto() + ".jpg", holder);

        if (condicionDetalles == 1) {
        holder.buttonDetalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(contexto,DetallesUsuarioActivity.class);
                String APIKEY = incidencia.getApiKey();
                intent.putExtra("nombreIncidencia", APIKEY);
                contexto.startActivity(intent);}
        }); }

        if (condicionDetalles == 2) {
            holder.buttonDetalles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(contexto,DetallesMisIncidenciasActivity.class);
                    String APIKEY = incidencia.getApiKey();
                    intent.putExtra("nombreIncidencia", APIKEY);
                    contexto.startActivity(intent);}
            }); }
    };


    public void publicarImagen (final String photoName, final ListaIncidenciasAdapter.IncidenciaViewHolder holder){
        storageReference.child("Images").child(photoName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(contexto)
                        .load(uri)
                        .into(holder.fotoIncidencia); }
        }); }


    @Override
    public int getItemCount() {
        return listaIncidencias.length; }


}
