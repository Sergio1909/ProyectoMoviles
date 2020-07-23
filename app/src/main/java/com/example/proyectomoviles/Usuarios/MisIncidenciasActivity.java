package com.example.proyectomoviles.Usuarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

public class MisIncidenciasActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Incidencia[] listaMisIncidencias;

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
                            String autor = children.child("autor").getValue().toString();
                            final String nombreRaroIncidencia = dataSnapshot.getKey();

                            // BOTON DETALLES ---> Cambiar despues a un personalizado por el boton borrar/atras y posible editar
                            Button botonDetallesUsuario = (Button) findViewById(R.id.buttonDetalles);
                            botonDetallesUsuario.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // OCULTAR BOTON DE DETALLES ADMIN
                                    Intent intent = new Intent(getApplicationContext(), DetallesUsuarioActivity.class);
                                    String APIKEY = nombreRaroIncidencia;
                                    intent.putExtra("nombreIncidencia", APIKEY);
                                    startActivity(intent);
                                }
                            });

                            /// Añadir solo si el autor es el usuario logueado
                            if (autor.equals(nombreUsuario)){
                            listaMisIncidencias[contador] = incidencia;
                            contador++;} else {}
                        }
                    }
                }

                ListaIncidenciasAdapter incidenciasAdapter = new ListaIncidenciasAdapter(listaMisIncidencias, MisIncidenciasActivity.this);
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setAdapter(incidenciasAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MisIncidenciasActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MisIncidenciasActivity.this,"Error Base de Datos",Toast.LENGTH_LONG).show();
            }
        });



    }
}
