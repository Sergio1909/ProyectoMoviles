package com.example.proyectomoviles.Administrador;

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
import com.example.proyectomoviles.Usuarios.DetallesMisIncidenciasActivity;
import com.example.proyectomoviles.Usuarios.DetallesUsuarioActivity;
import com.example.proyectomoviles.Usuarios.ListaIncidenciasAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

public class ListaIncidenciasAdapter2 extends RecyclerView.Adapter<ListaIncidenciasAdapter2.IncidenciaViewHolder2> {

    Incidencia[] listaIncidencias;
    private Context contexto;
    private StorageReference storageReference;
    private int condicionDetalles;

    public ListaIncidenciasAdapter2(Incidencia[] lista, Context contexto,StorageReference storageReference,
                                    int condicionDetalles){
        this.listaIncidencias = lista;
        this.contexto = contexto;
        this.storageReference = storageReference;
        this.condicionDetalles = condicionDetalles; }

    public static class IncidenciaViewHolder2 extends RecyclerView.ViewHolder {
        public TextView  nombreIncidencia;
        public ImageView fotoIncidencia;
        public TextView  fechaPublicacion;
        public TextView  estado;
        public TextView  ubicacion;
        public Button buttonDetalles;

        public IncidenciaViewHolder2(@NonNull View itemView) {
            super(itemView);
            this.nombreIncidencia = itemView.findViewById(R.id.textViewNombre);
            this.fotoIncidencia = itemView.findViewById(R.id.imageViewFoto);
            this.fechaPublicacion = itemView.findViewById(R.id.textViewFecha);
            this.estado = itemView.findViewById(R.id.textViewEstado);
            this.ubicacion = itemView.findViewById(R.id.textViewLugar);
            this.buttonDetalles = itemView.findViewById(R.id.buttonDetalles);} }


    @NonNull
    @Override
    public IncidenciaViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(contexto).inflate(R.layout.incidencia_admin_rv,parent,false);
        IncidenciaViewHolder2 incidenciaViewHolder = new IncidenciaViewHolder2(itemView);
        return incidenciaViewHolder; }

    @Override
    public void onBindViewHolder(IncidenciaViewHolder2 holder, int position) {
        final Incidencia incidencia = listaIncidencias[position];
        String nombreIncidencia = incidencia.getNombre(); holder.nombreIncidencia.setText(nombreIncidencia);
        String estadoIncidencia = incidencia.getEstado(); holder.estado.setText(estadoIncidencia);
        String fechaPublicacion = incidencia.getFecha(); holder.fechaPublicacion.setText(fechaPublicacion);
        String ubicacionIncidencia = incidencia.getLugar(); holder.ubicacion.setText(ubicacionIncidencia);
        publicarImagen(incidencia.getFoto() + ".JPG", holder);

        if (condicionDetalles == 1) {
            holder.buttonDetalles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(contexto,DetallesAdminActivity.class);
                    String APIKEY = incidencia.getApiKey();
                    intent.putExtra("nombreIncidencia", APIKEY);
                    contexto.startActivity(intent);}
            }); }

        if (condicionDetalles == 2) {
            holder.buttonDetalles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(contexto, DetallesTomadasActivity.class);
                    String APIKEY = incidencia.getApiKey();
                    intent.putExtra("nombreIncidencia", APIKEY);
                    contexto.startActivity(intent);}
            }); }

    }

    public void publicarImagen (final String photoName, final ListaIncidenciasAdapter2.IncidenciaViewHolder2 holder){
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
