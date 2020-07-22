package com.example.proyectomoviles;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.proyectomoviles.Entidades.IncidenciaDTO;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        IncidenciaDTO incidenciaDTO = new IncidenciaDTO();
        incidenciaDTO.setAutor("Elmo Mazo");
        incidenciaDTO.setDescripcion("Descripcion generica");
        incidenciaDTO.setEstado("sin atender");
        incidenciaDTO.setFecha("hoy dia xd");
        incidenciaDTO.setLugar("Jamaica");
        incidenciaDTO.setNombre("Eventoprueba");
        databaseReference.child("Incidencias").push().setValue(incidenciaDTO).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            Log.d("infoApp", "guardado exitosamente");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("infoApp", "onFailure",e.getCause());
            }
        });

    }

    public void iniciarSesion(View view){

        List<AuthUI.IdpConfig> listaProveedores =
                Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build()

                );

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(listaProveedores)
                        .build(),
                1);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                Log.d("infoApp","inicio de sesion exitoso");
            } else {

                Log.d("infoApp","inicio erroneo");
            }

        }

    }
}
