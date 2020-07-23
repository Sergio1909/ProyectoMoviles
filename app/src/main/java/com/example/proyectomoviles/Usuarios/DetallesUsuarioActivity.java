package com.example.proyectomoviles.Usuarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectomoviles.Entidades.Comentario;
import com.example.proyectomoviles.Entidades.Incidencia;
import com.example.proyectomoviles.ListaComentariosAdapter;
import com.example.proyectomoviles.MapitaFragment;
import com.example.proyectomoviles.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.type.LatLng;

public class DetallesUsuarioActivity extends AppCompatActivity {

    Incidencia[] listaIncidencias;
 Comentario[] listaComentarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_usuario);

      final Incidencia incidencia = new Incidencia();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Obtenemos el parametro Enviado
        final String apikeyIncidencia = getIntent().getStringExtra("nombreIncidencia");

        databaseReference.child("Incidencias").child(apikeyIncidencia).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Incidencia incidencia = dataSnapshot.getValue(Incidencia.class);
                    String autor = dataSnapshot.child("autor").getValue().toString(); incidencia.setUsuarioAutor(autor);
                    String nombre = dataSnapshot.child("nombre").getValue().toString(); incidencia.setNombre(nombre);
                    String estado = dataSnapshot.child("estado").getValue().toString(); incidencia.setEstado(estado);
                    String fecha = dataSnapshot.child("fecha").getValue().toString(); incidencia.setFecha(fecha);
                    String descripcion = dataSnapshot.child("autor").getValue().toString(); incidencia.setDescripcion(descripcion);
                    String ubicacion = dataSnapshot.child("autor").getValue().toString(); incidencia.setLugar(ubicacion);
                    // Latitud y Longitud
                    String latitud = dataSnapshot.child("latitud").getValue().toString();  double latitudDouble = Double.valueOf(latitud);
                    incidencia.setLatitud(latitudDouble);
                    String longitud = dataSnapshot.child("longitud").getValue().toString(); final  double longitudDouble = Double.valueOf(longitud);
                    incidencia.setLongitud(longitudDouble);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DetallesUsuarioActivity.this,"Error Base de Datos",Toast.LENGTH_LONG).show(); }
        });

        databaseReference.child("Incidencias").child(apikeyIncidencia).child("Comentarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Long longitudComentarios = dataSnapshot.getChildrenCount();
                    int longitud2 = longitudComentarios.intValue();
                    listaComentarios = new Comentario[longitud2];
                    int contador2 = 0;

                    for (DataSnapshot children: dataSnapshot.getChildren()){
                        if (dataSnapshot.exists()){
                            Comentario comentario = children.getValue(Comentario.class);
                            listaComentarios[contador2] = comentario;
                            contador2++; }
                        incidencia.setListaComentarios(listaComentarios);}
                }

                ListaComentariosAdapter comentariosAdapter = new ListaComentariosAdapter(listaComentarios,DetallesUsuarioActivity.this);
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setAdapter(comentariosAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(DetallesUsuarioActivity.this)); }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DetallesUsuarioActivity.this,"Error Base de Datos",Toast.LENGTH_LONG).show(); }
        });

        TextView autor = findViewById(R.id.textViewFecha); autor.setText(incidencia.getUsuarioAutor());
        TextView nombre = findViewById(R.id.textViewNombre); nombre.setText(incidencia.getNombre());
        TextView estado = findViewById(R.id.textViewEstado); estado.setText(incidencia.getEstado());
        TextView fecha = findViewById(R.id.textViewFecha); fecha.setText(incidencia.getFecha());
        TextView ubicacion = findViewById(R.id.textViewLugar); ubicacion.setText(incidencia.getLugar());
        TextView descripcion = findViewById(R.id.textViewDescripcion); descripcion.setText(incidencia.getDescripcion());


        double latitudMapa  = incidencia.getLatitud();
        double longitudMapa = incidencia.getLongitud();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentMapita, MapitaFragment.newInstance(latitudMapa,longitudMapa),"MapitaFragment").commit();

        }

}


