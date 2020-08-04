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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.proyectomoviles.Entidades.Comentario;
import com.example.proyectomoviles.Entidades.Incidencia;
import com.example.proyectomoviles.ListaComentariosAdapter;
import com.example.proyectomoviles.MainActivity;
// import com.example.proyectomoviles.MapitaFragment;
import com.example.proyectomoviles.MapsActivity;
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

public class DetallesMisIncidenciasActivity extends AppCompatActivity {

    Comentario[] listaComentarios;
    Incidencia incidencia = new Incidencia();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_mis_incidencias);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final String apikeyIncidencia = getIntent().getStringExtra("nombreIncidencia");

        databaseReference.child("Incidencias").child(apikeyIncidencia).addListenerForSingleValueEvent
                (new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        if (dataSnapshot1.exists()) {

                            Incidencia incidencia2 = dataSnapshot1.getValue(Incidencia.class);
                            incidencia = incidencia2;

                            TextView autor = findViewById(R.id.textViewAutor) ; autor.setText(incidencia.getUsuarioAutor());
                            TextView nombre = findViewById(R.id.textViewNombre); nombre.setText(incidencia.getNombre());
                            TextView estado = findViewById(R.id.textViewEstado); estado.setText(incidencia.getEstado());
                            TextView fecha = findViewById(R.id.textViewFecha); fecha.setText(incidencia.getFecha());
                            TextView ubicacion = findViewById(R.id.textViewLugar); ubicacion.setText(incidencia.getLugar());
                            TextView descripcion = findViewById(R.id.textViewDescripcion); descripcion.setText(incidencia.getDescripcion());
                            publicarImagen(incidencia.getFoto() + ".jpg", storageReference);

                            Button butonUbicacion = findViewById(R.id.buttonUbicacion);
                            butonUbicacion.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent =new Intent(DetallesMisIncidenciasActivity.this, MapsActivity.class);
                                    Double latitudMapa = incidencia.getLatitud();
                                    Double longitudMapa = incidencia.getLongitud();
                                    intent.putExtra("latitudMapa",String.valueOf(latitudMapa));
                                    intent.putExtra("longitudMapa",String.valueOf(longitudMapa));
                                    DetallesMisIncidenciasActivity.this.startActivity(intent);
                                }
                            });
                           /* butonUbicacion.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getSupportFragmentManager().beginTransaction().add(R.id.fragmentMapita, MapitaFragment.newInstance(latitudMapa,longitudMapa),"MapitaFragment").commit();
                                }
                            }); */
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(DetallesMisIncidenciasActivity.this,"Error Base de Datos",Toast.LENGTH_LONG).show(); }
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


                                ListaComentariosAdapter comentariosAdapter = new ListaComentariosAdapter(listaComentarios,DetallesMisIncidenciasActivity.this);
                                RecyclerView recyclerView = findViewById(R.id.recyclerView2);
                                recyclerView.setAdapter(comentariosAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(DetallesMisIncidenciasActivity.this));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(DetallesMisIncidenciasActivity.this,"Error Base de Datos",Toast.LENGTH_LONG).show(); }

                });

        Button botonEliminar = findViewById(R.id.buttonBorrarAdmin);
        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("Incidencias").child(apikeyIncidencia).removeValue();
                Intent intent = new Intent(getApplicationContext(),MisIncidenciasActivity.class);
                startActivity(intent);
            } });






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
        getMenuInflater().inflate(R.menu.appbarusuario,menu);
        // menu.findItem(R.id.nombreUsuario).setTitle(nombreLogueado);
        return true;  }

    public boolean onOptionsItemSelected(@NotNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.cerrarSesion:
                FirebaseAuth.getInstance().signOut(); finish();
                startActivity(new Intent(DetallesMisIncidenciasActivity.this, MainActivity.class));
                return true;
            case R.id.incidenciasTomadas:
                startActivity(new Intent(DetallesMisIncidenciasActivity.this,MisIncidenciasActivity.class));
                return true;
            case R.id.nuevaIncidencia:
                startActivity(new Intent(DetallesMisIncidenciasActivity.this, NuevaIncidenciaActivity.class));
                return true;
        }
        return onOptionsItemSelected(item);}




}


