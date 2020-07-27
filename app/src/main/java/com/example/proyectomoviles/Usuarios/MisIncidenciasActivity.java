package com.example.proyectomoviles.Usuarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.proyectomoviles.Entidades.Incidencia;
import com.example.proyectomoviles.Entidades.Usuario;
import com.example.proyectomoviles.MainActivity;
import com.example.proyectomoviles.NuevaIncidenciaActivity;
import com.example.proyectomoviles.R;
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

public class MisIncidenciasActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Incidencia[] listaMisIncidencias;
    private int DETALLES_INCIDENCIAS_PROPIAS = 2;
    Usuario usuario = new Usuario();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_incidencias);

        mAuth = FirebaseAuth.getInstance();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        databaseReference.child("Usuarios").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
              usuario = snapshot.getValue(Usuario.class); }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        databaseReference.child("Incidencias").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Long longitudIncidencias = dataSnapshot.getChildrenCount();
                    int longitud = longitudIncidencias.intValue();
                    listaMisIncidencias = new Incidencia[longitud];
                    int contador = 0;

                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        if (dataSnapshot.exists()) {
                            final Incidencia incidencia = children.getValue(Incidencia.class);
                            final String nombreRaroIncidencia = dataSnapshot.getKey(); incidencia.setApiKey(nombreRaroIncidencia);

                            if (incidencia.getUsuarioAutor().equals(usuario.getNombre())){
                            listaMisIncidencias[contador] = incidencia;
                            contador++;} else {contador = contador+0;}

                            final StorageReference fStorage = FirebaseStorage.getInstance().getReference();
                            ListaIncidenciasAdapter incidenciasAdapter = new ListaIncidenciasAdapter(listaMisIncidencias, MisIncidenciasActivity.this, fStorage,
                                    DETALLES_INCIDENCIAS_PROPIAS);
                            RecyclerView recyclerView = findViewById(R.id.recyclerViewUsuario1);
                            recyclerView.setAdapter(incidenciasAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(MisIncidenciasActivity.this));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MisIncidenciasActivity.this,"Error Base de Datos",Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbarusuario,menu);
        // String nombreLogueado = usuario.getNombre();
        // menu.findItem(R.id.nombreUsuario).setTitle(nombreLogueado); Si se puede dar la bienvenida en
        return true;  }

    public boolean onOptionsItemSelected(@NotNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.cerrarSesion:
                FirebaseAuth.getInstance().signOut(); finish();
                startActivity(new Intent(MisIncidenciasActivity.this, MainActivity.class));
                return true;
            case R.id.incidenciasTomadas:
                startActivity(new Intent(MisIncidenciasActivity.this,MisIncidenciasActivity.class));
                return true;
            case R.id.nuevaIncidencia:
                startActivity(new Intent(MisIncidenciasActivity.this, NuevaIncidenciaActivity.class));
                return true;
        }
        return onOptionsItemSelected(item);}


}
