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
import com.example.proyectomoviles.MainActivity;
import com.example.proyectomoviles.NuevaIncidenciaActivity;
import com.example.proyectomoviles.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class IncidenciaUsuarioActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Incidencia[] listaIncidencias;
    private StorageReference storageReference;
    private FirebaseStorage fStorage;
    private int DETALLES_INCIDENCIAS_GENERAL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidencia_usuario);

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
                            // final String foto = dataSnapshot.child("foto").getValue().toString(); incidencia.setFoto(foto);
                            final StorageReference fStorage = FirebaseStorage.getInstance().getReference();
                            final ListaIncidenciasAdapter incidenciasAdapter = new ListaIncidenciasAdapter(listaIncidencias, IncidenciaUsuarioActivity.this, fStorage,
                                    DETALLES_INCIDENCIAS_GENERAL);

                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
                            recyclerView.setAdapter(incidenciasAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(IncidenciaUsuarioActivity.this));
                            listaIncidencias[contador] = incidencia;
                            contador++;

                        }
                    }




                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(IncidenciaUsuarioActivity.this,"Error Base de Datos",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbarusuario,menu);
        // String nombreLogueado = mAuth.getCurrentUser().getDisplayName();
        // menu.findItem(R.id.nombreUsuario).setTitle(nombreLogueado); Si se puede dar la bienvenida en
        return true;  }


    public boolean onOptionsItemSelected(@NotNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.cerrarSesion:
                FirebaseAuth.getInstance().signOut(); finish();
                startActivity(new Intent(IncidenciaUsuarioActivity.this, MainActivity.class));
                return true;
            case R.id.incidenciasTomadas:
                startActivity(new Intent(IncidenciaUsuarioActivity.this,MisIncidenciasActivity.class));
                return true;
            case R.id.nuevaIncidencia:
                startActivity(new Intent(IncidenciaUsuarioActivity.this, NuevaIncidenciaActivity.class));
                return true;
        }
        return onOptionsItemSelected(item);}


}