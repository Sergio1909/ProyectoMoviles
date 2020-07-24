package com.example.proyectomoviles.Administrador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.proyectomoviles.Entidades.Incidencia;
import com.example.proyectomoviles.R;
import com.example.proyectomoviles.Usuarios.DetallesUsuarioActivity;
import com.example.proyectomoviles.Usuarios.IncidenciaUsuarioActivity;
import com.example.proyectomoviles.Usuarios.ListaIncidenciasAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class IncidenciaAdminActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbarinfraestructura,menu);
        return true;
    }

    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private FirebaseStorage fStorage;
    Incidencia[] listaIncidencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidencia_admin);

        mAuth = FirebaseAuth.getInstance();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(); // Base De Datos

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
                            final String nombreRaroIncidencia = dataSnapshot.getKey(); incidencia.setApiKey(nombreRaroIncidencia);
                            final String foto = dataSnapshot.child("fotoAPIKEY").getValue().toString(); incidencia.setFoto(foto);

                            listaIncidencias[contador] = incidencia;
                            contador++;
                        }
                    }
                }

                ListaIncidenciasAdapter2 incidenciasAdapter = new ListaIncidenciasAdapter2(listaIncidencias, IncidenciaAdminActivity.this,fStorage.getReference());
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setAdapter(incidenciasAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(IncidenciaAdminActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(IncidenciaAdminActivity.this,"Error Base de Datos",Toast.LENGTH_LONG).show();
            }
        });





    }
}
