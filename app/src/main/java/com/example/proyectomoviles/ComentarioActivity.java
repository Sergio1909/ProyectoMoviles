package com.example.proyectomoviles;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;

import com.example.proyectomoviles.Entidades.Comentario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class ComentarioActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentario);

        final String nombreIncidencia = getIntent().getStringExtra("nombreIncidencia");

        // Autor Comentario
        FirebaseUser usuario = mAuth.getCurrentUser();
        String autorComentario = usuario.getDisplayName();
        //Descripcion Comentario
        EditText cuerpoComentario = (EditText) findViewById(R.id.comentario);
        String descripcionComentario = cuerpoComentario.getText().toString();
        // Fecha Comentario
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String fechaComentario= formatter.format(now);


        if (descripcionComentario != null){
            Comentario nuevoComentario = new Comentario();
            nuevoComentario.setAutorComentario(autorComentario);
            nuevoComentario.setDescripcionComentario(descripcionComentario);
            nuevoComentario.setFechaComentario(fechaComentario);

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Incidencias").child(nombreIncidencia).child("Comentarios").push().setValue(nuevoComentario);}

    }
}