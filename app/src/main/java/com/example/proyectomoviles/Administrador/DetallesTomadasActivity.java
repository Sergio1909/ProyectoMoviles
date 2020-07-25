package com.example.proyectomoviles.Administrador;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectomoviles.Entidades.Comentario;
import com.example.proyectomoviles.Entidades.Incidencia;
import com.example.proyectomoviles.ListaComentariosAdapter;
import com.example.proyectomoviles.MainActivity;
import com.example.proyectomoviles.MapitaFragment;
import com.example.proyectomoviles.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class DetallesTomadasActivity extends AppCompatActivity {



    Incidencia[] listaIncidencias;
    Comentario[] listaComentarios;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    final ImageView fotoIncidencia = findViewById(R.id.imageViewFoto);


        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detalles_admin); // CAMBIAR POR ACTIVITY_DETALLES_TOMADAS

            final Incidencia incidencia = new Incidencia();
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

            // Obtenemos el parametro Enviado
            final String apikeyIncidencia = getIntent().getStringExtra("nombreIncidencia");

            databaseReference.child("Incidencias").child(apikeyIncidencia).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Incidencia incidencia = dataSnapshot.getValue(Incidencia.class);
                        String autor = dataSnapshot.child("autor").getValue().toString();
                        incidencia.setUsuarioAutor(autor);
                        String nombre = dataSnapshot.child("nombre").getValue().toString();
                        incidencia.setNombre(nombre);
                        String estado = dataSnapshot.child("estado").getValue().toString();
                        incidencia.setEstado(estado);
                        String fecha = dataSnapshot.child("fecha").getValue().toString();
                        incidencia.setFecha(fecha);
                        String descripcion = dataSnapshot.child("autor").getValue().toString();
                        incidencia.setDescripcion(descripcion);
                        String ubicacion = dataSnapshot.child("autor").getValue().toString();
                        incidencia.setLugar(ubicacion);
                        String foto = dataSnapshot.child("foto").getValue().toString();
                        incidencia.setFoto(foto);
                        String latitud = dataSnapshot.child("latitud").getValue().toString();
                        double latitudDouble = Double.valueOf(latitud);
                        incidencia.setLatitud(latitudDouble);
                        String longitud = dataSnapshot.child("longitud").getValue().toString();
                        final double longitudDouble = Double.valueOf(longitud);
                        incidencia.setLongitud(longitudDouble);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(DetallesTomadasActivity.this, "Error Base de Datos", Toast.LENGTH_LONG).show();
                }
            });

            databaseReference.child("Incidencias").child(apikeyIncidencia).child("Comentarios").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Long longitudComentarios = dataSnapshot.getChildrenCount();
                        int longitud2 = longitudComentarios.intValue();
                        listaComentarios = new Comentario[longitud2];
                        int contador2 = 0;

                        for (DataSnapshot children : dataSnapshot.getChildren()) {
                            if (dataSnapshot.exists()) {
                                Comentario comentario = children.getValue(Comentario.class);
                                listaComentarios[contador2] = comentario;
                                contador2++;
                            }
                            incidencia.setListaComentarios(listaComentarios);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(DetallesTomadasActivity.this, "Error Base de Datos", Toast.LENGTH_LONG).show();
                }
            });


            final StorageReference fStorage = FirebaseStorage.getInstance().getReference();
            ListaComentariosAdapter comentariosAdapter = new ListaComentariosAdapter(listaComentarios, DetallesTomadasActivity.this);
            RecyclerView recyclerView = findViewById(R.id.recyclerViewUsuario1);
            recyclerView.setAdapter(comentariosAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(DetallesTomadasActivity.this));

            TextView autor = findViewById(R.id.textViewFecha);
            autor.setText(incidencia.getUsuarioAutor());
            TextView nombre = findViewById(R.id.textViewNombre);
            nombre.setText(incidencia.getNombre());
            TextView estado = findViewById(R.id.textViewEstado);
            estado.setText(incidencia.getEstado());
            TextView fecha = findViewById(R.id.textViewFecha);
            fecha.setText(incidencia.getFecha());
            TextView ubicacion = findViewById(R.id.textViewLugar);
            ubicacion.setText(incidencia.getLugar());
            TextView descripcion = findViewById(R.id.textViewDescripcion);
            descripcion.setText(incidencia.getDescripcion());
            publicarImagen(incidencia.getFoto());

            double latitudMapa = incidencia.getLatitud();
            double longitudMapa = incidencia.getLongitud();
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentMapita, MapitaFragment.newInstance(latitudMapa, longitudMapa), "MapitaFragment").commit();

            Button botonAtender = (Button) findViewById(R.id.buttonAtender);
            botonAtender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    incidencia.setEstado("ATENDIDO CRJ");
                    Intent intent = new Intent(getApplicationContext(), DetallesAdminActivity.class);
                    String nombreFiltro = apikeyIncidencia;
                    intent.putExtra("nombreIncidencia", nombreFiltro);
                    startActivity(intent);
                }
            });

            // Autor Comentario
            FirebaseUser usuario = mAuth.getCurrentUser();
            String autorComentario = usuario.getDisplayName();
            //Descripcion Comentario
            EditText cuerpoComentario = (EditText) findViewById(R.id.comentario);
            String descripcionComentario = cuerpoComentario.getText().toString();
            // Fecha Comentario
            SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String fechaComentario = formatter.format(now);


            if (descripcionComentario != null) {
                final Comentario nuevoComentario = new Comentario();
                nuevoComentario.setAutorComentario(autorComentario);
                nuevoComentario.setDescripcionComentario(descripcionComentario);
                nuevoComentario.setFechaComentario(fechaComentario);

                Button botonAgregarComentario = (Button) findViewById(R.id.buttonComentario);
                botonAgregarComentario.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child("Incidencias").child(apikeyIncidencia).child("Comentarios").push().setValue(nuevoComentario);
                        Intent intent = new Intent(getApplicationContext(), DetallesAdminActivity.class);
                        String nombreFiltro = apikeyIncidencia;
                        intent.putExtra("nombreIncidencia", nombreFiltro);
                        startActivity(intent);
                    }
                }); }
            else {
                Button botonAgregarComentarioVacio = (Button) findViewById(R.id.buttonComentario);
                botonAgregarComentarioVacio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(DetallesTomadasActivity.this, "No se puede agregar comentarios vacíos ", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        // PUBLICAR LA PUTA FOTO
        public void publicarImagen (String photoName) {
            storageReference.child("Images").child(photoName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getApplicationContext())
                            .load(uri)
                            .into(fotoIncidencia); }
            }); }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbarinfraestructura,menu);
        String nombreLogueado = mAuth.getCurrentUser().getDisplayName();
        // menu.findItem(R.id.nombreUsuario).setTitle(nombreLogueado); Si se puede dar la bienvenida en
        return true;  }

    public boolean onOptionsItemSelected(@NotNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.cerrarSesion:
                FirebaseAuth.getInstance().signOut(); finish();
                startActivity(new Intent(DetallesTomadasActivity.this, MainActivity.class));
                return true;
            case R.id.incidenciasTomadas:
                startActivity(new Intent(DetallesTomadasActivity.this, IncidenciasTomadasActivity.class));
                return true;

        }
        return onOptionsItemSelected(item);}


}