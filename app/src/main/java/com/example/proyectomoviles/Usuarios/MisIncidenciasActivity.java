package com.example.proyectomoviles.Usuarios;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MisIncidenciasActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbarusuario,menu);
        return true;
    }

    private FirebaseAuth mAuth;
    Incidencia[] listaMisIncidencias;
    private StorageReference storageReference;
    //private FirebaseStorage fStorage;
    private int DETALLES_INCIDENCIAS_PROPIAS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_incidencias);

        mAuth = FirebaseAuth.getInstance();
        final String nombreUsuario = mAuth.getCurrentUser().getDisplayName();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(); // Base De Datos

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
                            String autor = dataSnapshot.child("autor").getValue().toString();
                            final String nombreRaroIncidencia = dataSnapshot.getKey(); incidencia.setApiKey(nombreRaroIncidencia);
                            final String foto = dataSnapshot.child("fotoAPIKEY").getValue().toString(); incidencia.setFoto(foto);

                            /// Añadir solo si el autor es el usuario logueado
                            String nombreLogueado = mAuth.getCurrentUser().getDisplayName();
                            if (autor.equals(nombreLogueado)){
                            listaMisIncidencias[contador] = incidencia;
                            contador++;} else {contador = contador+0;}
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MisIncidenciasActivity.this,"Error Base de Datos",Toast.LENGTH_LONG).show();
            }
        });

        final StorageReference fStorage = FirebaseStorage.getInstance().getReference();
        ListaIncidenciasAdapter incidenciasAdapter = new ListaIncidenciasAdapter(listaMisIncidencias, MisIncidenciasActivity.this, fStorage,
                DETALLES_INCIDENCIAS_PROPIAS);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(incidenciasAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MisIncidenciasActivity.this));

    }
}
