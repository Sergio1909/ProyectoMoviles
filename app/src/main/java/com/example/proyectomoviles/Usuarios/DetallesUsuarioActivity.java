package com.example.proyectomoviles.Usuarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.proyectomoviles.Administrador.DetallesTomadasActivity;
import com.example.proyectomoviles.Entidades.Comentario;
import com.example.proyectomoviles.Entidades.Incidencia;
import com.example.proyectomoviles.ListaComentariosAdapter;
import com.example.proyectomoviles.MainActivity;
import com.example.proyectomoviles.MapitaFragment;
import com.example.proyectomoviles.NuevaIncidenciaActivity;
import com.example.proyectomoviles.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetallesUsuarioActivity extends AppCompatActivity {

    Incidencia[] listaIncidencias;
    Comentario[] listaComentarios;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    Incidencia incidencia = new Incidencia();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_usuario);

        final Incidencia[] incidenciaxXx = {new Incidencia()};
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        // Obtenemos el parametro Enviado :c
        final String apikeyIncidencia = getIntent().getStringExtra("nombreIncidencia");

        databaseReference.child("Incidencias").child(apikeyIncidencia).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                if (dataSnapshot1.exists()) {
                    Incidencia incidencia2 = dataSnapshot1.getValue(Incidencia.class);
                 /*   // incidencia[0] = dataSnapshot.getValue(Incidencia.class);
                     String autor = dataSnapshot.child("autor").getValue().toString(); incidencia.setUsuarioAutor(autor);
                    String nombre = dataSnapshot.child("nombre").getValue().toString(); incidencia.setNombre(nombre);
                    String estado = dataSnapshot.child("estado").getValue().toString(); incidencia.setEstado(estado);
                    String fecha = dataSnapshot.child("fecha").getValue().toString(); incidencia.setFecha(fecha);
                    String descripcion = dataSnapshot.child("descripcion").getValue().toString(); incidencia.setDescripcion(descripcion);
                    String ubicacion = dataSnapshot.child("lugar").getValue().toString(); incidencia.setLugar(ubicacion);
                    String foto = dataSnapshot.child("foto").getValue().toString(); incidencia.setFoto(foto);
                    // Latitud y Longitud
                     String latitud = dataSnapshot.child("latitud").getValue().toString();  double latitudDouble = Double.valueOf(latitud);
                    incidencia.setLatitud(latitudDouble);
                    String longitud = dataSnapshot.child("longitud").getValue().toString(); final  double longitudDouble = Double.valueOf(longitud);
                    incidencia.setLongitud(longitudDouble);
                        */
                    incidencia = incidencia2;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DetallesUsuarioActivity.this,"Error Base de Datos",Toast.LENGTH_LONG).show(); }
        });

        // incidenciaInutil = (Incidencia) incidencia;

        databaseReference.child("Incidencias").child(apikeyIncidencia).child("comentarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                        incidencia.setListaComentarios(listaComentarios);


                        ListaComentariosAdapter comentariosAdapter = new ListaComentariosAdapter(listaComentarios,DetallesUsuarioActivity.this);
                        RecyclerView recyclerView = findViewById(R.id.recyclerViewUsuario1);
                        recyclerView.setAdapter(comentariosAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(DetallesUsuarioActivity.this));

                    }
                }


            }

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
        publicarImagen(incidencia.getFoto());

        double latitudMapa  = incidencia.getLatitud();
        double longitudMapa = incidencia.getLongitud();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentMapita, MapitaFragment.newInstance(latitudMapa,longitudMapa),"MapitaFragment").commit();

        }
   // final ImageView fotoIncidencia = (ImageView) findViewById(R.id.imageViewFoto);
    public void publicarImagen (String photoName) {
        storageReference.child("Images").child(photoName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(uri)
                        .into( (ImageView) findViewById(R.id.imageViewFoto)); }
        }); }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbarusuario,menu);
        //String nombreLogueado = mAuth.getCurrentUser().getDisplayName();
        // menu.findItem(R.id.nombreUsuario).setTitle(nombreLogueado); Si se puede dar la bienvenida en
        return true;  }

    public boolean onOptionsItemSelected(@NotNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.cerrarSesion:
                FirebaseAuth.getInstance().signOut(); finish();
                startActivity(new Intent(DetallesUsuarioActivity.this, MainActivity.class));
                return true;
            case R.id.incidenciasTomadas:
                startActivity(new Intent(DetallesUsuarioActivity.this,MisIncidenciasActivity.class));
                return true;
            case R.id.nuevaIncidencia:
                startActivity(new Intent(DetallesUsuarioActivity.this, NuevaIncidenciaActivity.class));
                return true;
        }
        return onOptionsItemSelected(item);}




}


