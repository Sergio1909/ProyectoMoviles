package com.example.proyectomoviles.Administrador;

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
import com.example.proyectomoviles.Usuarios.IncidenciaUsuarioActivity;
import com.example.proyectomoviles.Usuarios.ListaIncidenciasAdapter;
import com.example.proyectomoviles.Usuarios.MisIncidenciasActivity;
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

public class IncidenciaAdminActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Incidencia[] listaIncidencias;
    private StorageReference storageReference;
    private FirebaseStorage fStorage;
    private int DETALLES_INCIDENCIAS_GENERAL_ADMIN = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidencia_admin);

        mAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Incidencias").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Long longitudIncidencias = dataSnapshot.getChildrenCount();
                    int longitud = longitudIncidencias.intValue();
                    listaIncidencias = new Incidencia[longitud];
                    int contador = 0;

                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        if (dataSnapshot.exists()) {
                            final Incidencia incidencia = children.getValue(Incidencia.class);
                            final String nombreRaroIncidencia = children.getKey();  incidencia.setApiKey(nombreRaroIncidencia);
                            final StorageReference fStorage = FirebaseStorage.getInstance().getReference();
                            final ListaIncidenciasAdapter incidenciasAdapter = new ListaIncidenciasAdapter(listaIncidencias, IncidenciaAdminActivity.this, fStorage,
                                    DETALLES_INCIDENCIAS_GENERAL_ADMIN);

                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewAdmin);
                            recyclerView.setAdapter(incidenciasAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(IncidenciaAdminActivity.this));
                            listaIncidencias[contador] = incidencia;
                            contador++;

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(IncidenciaAdminActivity.this,"Error Base de Datos",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbarinfraestructura,menu);

        return true;  }

    public boolean onOptionsItemSelected(@NotNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.cerrarSesion:
                FirebaseAuth.getInstance().signOut(); finish();
                startActivity(new Intent(IncidenciaAdminActivity.this, MainActivity.class));
                return true;
            case R.id.incidenciasTomadas:
                startActivity(new Intent(IncidenciaAdminActivity.this, IncidenciasTomadasActivity.class));
                return true;

        }
        return onOptionsItemSelected(item);}



}
