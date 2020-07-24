package com.example.proyectomoviles.Administrador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class IncidenciasTomadasActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbarinfraestructura,menu);
        return true;
    }

    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    // private FirebaseStorage fStorage;
    Incidencia[] listaIncidenciasTomadas;
    private int DETALLES_INCIDENCIAS_TOMADAS = 2;

    @Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_incidencias_tomadas);

    mAuth = FirebaseAuth.getInstance();
    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(); // Base De Datos
        databaseReference.child("Incidencias").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Long longitudIncidencias = dataSnapshot.getChildrenCount();
                    int longitud = longitudIncidencias.intValue();
                    listaIncidenciasTomadas = new Incidencia[longitud];
                    int contador = 0;

                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        if (dataSnapshot.exists()) {
                            final Incidencia incidencia = children.getValue(Incidencia.class);
                            String autor = dataSnapshot.child("autor").getValue().toString();
                            final String nombreRaroIncidencia = dataSnapshot.getKey(); incidencia.setApiKey(nombreRaroIncidencia);
                            final String foto = dataSnapshot.child("fotoAPIKEY").getValue().toString(); incidencia.setFoto(foto);

                            String nombreLogueado = mAuth.getCurrentUser().getDisplayName();
                            if (autor.equals(nombreLogueado)){
                            listaIncidenciasTomadas[contador] = incidencia;
                            contador++;} else{ contador = contador + 0;}
                        }
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(IncidenciasTomadasActivity.this,"Error Base de Datos",Toast.LENGTH_LONG).show();
            }
        });

        final StorageReference fStorage = FirebaseStorage.getInstance().getReference();
        ListaIncidenciasAdapter2 incidenciasAdapter = new ListaIncidenciasAdapter2(listaIncidenciasTomadas, IncidenciasTomadasActivity.this,fStorage,
                DETALLES_INCIDENCIAS_TOMADAS);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(incidenciasAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(IncidenciasTomadasActivity.this));

}
}