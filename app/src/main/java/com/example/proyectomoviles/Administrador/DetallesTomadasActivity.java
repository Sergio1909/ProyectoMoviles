package com.example.proyectomoviles.Administrador;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectomoviles.Entidades.Comentario;
import com.example.proyectomoviles.Entidades.Incidencia;
import com.example.proyectomoviles.Entidades.Usuario;
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

    Comentario[] listaComentarios;
    Incidencia incidencia = new Incidencia();
    Usuario usuario = new Usuario();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_admin);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final String apikeyIncidencia = getIntent().getStringExtra("nombreIncidencia");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        databaseReference.child("Usuarios").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()){
                    usuario = snapshot.getValue(Usuario.class);
                } }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        databaseReference.child("Incidencias").child(apikeyIncidencia).addListenerForSingleValueEvent
                (new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        if (dataSnapshot1.exists()) {

                            Incidencia incidencia2 = dataSnapshot1.getValue(Incidencia.class);
                            incidencia = incidencia2;

                            // TextView autor = findViewById(R.id.textViewAutor) ; autor.setText(incidencia.getUsuarioAutor());
                            TextView nombre = findViewById(R.id.textViewNombre); nombre.setText(incidencia.getNombre());
                            TextView estado = findViewById(R.id.textViewEstado); estado.setText(incidencia.getEstado());
                            TextView fecha = findViewById(R.id.textViewFecha); fecha.setText(incidencia.getFecha());
                            TextView ubicacion = findViewById(R.id.textViewLugar); ubicacion.setText(incidencia.getLugar());
                            TextView descripcion = findViewById(R.id.textViewDescripcion); descripcion.setText(incidencia.getDescripcion());
                            publicarImagen(incidencia.getFoto() + ".jpg", storageReference);

                            final double latitudMapa  = incidencia.getLatitud();
                            final double longitudMapa = incidencia.getLongitud();
                            Button butonUbicacion = findViewById(R.id.buttonUbicacion);
                            butonUbicacion.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getSupportFragmentManager().beginTransaction().add(R.id.fragmentMapita, MapitaFragment.newInstance(latitudMapa,longitudMapa),"MapitaFragment").commit();
                                }
                            });

                            Button botonAtender = (Button) findViewById(R.id.buttonAtender);
                            botonAtender.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    incidencia.setEstado("Atendido");
                                    Intent intent = new Intent(getApplicationContext(), DetallesAdminActivity.class);
                                    String nombreFiltro = apikeyIncidencia;
                                    intent.putExtra("nombreIncidencia", nombreFiltro);
                                    startActivity(intent);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(DetallesTomadasActivity.this,"Error Base de Datos",Toast.LENGTH_LONG).show(); }
                });


        databaseReference.child("Incidencias").child(apikeyIncidencia).child("comentarios").addValueEventListener
                (new ValueEventListener() {
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

                                ListaComentariosAdapter comentariosAdapter = new ListaComentariosAdapter(listaComentarios,DetallesTomadasActivity.this);
                                RecyclerView recyclerView = findViewById(R.id.recyclerView4);
                                recyclerView.setAdapter(comentariosAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(DetallesTomadasActivity.this));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(DetallesTomadasActivity.this,"Error Base de Datos",Toast.LENGTH_LONG).show(); }

                });


        String autorComentario = usuario.getNombre();
        EditText cuerpoComentario = (EditText) findViewById(R.id.comentario);
        String descripcionComentario = cuerpoComentario.getText().toString();
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
                    Intent intent = new Intent(getApplicationContext(), DetallesTomadasActivity.class);
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

    // Agregar Fotografía
    public void publicarImagen (String photoName, StorageReference storageReference) {
        storageReference.child("Images").child(photoName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(uri)
                        .load(uri)
                        .into((ImageView) findViewById(R.id.imageViewFoto)); }
        }); }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbarinfraestructura,menu);
        String nombreLogueado = usuario.getNombre();
        // menu.findItem(R.id.nombreUsuario).setTitle(nombreLogueado);
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
